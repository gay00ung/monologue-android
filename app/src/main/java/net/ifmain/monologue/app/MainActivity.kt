package net.ifmain.monologue.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.ifmain.monologue.data.model.DiaryUiState
import net.ifmain.monologue.ui.screen.HomeScreen
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
                StartNavigation(
                    diaryUiState = DiaryUiState()
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun StartNavigation(
    diaryUiState: DiaryUiState
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "intro_screen",
    ) {
        composable("intro_screen") {
            val introViewModel: IntroViewModel = hiltViewModel()
            IntroScreen(
                onSignInClick = {navController.navigate("sign_in_screen")},
                onSignUpClick = {navController.navigate("sign_up_screen")},
                onNavigateToMain = {navController.navigate("home_screen")},
                viewModel = introViewModel
            )
        }
        composable("sign_in_screen") {
            val signInViewModel: SignInViewModel = hiltViewModel()

            SignInScreen(
                viewModel = signInViewModel,
                onSignInClick = { navController.navigate("home_screen") }
            )
        }
        composable("sign_up_screen") {
            val signUpViewModel: SignUpViewModel = hiltViewModel()

            SignUpScreen(
                viewModel = signUpViewModel,
                onNavigateToMain = { navController.navigate("home_screen") },
            )
        }
        composable("home_screen") {
            val diaryViewModel: DiaryViewModel = hiltViewModel()
            HomeScreen(
                uiState = diaryUiState,
                onTextChange = { diaryViewModel.onTextChange(it) },
                onMoodSelect = { diaryViewModel.onMoodSelect(it) },
                onAnalyzeClick = { diaryViewModel.onAnalyzeClick() },
                onSaveClick = { diaryViewModel.onSaveClick() },
                onNavigateToDiaryList = { navController.navigate("diary_list_screen") }
            )
        }
    }
}