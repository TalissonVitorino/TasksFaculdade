# Deep Ocean UI Redesign — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Substituir o tema padrão do Android Studio por uma identidade visual "Deep Ocean" (teal + navy + cyan, Nunito, cantos arredondados, light + dark mode) sem alterar nenhuma lógica de negócio.

**Architecture:** Todas as mudanças ficam na camada `ui/theme/` e nos arquivos de UI. O `MaterialTheme` distribui paleta, tipografia e shapes automaticamente para todos os composables que usam `MaterialTheme.colorScheme`, `MaterialTheme.typography` e `MaterialTheme.shapes`. Dynamic color é desativado para garantir a paleta customizada em Android 12+.

**Tech Stack:** Jetpack Compose, Material3, `androidx.compose.ui:ui-text-google-fonts` (Nunito via Google Fonts).

---

## Mapa de Arquivos

| Ação | Arquivo |
|------|---------|
| Modificar | `gradle/libs.versions.toml` |
| Modificar | `app/build.gradle.kts` |
| Substituir | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/theme/Color.kt` |
| Substituir | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/theme/Type.kt` |
| Substituir | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/theme/Theme.kt` |
| Modificar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/components/TaskItem.kt` |
| Modificar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/screens/TaskListScreen.kt` |
| Modificar | `app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/screens/TaskFormScreen.kt` |

> **Convenção:** `<pkg>` = `app/src/main/java/com/kotlincrossplatform/tasksfaculdade`

---

## Task 1: Adicionar Dependência Google Fonts

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`

- [ ] **Step 1: Adicionar entrada em `gradle/libs.versions.toml`**

Adicionar a linha abaixo logo após a última entrada de `[libraries]`, antes de `[plugins]`:

```toml
androidx-compose-ui-text-google-fonts = { group = "androidx.compose.ui", name = "ui-text-google-fonts" }
```

> Sem `version.ref` — versão gerenciada pelo Compose BOM já declarado no projeto.

- [ ] **Step 2: Adicionar `implementation()` em `app/build.gradle.kts`**

Adicionar a linha abaixo, junto às outras dependências Compose (após `implementation(libs.androidx.navigation.compose)`):

```kotlin
implementation(libs.androidx.compose.ui.text.google.fonts)
```

- [ ] **Step 3: Verificar build**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 4: Commit**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
git add gradle/libs.versions.toml app/build.gradle.kts
git commit -m "chore: add ui-text-google-fonts dependency for Nunito font"
```

---

## Task 2: Substituir Color.kt — Paleta Deep Ocean

**Files:**
- Substituir: `<pkg>/ui/theme/Color.kt`

- [ ] **Step 1: Substituir o arquivo completo**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.ui.theme

import androidx.compose.ui.graphics.Color

// ── Light Theme ──────────────────────────────────────────────────────────────
val LightBackground            = Color(0xFFF0F7F7)
val LightOnBackground          = Color(0xFF1A2E2E)
val LightSurface               = Color(0xFFFFFFFF)
val LightOnSurface             = Color(0xFF1A2E2E)
val LightSurfaceVariant        = Color(0xFFE0F2F1)
val LightOnSurfaceVariant      = Color(0xFF37474F)
val LightPrimary               = Color(0xFF00897B)
val LightOnPrimary             = Color(0xFFFFFFFF)
val LightPrimaryContainer      = Color(0xFFB2DFDB)
val LightOnPrimaryContainer    = Color(0xFF004D40)
val LightSecondary             = Color(0xFF0097A7)
val LightOnSecondary           = Color(0xFFFFFFFF)
val LightSecondaryContainer    = Color(0xFFB2EBF2)
val LightOnSecondaryContainer  = Color(0xFF006064)
val LightTertiary              = Color(0xFF00838F)
val LightOnTertiary            = Color(0xFFFFFFFF)
val LightTertiaryContainer     = Color(0xFFE0F7FA)
val LightOnTertiaryContainer   = Color(0xFF006064)
val LightError                 = Color(0xFFD32F2F)
val LightOnError               = Color(0xFFFFFFFF)
val LightErrorContainer        = Color(0xFFFFCDD2)
val LightOnErrorContainer      = Color(0xFFB71C1C)
val LightOutline               = Color(0xFF80CBC4)
val LightOutlineVariant        = Color(0xFFB2DFDB)
val LightScrim                 = Color(0xFF000000)

// ── Dark Theme ────────────────────────────────────────────────────────────────
val DarkBackground             = Color(0xFF0D1B2A)
val DarkOnBackground           = Color(0xFFE0F2F1)
val DarkSurface                = Color(0xFF102232)
val DarkOnSurface              = Color(0xFFE0F2F1)
val DarkSurfaceVariant         = Color(0xFF1A3044)
val DarkOnSurfaceVariant       = Color(0xFF80CBC4)
val DarkPrimary                = Color(0xFF80CBC4)
val DarkOnPrimary              = Color(0xFF003733)
val DarkPrimaryContainer       = Color(0xFF00574F)
val DarkOnPrimaryContainer     = Color(0xFFB2DFDB)
val DarkSecondary              = Color(0xFF80DEEA)
val DarkOnSecondary            = Color(0xFF003E47)
val DarkSecondaryContainer     = Color(0xFF00626E)
val DarkOnSecondaryContainer   = Color(0xFFB2EBF2)
val DarkTertiary               = Color(0xFF00E5FF)
val DarkOnTertiary             = Color(0xFF003640)
val DarkTertiaryContainer      = Color(0xFF004E5A)
val DarkOnTertiaryContainer    = Color(0xFFB2EBF2)
val DarkError                  = Color(0xFFEF9A9A)
val DarkOnError                = Color(0xFF6F0000)
val DarkErrorContainer         = Color(0xFFB71C1C)
val DarkOnErrorContainer       = Color(0xFFFFCDD2)
val DarkOutline                = Color(0xFF4DB6AC)
val DarkOutlineVariant         = Color(0xFF1A3044)
val DarkScrim                  = Color(0xFF000000)
```

- [ ] **Step 2: Verificar build**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/theme/Color.kt
git commit -m "feat(ui): replace color palette with Deep Ocean theme"
```

---

## Task 3: Substituir Type.kt — Tipografia Nunito

**Files:**
- Substituir: `<pkg>/ui/theme/Type.kt`

- [ ] **Step 1: Substituir o arquivo completo**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.kotlincrossplatform.tasksfaculdade.R

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs,
)

private val nunitoFont = GoogleFont("Nunito")

val NunitoFontFamily = FontFamily(
    Font(googleFont = nunitoFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = nunitoFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = nunitoFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = nunitoFont, fontProvider = fontProvider, weight = FontWeight.Bold),
)

val Typography = Typography(
    displayLarge   = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold,     fontSize = 57.sp, lineHeight = 64.sp,  letterSpacing = (-0.25).sp),
    displayMedium  = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold,     fontSize = 45.sp, lineHeight = 52.sp,  letterSpacing = 0.sp),
    displaySmall   = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold,     fontSize = 36.sp, lineHeight = 44.sp,  letterSpacing = 0.sp),
    headlineLarge  = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold,     fontSize = 32.sp, lineHeight = 40.sp,  letterSpacing = 0.sp),
    headlineMedium = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold,     fontSize = 28.sp, lineHeight = 36.sp,  letterSpacing = 0.sp),
    headlineSmall  = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold,     fontSize = 24.sp, lineHeight = 32.sp,  letterSpacing = 0.sp),
    titleLarge     = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp,  letterSpacing = 0.sp),
    titleMedium    = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp,  letterSpacing = 0.15.sp),
    titleSmall     = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp,  letterSpacing = 0.1.sp),
    bodyLarge      = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 24.sp,  letterSpacing = 0.5.sp),
    bodyMedium     = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 20.sp,  letterSpacing = 0.25.sp),
    bodySmall      = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 16.sp,  letterSpacing = 0.4.sp),
    labelLarge     = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp,  letterSpacing = 0.1.sp),
    labelMedium    = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium,   fontSize = 12.sp, lineHeight = 16.sp,  letterSpacing = 0.5.sp),
    labelSmall     = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium,   fontSize = 11.sp, lineHeight = 16.sp,  letterSpacing = 0.5.sp),
)
```

> `R.array.com_google_android_gms_fonts_certs` é fornecido pela própria biblioteca `ui-text-google-fonts` — não é necessário adicionar nenhum arquivo XML ao projeto.

- [ ] **Step 2: Verificar build**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/theme/Type.kt
git commit -m "feat(ui): replace typography with Nunito font family"
```

---

## Task 4: Substituir Theme.kt — Shapes, Esquemas de Cor, Sem Dynamic Color

**Files:**
- Substituir: `<pkg>/ui/theme/Theme.kt`

- [ ] **Step 1: Substituir o arquivo completo**

```kotlin
package com.kotlincrossplatform.tasksfaculdade.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    background            = LightBackground,
    onBackground          = LightOnBackground,
    surface               = LightSurface,
    onSurface             = LightOnSurface,
    surfaceVariant        = LightSurfaceVariant,
    onSurfaceVariant      = LightOnSurfaceVariant,
    primary               = LightPrimary,
    onPrimary             = LightOnPrimary,
    primaryContainer      = LightPrimaryContainer,
    onPrimaryContainer    = LightOnPrimaryContainer,
    secondary             = LightSecondary,
    onSecondary           = LightOnSecondary,
    secondaryContainer    = LightSecondaryContainer,
    onSecondaryContainer  = LightOnSecondaryContainer,
    tertiary              = LightTertiary,
    onTertiary            = LightOnTertiary,
    tertiaryContainer     = LightTertiaryContainer,
    onTertiaryContainer   = LightOnTertiaryContainer,
    error                 = LightError,
    onError               = LightOnError,
    errorContainer        = LightErrorContainer,
    onErrorContainer      = LightOnErrorContainer,
    outline               = LightOutline,
    outlineVariant        = LightOutlineVariant,
    scrim                 = LightScrim,
)

private val DarkColorScheme = darkColorScheme(
    background            = DarkBackground,
    onBackground          = DarkOnBackground,
    surface               = DarkSurface,
    onSurface             = DarkOnSurface,
    surfaceVariant        = DarkSurfaceVariant,
    onSurfaceVariant      = DarkOnSurfaceVariant,
    primary               = DarkPrimary,
    onPrimary             = DarkOnPrimary,
    primaryContainer      = DarkPrimaryContainer,
    onPrimaryContainer    = DarkOnPrimaryContainer,
    secondary             = DarkSecondary,
    onSecondary           = DarkOnSecondary,
    secondaryContainer    = DarkSecondaryContainer,
    onSecondaryContainer  = DarkOnSecondaryContainer,
    tertiary              = DarkTertiary,
    onTertiary            = DarkOnTertiary,
    tertiaryContainer     = DarkTertiaryContainer,
    onTertiaryContainer   = DarkOnTertiaryContainer,
    error                 = DarkError,
    onError               = DarkOnError,
    errorContainer        = DarkErrorContainer,
    onErrorContainer      = DarkOnErrorContainer,
    outline               = DarkOutline,
    outlineVariant        = DarkOutlineVariant,
    scrim                 = DarkScrim,
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(10.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun TasksFaculdadeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        shapes      = AppShapes,
        typography  = Typography,
        content     = content,
    )
}
```

- [ ] **Step 2: Verificar build**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/theme/Theme.kt
git commit -m "feat(ui): apply Deep Ocean color schemes, custom shapes, disable dynamic color"
```

---

## Task 5: Atualizar TaskItem.kt — Cores do Card e Efeito de Concluído

**Files:**
- Modificar: `<pkg>/ui/components/TaskItem.kt`

- [ ] **Step 1: Substituir o arquivo completo**

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
import androidx.compose.ui.draw.alpha
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
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                    .alpha(if (task.completed) 0.5f else 1f)
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
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/components/TaskItem.kt
git commit -m "feat(ui): apply Deep Ocean card colors and completed task alpha effect"
```

---

## Task 6: Atualizar TopAppBar nas Telas

**Files:**
- Modificar: `<pkg>/ui/screens/TaskListScreen.kt`
- Modificar: `<pkg>/ui/screens/TaskFormScreen.kt`

- [ ] **Step 1: Substituir `TaskListScreen.kt` completo**

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
import androidx.compose.material3.TopAppBarDefaults
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
            TopAppBar(
                title = { Text("Minhas Tarefas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor        = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor     = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary,
            ) {
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

- [ ] **Step 2: Substituir `TaskFormScreen.kt` completo**

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
```

- [ ] **Step 3: Verificar build final**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
./gradlew assembleDebug
```
Esperado: `BUILD SUCCESSFUL` — APK gerado com o novo visual

- [ ] **Step 4: Commit final**

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade
git add app/src/main/java/com/kotlincrossplatform/tasksfaculdade/ui/screens/
git commit -m "feat(ui): apply Deep Ocean TopAppBar colors and FAB styling to all screens"
```

---

## Verificação Visual no Emulador

Após `./gradlew installDebug`:

- [ ] **Light mode:** fundo off-white azulado, TopAppBar teal suave, cards com fundo `surfaceVariant` (E0F2F1), FAB teal vibrante
- [ ] **Dark mode** (mudar nas configurações do emulador): fundo navy profundo `#0D1B2A`, cards `#1A3044`, topbar teal escuro
- [ ] **Tarefa concluída:** título com strikethrough + texto esmaecido (alpha 0.5)
- [ ] **Fonte Nunito:** letras com letterforms arredondadas visíveis nos títulos
- [ ] **Cantos arredondados:** cards com `16dp`, FAB com `28dp`, botões com `10dp`
