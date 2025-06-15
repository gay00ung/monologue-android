package net.ifmain.monologue.app

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import net.ifmain.monologue.ui.screen.*
import net.ifmain.monologue.ui.screen.auth.*
import net.ifmain.monologue.ui.theme.MonologueTheme
import net.ifmain.monologue.viewmodel.*

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
fun StartNavigation() {
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
                onSignInClick = {
                    navController.navigate("sign_in_screen") {
                        launchSingleTop = true
                    }
                },
                onSignUpClick = {
                    navController.navigate("sign_up_screen") {
                        launchSingleTop = true
                    }
                },
                onNavigateToDiaryScreen = { name, id ->
                    userId = id
                    navController.navigate("diary_write_screen/$userId") {
                        popUpTo("intro_screen") { inclusive = true }
                    }
                },
                onNavigateToDiaryList = { name, id ->
                    userId = id
                    navController.navigate("diary_list_screen/$userId") {
                        popUpTo("intro_screen") { inclusive = true }
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
                    navController.navigate("diary_write_screen/$userId") {
                        popUpTo("intro_screen") { inclusive = true }
                    }
                },
                onNavigateToDiaryList = { name, id ->
                    userId = id
                    navController.navigate("diary_list_screen/$userId") {
                        popUpTo("intro_screen") { inclusive = true }
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
                    navController.navigate("diary_write_screen/$userId") {
                        popUpTo("intro_screen") { inclusive = true }
                    }
                },
            )
        }
        composable(
            route = "diary_write_screen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val diaryViewModel: DiaryViewModel = hiltViewModel()
            diaryViewModel.userId = userId ?: ""
            DiaryScreen(
                userId = diaryViewModel.userId,
                viewModel = diaryViewModel,
                onTextChange = diaryViewModel::onTextChange,
                onMoodSelect = diaryViewModel::onMoodSelect,
                onAnalyzeClick = { diaryViewModel.onAnalyzeClick() },
                onSaveClick = { selectedMood, textToSave ->
                    diaryViewModel.onSaveClick(
                        onError = { errorMessage -> println("Error: $errorMessage") },
                        onSuccess = { println("Successfully saved!") }
                    )
                },
                onNavigateToDiaryList = {
                    navController.navigate("diary_list_screen/${diaryViewModel.userId}") {
                        popUpTo("diary_write_screen/${diaryViewModel.userId}") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "diary_list_screen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val diaryViewModel: DiaryViewModel = hiltViewModel()
            diaryViewModel.userId = userId ?: ""
            val userId = backStackEntry.arguments!!.getString("userId")!!
            DiaryListScreen(
                viewModel = diaryViewModel,
                userId = userId,
                onNavigateToDiaryDetail = { entry ->
                    navController.navigate("diary_detail_screen/${entry.date}")
                },
                onNavigateToSettings = {
                    navController.navigate("settings_screen/$userId")
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
        composable(
            route = "settings_screen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            SettingsScreen(
                onNavigateToIntro = {
                    navController.navigate("intro_screen") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToLicense = {
                    navController.navigate("license_screen")
                }
            )
        }
        composable("license_screen") {
            LicenseScreen()
        }
    }

    BackHandler {
        val currentRoute = navController.currentBackStackEntry?.destination?.route ?: ""
        val now = System.currentTimeMillis()

        when {
            currentRoute.startsWith("sign_in_screen") || currentRoute.startsWith("sign_up_screen") -> {
                navController.navigate("intro_screen") {
                    popUpTo("sign_in_screen") { inclusive = true }
                }
            }
            currentRoute.startsWith("license_screen") -> {
                navController.popBackStack("settings_screen/${userId}", false)
            }
            currentRoute.startsWith("settings_screen") -> {
                navController.popBackStack("diary_list_screen/${userId}", false)
            }
            currentRoute.startsWith("intro_screen") || currentRoute.startsWith("diary_list_screen") -> {
                if (now - backPressedTime < 2000) {
                    (context as? ComponentActivity)?.finish()
                } else {
                    Toast.makeText(
                        context,
                        "뒤로가기를 한 번 더 누르면 앱이 종료됩니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    backPressedTime = now
                }
            }
            else -> {
                if (!navController.popBackStack()) {
                    if (now - backPressedTime < 2000) {
                        (context as? ComponentActivity)?.finish()
                    } else {
                        Toast.makeText(
                            context,
                            "뒤로가기를 한 번 더 누르면 앱이 종료됩니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        backPressedTime = now
                    }
                }
            }
        }
    }
}