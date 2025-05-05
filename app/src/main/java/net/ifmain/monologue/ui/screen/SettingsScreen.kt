package net.ifmain.monologue.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import net.ifmain.monologue.ui.component.SettingsUI
import net.ifmain.monologue.ui.component.ShowDialog
import net.ifmain.monologue.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
    onNavigateToIntro: () -> Unit = {},
    onNavigateToLicense: () -> Unit = {}
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "설정",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                SettingsUI(
                    title = "오픈 소스",
                    content = "오픈 소스 라이선스",
                    onNavigateToPushAlarm = null,
                    onNavigateToPremium = null,
                    onNavigateToLicense = onNavigateToLicense
                )
                Text(
                    text = "로그아웃",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .clickable {
                            viewModel.logout(
                                onComplete = {
                                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT)
                                        .show()
                                    onNavigateToIntro()
                                }
                            )
                        }
                )
                Text(
                    text = "계정 삭제",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .clickable {
                            showDialog = true
                        }
                )
            }
        }
    )
    if (showDialog) {
        ShowDialog(
            onConfirm = {
                showDialog = false
                viewModel.withdraw(
                    onSuccess = {
                        Toast.makeText(context, "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        onNavigateToIntro()
                    },
                    onError = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onDismiss = {
                showDialog = false
            },
            title = "계정 삭제",
            text = "계정을 삭제하시겠습니까?\n삭제된 계정은 복구할 수 없습니다."
        )
    }
}
