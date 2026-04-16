package com.kotlincrossplatform.tasksfaculdade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kotlincrossplatform.tasksfaculdade.ui.screens.TaskFormScreen
import com.kotlincrossplatform.tasksfaculdade.ui.screens.TaskListScreen
import com.kotlincrossplatform.tasksfaculdade.ui.theme.TasksFaculdadeTheme
import com.kotlincrossplatform.tasksfaculdade.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TasksFaculdadeTheme {
                val navController = rememberNavController()
                val taskViewModel: TaskViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "task_list"
                ) {
                    composable("task_list") {
                        TaskListScreen(
                            viewModel = taskViewModel,
                            onNavigateToCreate = { navController.navigate("task_form") },
                            onNavigateToEdit = { id -> navController.navigate("task_form/$id") }
                        )
                    }
                    composable("task_form") {
                        TaskFormScreen(
                            viewModel = taskViewModel,
                            taskId = null,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("task_form/{taskId}") { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
                        TaskFormScreen(
                            viewModel = taskViewModel,
                            taskId = taskId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
