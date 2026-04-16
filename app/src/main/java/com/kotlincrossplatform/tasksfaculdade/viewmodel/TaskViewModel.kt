package com.kotlincrossplatform.tasksfaculdade.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlincrossplatform.tasksfaculdade.data.model.Task
import com.kotlincrossplatform.tasksfaculdade.data.remote.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository = TaskRepository()

    private val _listUiState = MutableStateFlow<TaskListUiState>(TaskListUiState.Loading)
    val listUiState: StateFlow<TaskListUiState> = _listUiState.asStateFlow()

    private val _formUiState = MutableStateFlow<TaskFormUiState>(TaskFormUiState.Idle)
    val formUiState: StateFlow<TaskFormUiState> = _formUiState.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _listUiState.value = TaskListUiState.Loading
            try {
                _listUiState.value = TaskListUiState.Success(repository.getAllTasks())
            } catch (e: Exception) {
                _listUiState.value = TaskListUiState.Error(e.message ?: "Erro ao carregar tarefas")
            }
        }
    }

    fun loadTaskForEdit(id: Long) {
        viewModelScope.launch {
            _formUiState.value = TaskFormUiState.Loading
            try {
                _selectedTask.value = repository.getTaskById(id)
                _formUiState.value = TaskFormUiState.Idle
            } catch (e: Exception) {
                _formUiState.value = TaskFormUiState.Error(e.message ?: "Erro ao carregar tarefa")
            }
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            _formUiState.value = TaskFormUiState.Loading
            try {
                repository.createTask(task)
                _formUiState.value = TaskFormUiState.Saved
                loadTasks()
            } catch (e: Exception) {
                _formUiState.value = TaskFormUiState.Error(e.message ?: "Erro ao criar tarefa")
            }
        }
    }

    fun updateTask(id: Long, task: Task) {
        viewModelScope.launch {
            _formUiState.value = TaskFormUiState.Loading
            try {
                repository.updateTask(id, task)
                _formUiState.value = TaskFormUiState.Saved
                loadTasks()
            } catch (e: Exception) {
                _formUiState.value = TaskFormUiState.Error(e.message ?: "Erro ao atualizar tarefa")
            }
        }
    }

    fun toggleCompleted(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task.id!!, task.copy(completed = !task.completed))
                loadTasks()
            } catch (e: Exception) {
                _listUiState.value = TaskListUiState.Error(e.message ?: "Erro ao atualizar tarefa")
            }
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteTask(id)
                loadTasks()
            } catch (e: Exception) {
                _listUiState.value = TaskListUiState.Error(e.message ?: "Erro ao excluir tarefa")
            }
        }
    }

    fun resetFormState() {
        _formUiState.value = TaskFormUiState.Idle
        _selectedTask.value = null
    }
}
