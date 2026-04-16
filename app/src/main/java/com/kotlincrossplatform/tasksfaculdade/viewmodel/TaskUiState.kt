package com.kotlincrossplatform.tasksfaculdade.viewmodel

import com.kotlincrossplatform.tasksfaculdade.data.model.Task

sealed class TaskListUiState {
    object Loading : TaskListUiState()
    data class Success(val tasks: List<Task>) : TaskListUiState()
    data class Error(val message: String) : TaskListUiState()
}

sealed class TaskFormUiState {
    object Idle : TaskFormUiState()
    object Loading : TaskFormUiState()
    object Saved : TaskFormUiState()
    data class Error(val message: String) : TaskFormUiState()
}
