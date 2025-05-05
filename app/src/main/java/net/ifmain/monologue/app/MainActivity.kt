package net.ifmain.monologue.app

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.ifmain.monologue.ui.screen.DiaryScreen
import net.ifmain.monologue.ui.screen.DiaryListScreen
import net.ifmain.monologue.ui.screen.IntroScreen
import net.ifmain.monologue.ui.screen.auth.SignInScreen
import net.ifmain.monologue.ui.screen.auth.SignUpScreen
import net.ifmain.monologue.ui.theme.MonologueTheme
import net.ifmain.monologue.viewmodel.DiaryViewModel
import net.ifmain.monologue.viewmodel.IntroViewModel
import net.ifmain.monologue.viewmodel.SignInViewModel
import net.ifmain.monologue.viewmodel.SignUpViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.ifmain.monologue.ui.screen.LicenseScreen
import net.ifmain.monologue.ui.screen.SettingsScreen

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
    val context = LocalContext.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    NavHost(
        navController = navController,
        startDestination = "intro_screen",
    ) {
        composable("intro_screen") {
            val introViewModel: IntroViewModel = hiltViewModel()
            IntroScreen(
                onSignInClick = { navController.navigate("sign_in_screen") },
                onSignUpClick = { navController.navigate("sign_up_screen") },
                onNavigateToDiaryScreen = { name, id ->
                    userId = id
                    navController.navigate("diary_write_screen") {
                        popUpTo("diary_write_screen") { inclusive = true }
                    }
                },
                onNavigateToDiaryList = { name, id ->
                    userId = id
                    navController.navigate("diary_list_screen") {
                        popUpTo("diary_list_screen") { inclusive = true }
                    }
                },
                viewModel = introViewModel
            )
        }
        composable("sign_in_screen") {
            val signInViewModel: SignInViewModel = hiltViewModel()

            SignInScreen(
                signInViewModel = signInViewModel,
                introViewModel = hiltViewModel(),
                onNavigateToDiaryScreen = { name, id ->
                    userId = id
                    navController.navigate("diary_write_screen") {
                        popUpTo("diary_write_screen") { inclusive = true }
                    }
                },
                onNavigateToDiaryList = { name, id ->
                    userId = id
                    navController.navigate("diary_list_screen") {
                        popUpTo("diary_list_screen") { inclusive = true }
                    }
                },
            )
        }
        composable("sign_up_screen") {
            val signUpViewModel: SignUpViewModel = hiltViewModel()

            SignUpScreen(
                viewModel = signUpViewModel,
                onNavigateToMain = { name, id ->
                    userId = id
                    navController.navigate("diary_write_screen") {
                        popUpTo("diary_write_screen") { inclusive = true }
                    }
                },
            )
        }
        composable("diary_write_screen") {
            val diaryViewModel: DiaryViewModel = hiltViewModel()
            diaryViewModel.userId = userId ?: ""
            diaryViewModel.syncOfflineEntries()
            DiaryScreen(
                userId = diaryViewModel.userId,
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
                userId = diaryViewModel.userId,
                onNavigateToDiaryDetail = { entry ->
                    navController.navigate("diary_detail_screen/${entry.date}")
                },
                onNavigateToSettings = {
                    navController.navigate("settings_screen")
                }
            )
        }

        composable("diary_detail_screen/{date}") { backStackEntry ->
            val date = backStackEntry.arguments!!.getString("date")!!
            val diaryViewModel: DiaryViewModel = hiltViewModel()
            diaryViewModel.userId = userId ?: ""
            val entries by diaryViewModel.diaryEntries.collectAsStateWithLifecycle(initialValue = emptyList())
            val diaryEntry = entries.firstOrNull { it.date == date }

            DiaryScreen(
                userId = diaryViewModel.userId,
                diaryEntry = diaryEntry,
                viewModel = diaryViewModel,
                onTextChange = diaryViewModel::onTextChange,
                onMoodSelect = diaryViewModel::onMoodSelect,
                onAnalyzeClick = { diaryViewModel.onAnalyzeClick() },
                onSaveClick = { _, _ ->
                    diaryViewModel.updateDiary(diaryViewModel.uiState, diaryViewModel.userId, date)
                    navController.popBackStack()
                },
                onNavigateToDiaryList = { navController.popBackStack() }
            )
        }

        composable ("settings_screen") {
            SettingsScreen(
                onNavigateToIntro = { navController.navigate("intro_screen") },
                onNavigateToLicense = { navController.navigate("license_screen") }
            )
        }

        composable("license_screen") {
            LicenseScreen()
        }
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        when (navController.currentDestination?.route) {
            "intro_screen", "diary_write_screen", "diary_list_screen" -> {
                if (currentTime - backPressedTime < 2000) {
                    (context as? ComponentActivity)?.finish()
                } else {
                    Toast.makeText(context, "뒤로가기를 한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                    backPressedTime = currentTime
                }
            }

            else -> {
                navController.popBackStack()
            }
        }
    }
}