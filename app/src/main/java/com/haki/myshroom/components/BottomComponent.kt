package com.haki.myshroom.components//package com.haki.syncalarm.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.haki.syncalarm.ui.theme.AccentColor
//import com.haki.syncalarm.ui.theme.Secondary
//
//@Composable
//fun BottomComponent(
//    textQuery: String,
//    textClickable: String,
//    action: String,
//    navController: NavHostController
//) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Button(
//                onClick = { /*TODO*/ },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(Color.Transparent)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .background(
//                            brush = Brush.horizontalGradient(listOf(Secondary, AccentColor)),
//                            shape = RoundedCornerShape(50.dp)
//                        )
//                        .fillMaxWidth()
//                        .heightIn(48.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = action, color = Color.White, fontSize = 20.sp)
//                }
//            }
//            Spacer(modifier = Modifier.height(10.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Divider(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f),
//                    thickness = 1.dp,
//                    color = GrayColor
//                )
//                Text(
//                    text = "Or",
//                    modifier = Modifier.padding(10.dp),
//                    fontSize = 20.sp
//                )
//                Divider(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f),
//                    thickness = 1.dp,
//                    color = GrayColor
//                )
//            }
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Button(
//                    onClick = { /*TODO*/ },
//                    colors = ButtonDefaults.buttonColors(Color.Transparent),
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .border(
//                            width = 2.dp,
//                            color = Color(android.graphics.Color.parseColor("#d2d2d2")),
//                            shape = RoundedCornerShape(20.dp)
//                        )
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.google_svg),
//                        contentDescription = "Google Logo",
//                        modifier = Modifier
//                            .size(30.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.width(10.dp))
//                Button(
//                    onClick = { /*TODO*/ },
//                    colors = ButtonDefaults.buttonColors(Color.Transparent),
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .border(
//                            width = 2.dp,
//                            color = Color(android.graphics.Color.parseColor("#d2d2d2")),
//                            shape = RoundedCornerShape(20.dp)
//                        )
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.facebook_svg),
//                        contentDescription = "Google Logo",
//                        modifier = Modifier
//                            .size(30.dp)
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.height(15.dp))
//            AccountQueryComponent(textQuery, textClickable, navController)
//        }
//    }
//}