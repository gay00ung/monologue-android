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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import net.ifmain.monologue.ui.component.InputCard
import net.ifmain.monologue.ui.component.InputTextField
import net.ifmain.monologue.ui.component.TitleBar
import net.ifmain.monologue.ui.theme.Cream
import net.ifmain.monologue.ui.theme.Honey
import net.ifmain.monologue.viewmodel.SignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onSignInClick: (name: String, userId: String) -> Unit,
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
                        InputTextField(
                            "비밀번호",
                            viewModel.password,
                            isPassword = true
                        ) { viewModel.password = it }
                    }

                    Button(
                        onClick = {
                            when {
                                viewModel.hasEmptyFields() -> {
                                    Toast.makeText(context, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                else -> {
                                    viewModel.signIn(
                                        onSuccess = { name ->
                                            Toast.makeText(
                                                context,
                                                "${name}님 환영합니다!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            onSignInClick(name, viewModel.userId)
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
                            text = "로그인",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    )
}