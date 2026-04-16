package com.kotlincrossplatform.tasksfaculdade.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlincrossplatform.tasksfaculdade.data.model.Task
import com.kotlincrossplatform.tasksfaculdade.viewmodel.TaskFormUiState
import com.kotlincrossplatform.tasksfaculdade.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    viewModel: TaskViewModel,
    taskId: Long?,
    onNavigateBack: () -> Unit
) {
    val formUiState by viewModel.formUiState.collectAsStateWithLifecycle()
    val selectedTask by viewModel.selectedTask.collectAsStateWithLifecycle()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }

    val isEditMode = taskId != null
    val isLoaded = !isEditMode || selectedTask != null

    DisposableEffect(Unit) {
        viewModel.resetFormState()
        onDispose { }
    }

    LaunchedEffect(taskId) {
        if (taskId != null) viewModel.loadTaskForEdit(taskId)
    }

    LaunchedEffect(selectedTask) {
        selectedTask?.let {
            title = it.title
            description = it.description
        }
    }

    LaunchedEffect(formUiState) {
        if (formUiState is TaskFormUiState.Saved) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Tarefa" else "Nova Tarefa") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor              = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor           = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor  = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it; titleError = false },
                label = { Text("Título *") },
                isError = titleError,
                supportingText = if (titleError) ({ Text("O título é obrigatório") }) else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            if (formUiState is TaskFormUiState.Error) {
                Text(
                    text = (formUiState as TaskFormUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    if (title.isBlank()) { titleError = true; return@Button }
                    val task = Task(title = title.trim(), description = description.trim())
                    if (isEditMode) viewModel.updateTask(taskId!!, task)
                    else viewModel.createTask(task)
                },
                enabled = formUiState !is TaskFormUiState.Loading && isLoaded,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (formUiState is TaskFormUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text(if (isEditMode) "Atualizar" else "Salvar")
                }
            }
        }
    }
}
