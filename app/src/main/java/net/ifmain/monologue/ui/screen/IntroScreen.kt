package net.ifmain.monologue.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ifmain.monologue.ui.screen.auth.SignInScreen
import net.ifmain.monologue.ui.theme.Cream
import net.ifmain.monologue.ui.theme.Honey
import net.ifmain.monologue.ui.theme.Lemon

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun IntroScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var isLogoCentered by remember { mutableStateOf(true) }
    var isButtonVisible by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val screenHeight = maxHeight.value
            val dynamicTitleFontSize = (screenHeight * 0.07).sp
            val dynamicSmallFontSize = (screenHeight * 0.017).sp
            val animatedOffsetY by animateDpAsState(
                targetValue = if (isLogoCentered) maxHeight / 2 - 200.dp else 100.dp,
                animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = animatedOffsetY)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "모노로그📝",
                    fontSize = dynamicTitleFontSize,
                    textAlign = TextAlign.Center,
                    color = Honey,
                    style = MaterialTheme.typography.displayLarge
                )

                if (isLogoCentered) {
                    Text(
                        text = "하루 한 줄 기록 하는 나의 하루",
                        modifier = Modifier.padding(top = 12.dp),
                        fontSize = dynamicSmallFontSize,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
        if (isButtonVisible) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 160.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        onSignInClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Honey,
                        contentColor = Honey
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = "이메일로 로그인",
                        fontSize = 18.sp,
                        color = Cream,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "아직 계정이 없으신가요?",
                        modifier = Modifier.padding(top = 15.dp),
                        fontSize = 13.sp,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "회원가입",
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .clickable {
                                onSignUpClick()
                            },
                        fontSize = 13.sp,
                        color = Lemon,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun IntroScreenPreview() {
    IntroScreen(
        onSignInClick = {},
        onSignUpClick = {}
    )
}