package net.ifmain.monologue.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import net.ifmain.monologue.ui.component.InputCard
import net.ifmain.monologue.ui.component.InputTextField
import net.ifmain.monologue.ui.component.TitleBar
import net.ifmain.monologue.ui.theme.Cream
import net.ifmain.monologue.ui.theme.Honey
import net.ifmain.monologue.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Scaffold(
        containerColor = Cream,
        topBar = { TitleBar() },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .pointerInput(Unit) {
                        detectTapGestures { focusManager.clearFocus() }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InputCard {
                        InputTextField("이메일", viewModel.email) { viewModel.email = it }
                        InputTextField("사용자 이름", viewModel.username) { viewModel.username = it }
                        InputTextField(
                            "비밀번호",
                            viewModel.password,
                            isPassword = true
                        ) { viewModel.password = it }
                        Text(
                            text = "⚠️비밀번호는 대소문자와 숫자를 포함하며 8자 이상이어야 합니다.",
                            fontSize = 10.sp,
                            color = LightGray,
                        )
                        InputTextField(
                            "비밀번호 확인",
                            viewModel.confirmPassword,
                            isPassword = true
                        ) { viewModel.confirmPassword = it }
                    }

                    Button(
                        onClick = {
                            when {
                                viewModel.hasEmptyFields() -> {
                                    Toast.makeText(context, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                else -> {
                                    viewModel.signUp(
                                        onSuccess = {
                                            onNavigateToMain()
                                            Toast.makeText(context, "회원가입 성공!", Toast.LENGTH_SHORT)
                                                .show()
                                        },
                                        onError = { errorMsg ->
                                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Honey,
                            contentColor = Cream
                        ),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(
                            text = "회원가입",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    )
}