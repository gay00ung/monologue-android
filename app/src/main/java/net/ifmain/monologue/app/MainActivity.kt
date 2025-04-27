package net.ifmain.monologue.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.ifmain.monologue.data.model.DiaryUiState
import net.ifmain.monologue.ui.screen.DiaryHomeScreen
import net.ifmain.monologue.ui.screen.DiaryListScreen
import net.ifmain.monologue.ui.screen.IntroScreen
import net.ifmain.monologue.ui.screen.auth.SignInScreen
import net.ifmain.monologue.ui.screen.auth.SignUpScreen
import net.ifmain.monologue.ui.theme.MonologueTheme
import net.ifmain.monologue.viewmodel.DiaryViewModel
import net.ifmain.monologue.viewmodel.IntroViewModel
import net.ifmain.monologue.viewmodel.SignInViewModel
import net.ifmain.monologue.viewmodel.SignUpViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonologueTheme {
                StartNavigation()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun StartNavigation(
) {
    val navController = rememberNavController()
    var userId by remember { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = "intro_screen",
    ) {
        composable("intro_screen") {
            val introViewModel: IntroViewModel = hiltViewModel()
            IntroScreen(
                onSignInClick = { navController.navigate("sign_in_screen") },
                onSignUpClick = { navController.navigate("sign_up_screen") },
                onNavigateToMain = { name, id ->
                    userId = id
                    navController.navigate("diary_home_screen")
                },
                viewModel = introViewModel
            )
        }
        composable("sign_in_screen") {
            val signInViewModel: SignInViewModel = hiltViewModel()

            SignInScreen(
                viewModel = signInViewModel,
                onSignInClick = { name, id ->
                    userId = id
                    navController.navigate("diary_home_screen")
                }
            )
        }
        composable("sign_up_screen") {
            val signUpViewModel: SignUpViewModel = hiltViewModel()

            SignUpScreen(
                viewModel = signUpViewModel,
                onNavigateToMain = { name, id ->
                    userId = id
                    navController.navigate("diary_home_screen")
                },
            )
        }
        composable("diary_home_screen") {
            val diaryViewModel: DiaryViewModel = hiltViewModel()
            diaryViewModel.userId = userId ?: ""
            diaryViewModel.syncOfflineEntries()
            DiaryHomeScreen(
                viewModel = diaryViewModel,
                onTextChange = diaryViewModel::onTextChange,
                onMoodSelect = diaryViewModel::onMoodSelect,
                onAnalyzeClick = { diaryViewModel.onAnalyzeClick() },
                onSaveClick = { selectedMood, textToSave ->
                    diaryViewModel.onSaveClick(
                        onError = { errorMessage ->
                            println("Error: $errorMessage")
                        },
                        onSuccess = {
                            println("Successfully saved!")
                        }
                    )
                },
                onNavigateToDiaryList = { navController.navigate("diary_list_screen") }
            )
        }

        composable("diary_list_screen") {
            val diaryViewModel: DiaryViewModel = hiltViewModel()
            diaryViewModel.userId = userId ?: ""

            DiaryListScreen(
                viewModel = diaryViewModel,
                onEntryClick = { entry ->
//                    navController.navigate("diary_detail_screen/${entry.date}")
                }
            )
        }

    }
}