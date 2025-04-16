package net.ifmain.monologue

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.ifmain.monologue.data.DiaryUiState
import net.ifmain.monologue.ui.screen.HomeScreen
import net.ifmain.monologue.ui.screen.IntroScreen
import net.ifmain.monologue.ui.screen.auth.SignInScreen
import net.ifmain.monologue.ui.screen.auth.SignUpScreen
import net.ifmain.monologue.ui.theme.MonologueTheme
import net.ifmain.monologue.viewmodel.DiaryViewModel
import net.ifmain.monologue.viewmodel.SignInViewModel
import net.ifmain.monologue.viewmodel.SignUpViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonologueTheme {
                StartNavigation(
                    diaryUiState = DiaryUiState(),
                    diaryViewModel = DiaryViewModel()
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun StartNavigation(
    diaryUiState: DiaryUiState,
    diaryViewModel: DiaryViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "intro_screen",
    ) {
        composable("intro_screen") {
            IntroScreen(
                onSignInClick = {navController.navigate("sign_in_screen")},
                onSignUpClick = {navController.navigate("sign_up_screen")}
            )
        }
        composable("sign_in_screen") {
            SignInScreen(
                viewModel = SignInViewModel(),
                onSignInClick = { navController.navigate("home_screen") }
            )
        }
        composable("sign_up_screen") {
            SignUpScreen(
                viewModel = SignUpViewModel(),
                onSignUpClick = { navController.navigate("home_screen") },
            )
        }
        composable("home_screen") {
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