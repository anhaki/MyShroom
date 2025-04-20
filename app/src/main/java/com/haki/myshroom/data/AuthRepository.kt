package com.haki.myshroom.data

import android.util.Log
import com.haki.myshroom.database.entity.UserEntity
import com.haki.myshroom.preferences.UserPrefModel
import com.haki.myshroom.preferences.UserPreference
import com.haki.myshroom.states.AuthState
import com.haki.myshroom.states.ResultState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userPreference: UserPreference,
    private val sbClient: SupabaseClient,
) {
    private val _isPremiumState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    val isPremiumState: StateFlow<Boolean>
        get() = _isPremiumState

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: Flow<AuthState> = _authState

    private val _userSession = MutableStateFlow<ResultState<UserPrefModel>>(ResultState.Loading)

    val userSession: StateFlow<ResultState<UserPrefModel>>
        get() = _userSession

    init {
        CoroutineScope(Dispatchers.Main).launch {
            checkAuthState()
            checkUserSession()
            checkIsPremium()
        }
    }

    private suspend fun checkIsPremium() {
        _isPremiumState.value = userPreference.getUserSession().first().isPremium
    }

    private fun checkAuthState() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val session = userPreference.getUserSession().first()
                if (session.isLogin && session.email.isNotBlank()) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                Log.e("Error check auth", e.message.toString())
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Unauthenticated
    }

    private suspend fun checkUserSession() {
        flowOf(
            userPreference.getUserSession().first()
        ).catch {
            _userSession.value = ResultState.Error(it.message.toString())
        }.collect { user ->
            _userSession.value = ResultState.Success(user)
        }
    }

    private suspend fun saveUserSession(user: UserPrefModel) {
        userPreference.saveUserSession(user)
    }

    suspend fun logout() {
        _authState.value = AuthState.Unauthenticated
        userPreference.logout()
        checkUserSession()
    }

    suspend fun updatePremiumEveryOpen() {
        val uuid = userPreference.getUserSession().first().id
        if(uuid.isNotBlank()){
            val user = fetchUserProfile(uuid)
            updateIsPremium(user.isPremium)
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all the fields")
            return
        }

        _authState.value = AuthState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            try {
                sbClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                val user = fetchUserProfile(sbClient.auth.currentUserOrNull()?.id.orEmpty())
                userPrefHelper(email, user.name, user.isPremium, user.isAdmin)
            } catch (e: BadRequestRestException) {
                _authState.value = AuthState.Error(e.error)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message.orEmpty())
            }
        }
    }

    fun register(email: String, name: String, password: String, confirmPassword: String) {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$".toRegex()

        when {
            email.isBlank() || name.isBlank() || password.isBlank() -> {
                _authState.value = AuthState.Error("Please fill in all the fields")
            }

            !password.matches(passwordRegex) -> {
                _authState.value =
                    AuthState.Error("Password must be at least 6 characters long and contain both letters and numbers")
            }

            password != confirmPassword -> {
                _authState.value = AuthState.Error("Password doesn't match")
            }

            else -> {
                _authState.value = AuthState.Loading
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        sbClient.auth.signUpWith(Email) {
                            this.email = email
                            this.password = password
                        }

                        updateUserProfile(email, name)
                        userPrefHelper(email, name)
                    } catch (e: BadRequestRestException) {
                        _authState.value = AuthState.Error(e.error)
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error(e.message.orEmpty())
                    }
                }
            }
        }
    }

    private suspend fun fetchUserProfile(userId: String): UserEntity {
        return sbClient.from("profile").select {
            filter { eq("id", userId) }
        }.decodeSingle()
    }

    suspend fun fetchUsers(): List<UserEntity> {
        val user = sbClient.from("profile").select {
            filter { eq("is_admin", false) }
            order(column = "is_premium", order = Order.DESCENDING)
            order(column = "name", order = Order.ASCENDING)
        }.decodeList<UserEntity>()

        return user
    }

    private suspend fun updateUserProfile(email: String, name: String) {
        sbClient.from("profile").update(UserEntity(name = name)) {
            filter { eq("email", email) }
        }
    }

    suspend fun updateUser(email: String, name: String, isPremium: Boolean, expDate: String): ResultState<UserEntity> {
        return try {
            val user = sbClient.from("profile").update(
                {
                    set("name", name)
                    set("is_premium", isPremium)
                    set("exp_date", if(expDate == "null") null else expDate)
                }
            ) {
                select()
                filter {
                    eq("email", email)
                }
            }.decodeSingle<UserEntity>()

            ResultState.Success(user)
        } catch (e: BadRequestRestException) {
            ResultState.Error(e.error)
        } catch (e: Exception) {
            ResultState.Error(e.message.orEmpty())
        }
    }

    private suspend fun updateIsPremium(isPremium: Boolean) {
        userPreference.updatePremium(isPremium = isPremium)
        checkUserSession()
    }

    private suspend fun userPrefHelper(
        email: String,
        name: String = "",
        isPremium: Boolean = false,
        isAdmin: Boolean = false,
    ) {
        Log.e("id from initial", sbClient.auth.currentUserOrNull()?.id.toString())
        saveUserSession(
            UserPrefModel(
                id = sbClient.auth.currentUserOrNull()?.id.toString(),
                name = name,
                email = email,
                isPremium = isPremium,
                token = sbClient.auth.currentAccessTokenOrNull(),
                isLogin = true,
                isAdmin = isAdmin,
            )
        )
        checkAuthState()
        checkUserSession()
    }

    suspend fun realtimeDb(scope: CoroutineScope) {
        try {
            val channel = sbClient.channel("profile")
            val changeFlow = channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
                table = "profile"
            }
            changeFlow.onEach {
                val stringifiedData = it.record.toString()
                val data = Json.decodeFromString<UserEntity>(stringifiedData)
                if (data.email == userPreference.getUserSession().first().email) {
                    updateIsPremium(data.isPremium)
                    _isPremiumState.value = data.isPremium
                }
            }.launchIn(scope)
            channel.subscribe()
        } catch (_: Exception) {

        }
    }
}