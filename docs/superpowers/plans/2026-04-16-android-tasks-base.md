# Android Tasks — Configuração Base

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Configurar a base do app Android (TasksFaculdade) com arquitetura MVVM + Repository, conectado ao microserviço Spring Boot local via Retrofit, com CRUD completo de tarefas em Jetpack Compose.

**Architecture:** MVVM + Repository Pattern. A UI observa `StateFlow<UiState>` do `TaskViewModel`. O ViewModel orquestra coroutines via `viewModelScope` e delega ao `TaskRepository`, que chama o `TaskApiService` (Retrofit). Sem cache local.

**Tech Stack:** Kotlin 2.2.10, Jetpack Compose (BOM 2026.02.01), Retrofit 2.11.0, OkHttp 4.12.0, Coroutines 1.10.1, Navigation Compose 2.9.0, Lifecycle 2.10.0, Material 3, AGP 9.1.1.

---

## Mapa de Arquivos

| Ação | Arquivo |
|------|---------|
| Modificar | `gradle/libs.versions.toml` |
| Modificar | `app/build.gradle.kts` |
| Modificar | `app/src/main/AndroidManifest.xml` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/data/model/Task.kt` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/data/remote/TaskApiService.kt` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/data/remote/RetrofitInstance.kt` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/data/remote/TaskRepository.kt` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/viewmodel/TaskUiState.kt` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/viewmodel/TaskViewModel.kt` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/components/TaskItem.kt` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/screens/TaskListScreen.kt` |
| Criar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/screens/TaskFormScreen.kt` |
| Modificar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/MainActivity.kt` |

> **Convenção:** `<pkg>` = `app/src/main/java/com/kotlincrossplatform/tasksfaculdade`

---

## Task 1: Inicializar Git e Configurar Dependências

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`

- [ ] **Step 1: Inicializar repositório git**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
git init
git add .
git commit -m "chore: initial commit - projeto gerado pelo Android Studio"
```

- [ ] **Step 2: Substituir o conteúdo de `gradle/libs.versions.toml`**

Substituir o arquivo completo:

```toml
[versions]
agp = "9.1.1"
coreKtx = "1.18.0"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
lifecycleRuntimeKtx = "2.10.0"
activityCompose = "1.13.0"
kotlin = "2.2.10"
composeBom = "2026.02.01"
retrofit = "2.11.0"
okhttp = "4.12.0"
coroutines = "1.10.1"
navigationCompose = "2.9.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
retrofit-core = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-converter-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
okhttp-bom = { group = "com.squareup.okhttp3", name = "okhttp-bom", version.ref = "okhttp" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

- [ ] **Step 3: Substituir o bloco `dependencies` em `app/build.gradle.kts`**

Substituir apenas o bloco `dependencies { ... }` pelo seguinte:

```kotlin
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

- [ ] **Step 4: Sincronizar o projeto**

No Android Studio: **File → Sync Project with Gradle Files**
Ou via terminal:
```bash
./gradlew dependencies --configuration releaseRuntimeClasspath | head -30
```
Esperado: resolução sem erros, linhas com `retrofit`, `okhttp`, `navigation-compose`.

- [ ] **Step 5: Verificar build**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add gradle/libs.versions.toml app/build.gradle.kts
git commit -m "chore: add retrofit, okhttp, coroutines, navigation, viewmodel-compose dependencies"
```

---

## Task 2: Configurar AndroidManifest

**Files:**
- Modify: `app/src/main/AndroidManifest.xml`

- [ ] **Step 1: Adicionar permissão INTERNET e flag de tráfego HTTP**

Substituir o conteúdo de `app/src/main/AndroidManifest.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.TasksFaculdade"
        android:usesCleartextTraffic="true">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.TasksFaculdade">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

> `usesCleartextTraffic="true"` é necessário para HTTP ao emulador (`http://10.0.2.2:8080`). Em produção, remover e usar HTTPS.

- [ ] **Step 2: Verificar build**

```bash
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/AndroidManifest.xml
git commit -m "chore: add INTERNET permission and usesCleartextTraffic for emulator HTTP"
```

---

## Task 3: Criar Data Class Task

**Files:**
- Create: `<pkg>/data/model/Task.kt`

- [ ] **Step 1: Criar o arquivo `Task.kt`**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.data.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id")          val id: Long? = null,
    @SerializedName("title")       val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("completed")   val completed: Boolean = false
)
```

> `id` é nullable porque ao criar uma tarefa (`POST /tasks`), o backend gera o ID.

- [ ] **Step 2: Verificar build**

```bash
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/data/model/Task.kt
git commit -m "feat: add Task data class with Gson SerializedName annotations"
```

---

## Task 4: Criar Camada Remota (API + Retrofit + Repository)

**Files:**
- Create: `<pkg>/data/remote/TaskApiService.kt`
- Create: `<pkg>/data/remote/RetrofitInstance.kt`
- Create: `<pkg>/data/remote/TaskRepository.kt`

- [ ] **Step 1: Criar `TaskApiService.kt`**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.data.remote

import com.kotlincrossplatform.tasksfaculdade.data.model.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApiService {

    @GET("tasks")
    suspend fun getAllTasks(): List<Task>

    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: Long): Task

    @POST("tasks")
    suspend fun createTask(@Body task: Task): Task

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body task: Task): Task

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long)
}
```

- [ ] **Step 2: Criar `RetrofitInstance.kt`**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: TaskApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApiService::class.java)
    }
}
```

- [ ] **Step 3: Criar `TaskRepository.kt`**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.data.remote

import com.kotlincrossplatform.tasksfaculdade.data.model.Task

class TaskRepository {

    private val api = RetrofitInstance.api

    suspend fun getAllTasks(): List<Task> = api.getAllTasks()

    suspend fun getTaskById(id: Long): Task = api.getTaskById(id)

    suspend fun createTask(task: Task): Task = api.createTask(task)

    suspend fun updateTask(id: Long, task: Task): Task = api.updateTask(id, task)

    suspend fun deleteTask(id: Long) = api.deleteTask(id)
}
```

- [ ] **Step 4: Verificar build**

```bash
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/data/
git commit -m "feat: add data layer - TaskApiService, RetrofitInstance, TaskRepository"
```

---

## Task 5: Criar Camada ViewModel

**Files:**
- Create: `<pkg>/viewmodel/TaskUiState.kt`
- Create: `<pkg>/viewmodel/TaskViewModel.kt`

- [ ] **Step 1: Criar `TaskUiState.kt`**

```kotlin
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
```

- [ ] **Step 2: Criar `TaskViewModel.kt`**

```kotlin
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
```

- [ ] **Step 3: Verificar build**

```bash
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/viewmodel/
git commit -m "feat: add ViewModel layer - TaskUiState sealed classes and TaskViewModel"
```

---

## Task 6: Criar Componente TaskItem

**Files:**
- Create: `<pkg>/ui/components/TaskItem.kt`

- [ ] **Step 1: Criar `TaskItem.kt`**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kotlincrossplatform.tasksfaculdade.data.model.Task

@Composable
fun TaskItem(
    task: Task,
    onToggleCompleted: (Task) -> Unit,
    onEditClick: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Excluir tarefa") },
            text = { Text("Deseja excluir \"${task.title}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDeleteClick(task)
                }) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick(task) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggleCompleted(task) }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir tarefa",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
```

- [ ] **Step 2: Verificar build**

```bash
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/components/TaskItem.kt
git commit -m "feat: add TaskItem composable component with delete confirmation dialog"
```

---

## Task 7: Criar TaskListScreen

**Files:**
- Create: `<pkg>/ui/screens/TaskListScreen.kt`

- [ ] **Step 1: Criar `TaskListScreen.kt`**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlincrossplatform.tasksfaculdade.ui.components.TaskItem
import com.kotlincrossplatform.tasksfaculdade.viewmodel.TaskListUiState
import com.kotlincrossplatform.tasksfaculdade.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    val uiState by viewModel.listUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Minhas Tarefas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreate) {
                Icon(Icons.Default.Add, contentDescription = "Nova tarefa")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is TaskListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is TaskListUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadTasks() }) {
                            Text("Tentar novamente")
                        }
                    }
                }
                is TaskListUiState.Success -> {
                    if (state.tasks.isEmpty()) {
                        Text(
                            text = "Nenhuma tarefa encontrada.\nToque em + para criar.",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.tasks, key = { it.id!! }) { task ->
                                TaskItem(
                                    task = task,
                                    onToggleCompleted = { viewModel.toggleCompleted(it) },
                                    onEditClick = { onNavigateToEdit(it.id!!) },
                                    onDeleteClick = { viewModel.deleteTask(it.id!!) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
```

- [ ] **Step 2: Verificar build**

```bash
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/screens/TaskListScreen.kt
git commit -m "feat: add TaskListScreen with loading/error/empty states and FAB"
```

---

## Task 8: Criar TaskFormScreen

**Files:**
- Create: `<pkg>/ui/screens/TaskFormScreen.kt`

- [ ] **Step 1: Criar `TaskFormScreen.kt`**

```kotlin
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

    // Reseta o estado ao entrar na tela (evita Saved de uma operação anterior)
    DisposableEffect(Unit) {
        viewModel.resetFormState()
        onDispose { }
    }

    // Carrega a tarefa no modo edição
    LaunchedEffect(taskId) {
        if (taskId != null) viewModel.loadTaskForEdit(taskId)
    }

    // Preenche os campos quando a tarefa é carregada
    LaunchedEffect(selectedTask) {
        selectedTask?.let {
            title = it.title
            description = it.description
        }
    }

    // Navega de volta ao salvar com sucesso
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
                }
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
```

- [ ] **Step 2: Verificar build**

```bash
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/screens/TaskFormScreen.kt
git commit -m "feat: add TaskFormScreen supporting create and edit modes"
```

---

## Task 9: Atualizar MainActivity com NavHost

**Files:**
- Modify: `<pkg>/MainActivity.kt`

- [ ] **Step 1: Substituir o conteúdo de `MainActivity.kt`**

```kotlin
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
```

- [ ] **Step 2: Build final completo**

```bash
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL` — APK gerado em `app/build/outputs/apk/debug/app-debug.apk`

- [ ] **Step 3: Commit final**

```bash
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/MainActivity.kt
git commit -m "feat: wire up NavHost with TaskListScreen and TaskFormScreen"
```

---

## Verificação Manual no Emulador

Após instalar o APK no emulador Android (com o backend Spring Boot rodando):

1. **Backend rodando:** `mvn spring-boot:run` no projeto `ProjetoJavaSpringFaculdade`
2. **Instalar APK:** `./gradlew installDebug`
3. **Fluxo de teste:**
   - [ ] Abrir o app → tela de lista carrega (ou estado vazio se sem tarefas)
   - [ ] Tocar no FAB (+) → abre tela de criar tarefa
   - [ ] Criar tarefa sem título → exibe erro inline "O título é obrigatório"
   - [ ] Criar tarefa com título → volta para a lista com a nova tarefa
   - [ ] Tocar no card da tarefa → abre tela de edição com campos preenchidos
   - [ ] Editar e salvar → lista atualizada
   - [ ] Tocar no ícone de lixeira → dialog de confirmação aparece
   - [ ] Confirmar exclusão → tarefa removida da lista
   - [ ] Tocar no checkbox → tarefa marcada/desmarcada (título riscado)
   - [ ] Desligar o backend → lista exibe mensagem de erro + botão "Tentar novamente"
