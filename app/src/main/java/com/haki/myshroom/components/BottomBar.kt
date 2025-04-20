//package com.haki.syncalarm.components
//
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.pager.PagerState
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.res.vectorResource
//import com.haki.myshroom.R
//import com.haki.myshroom.navigation.NavigationItem
//import com.haki.myshroom.navigation.Screen
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun BottomBar(
//    modifier: Modifier = Modifier,
//    pagerState: PagerState,
//) {
//    val coroutineScope = rememberCoroutineScope()
//
//    NavigationBar(modifier = modifier) {
//        val navigationItems = listOf(
//            NavigationItem(
//                title = stringResource(R.string.alarm),
//                icon = R.drawable.ic_alarm,
//                screen = Screen.Alarm
//            ),
//        )
//
//        navigationItems.forEachIndexed { index, item ->
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        imageVector = ImageVector.vectorResource(item.icon),
//                        contentDescription = item.title
//                    )
//                },
//                label = { Text(item.title) },
//                selected = pagerState.currentPage == index,
//                onClick = {
//                    coroutineScope.launch {
//                        pagerState.animateScrollToPage(index)
//                    }
//                }
//            )
//        }
//    }
//}
