# Design: App Android de Gerenciamento de Tarefas

**Data:** 2026-04-16
**Projeto:** TasksFaculdade
**Tecnologias:** Kotlin, Jetpack Compose, MVVM, StateFlow, Retrofit

---

## 1. Contexto

App Android cliente para o microserviço Spring Boot de gerenciamento de tarefas (`ProjetoJavaSpringFaculdade`). O backend expõe uma API REST com CRUD completo da entidade `Task` (id, title, description, completed) na porta 8080. O app se conecta ao emulador via `http://10.0.2.2:8080`.

**Projeto Android:**
- Caminho: `C:\Users\talis\AndroidStudioProjects\TasksFaculdade`
- Package: `com.kotlincrossplatform.tasksfaculdade`
- minSdk: 35 / targetSdk: 36
- AGP: 9.1.1 / Kotlin: 2.2.10 / Compose BOM: 2026.02.01

**Backend:**
- Base URL (emulador): `http://10.0.2.2:8080/`
- Endpoints: `GET /tasks`, `GET /tasks/{id}`, `POST /tasks`, `PUT /tasks/{id}`, `DELETE /tasks/{id}`
- Sem autenticação

---

## 2. Arquitetura

**Padrão:** MVVM + Repository Pattern (arquitetura oficial do Google)

**Fluxo de dados (unidirecional):**
```
UI (Compose) → ViewModel.action() → Repository.suspend() → TaskApiService (Retrofit)
                    ↑                                                     ↓
             StateFlow<UiState> ←────────────────── Response<Task>
```

**Camadas:**

| Camada | Responsabilidade |
|--------|-----------------|
| `ui/screens` | Composables que observam StateFlow e disparam eventos |
| `viewmodel` | Lógica de apresentação, expõe StateFlow, orquestra coroutines |
| `data/remote` | Repository, interface Retrofit, singleton de configuração |
| `data/model` | Data classes Kotlin (mapeamento do JSON do backend) |

**Decisões:**
- Sem cache local (Room) — app assume conectividade sempre disponível
- Sem injeção de dependência (Hilt/Koin) — escopo acadêmico, instância manual no ViewModel
- Sem camada de domínio separada — Repository serve diretamente o ViewModel

---

## 3. Estrutura de Pacotes

```
com.kotlincrossplatform.tasksfaculdade/
├── data/
│   ├── model/
│   │   └── Task.kt                  ← data class com @SerializedName
│   └── remote/
│       ├── TaskApiService.kt        ← interface Retrofit com suspend funs
│       ├── RetrofitInstance.kt      ← singleton OkHttp + Retrofit
│       └── TaskRepository.kt        ← abstração da fonte de dados
├── ui/
│   ├── screens/
│   │   ├── TaskListScreen.kt        ← lista com FAB, delete, toggle completed
│   │   └── TaskFormScreen.kt        ← criar e editar (modo determinado por taskId)
│   ├── components/
│   │   └── TaskItem.kt              ← card reutilizável por tarefa
│   └── theme/                       ← gerado pelo Android Studio, não modificar
├── viewmodel/
│   ├── TaskUiState.kt               ← sealed classes de estados por tela
│   └── TaskViewModel.kt             ← ViewModel único com viewModelScope
└── MainActivity.kt                  ← NavHost, ponto de entrada da navegação
```

---

## 4. Dependências

Adicionadas ao `gradle/libs.versions.toml` e `app/build.gradle.kts`:

| Biblioteca | Versão | Finalidade |
|---|---|---|
| `retrofit` | 2.11.0 | Cliente HTTP tipado |
| `converter-gson` | 2.11.0 | Deserialização JSON → data class |
| `okhttp-bom` | 4.12.0 | Gerenciamento de versão OkHttp |
| `logging-interceptor` | (via BOM) | Logs de requisições no Logcat |
| `kotlinx-coroutines-android` | 1.10.1 | Coroutines na thread principal |
| `lifecycle-viewmodel-compose` | 2.10.0 (reutiliza versão já declarada) | `viewModel()` em Composables |
| `navigation-compose` | 2.9.0 | Navegação declarativa entre telas |

> Material 3 e Lifecycle Runtime já estão no projeto. `lifecycle-viewmodel-compose` precisa ser adicionado explicitamente ao version catalog mesmo reutilizando a versão existente.

---

## 5. Modelos de Dados

### Task.kt
```kotlin
data class Task(
    @SerializedName("id")          val id: Long? = null,
    @SerializedName("title")       val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("completed")   val completed: Boolean = false
)
```

### TaskUiState.kt
```kotlin
// Estado da tela de lista
sealed class TaskListUiState {
    object Loading : TaskListUiState()
    data class Success(val tasks: List<Task>) : TaskListUiState()
    data class Error(val message: String) : TaskListUiState()
}

// Estado da tela de formulário
sealed class TaskFormUiState {
    object Idle : TaskFormUiState()
    object Loading : TaskFormUiState()
    object Saved : TaskFormUiState()           // dispara popBackStack na UI
    data class Error(val message: String) : TaskFormUiState()
}
```

---

## 6. Telas e Navegação

### Rotas
```kotlin
sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object TaskCreate : Screen("task_form")
    object TaskEdit : Screen("task_form/{taskId}")
}
```

### TaskListScreen
- `LazyColumn` com `TaskItem` por tarefa
- FAB (+) → navega para `TaskFormScreen` (modo criar)
- Toque no item → navega para `TaskFormScreen` (modo editar, passa `taskId`)
- Checkbox no card → `PUT /tasks/{id}` com `completed` invertido
- Botão deletar no card → `DELETE /tasks/{id}` com confirmação via `AlertDialog`
- Estados visuais:
  - `Loading`: `CircularProgressIndicator` centralizado
  - `Error`: mensagem + botão "Tentar novamente"
  - Lista vazia: texto informativo centralizado

### TaskFormScreen
- Recebe `taskId: Long?` (null = modo criar, não-null = modo editar)
- Modo **criar**: campos vazios, chama `POST /tasks`
- Modo **editar**: busca `GET /tasks/{id}` ao abrir, pré-preenche campos, chama `PUT /tasks/{id}`
- Campos: `title` (TextField, obrigatório) e `description` (TextField, opcional, multilinha)
- Validação inline: título vazio exibe erro abaixo do campo
- Estado `Saved` → `navController.popBackStack()` automaticamente
- `TopAppBar` com botão Voltar (seta)

### Fluxo
```
TaskListScreen
    ├── FAB (+) ──────────────────────→ TaskFormScreen (criar)
    ├── toque no card ────────────────→ TaskFormScreen (editar, taskId)
    └── checkbox / delete ────────────→ chamadas diretas no ViewModel
                                                │
                          TaskFormScreen ───────┘
                               └── salvar → popBackStack → TaskListScreen
```

---

## 7. AndroidManifest

Permissões e configuração necessárias:
```xml
<uses-permission android:name="android.permission.INTERNET" />

<application
    android:usesCleartextTraffic="true"
    ...>
```

> `usesCleartextTraffic="true"` é necessário porque o emulador usa HTTP (não HTTPS). Em produção, isso deve ser removido e o backend deve usar HTTPS.

---

## 8. Tratamento de Erros

- Erros de rede (timeout, sem conexão) → estado `Error` com mensagem amigável
- HTTP 404 (tarefa não encontrada) → mensagem específica na UI
- HTTP 4xx/5xx genérico → mensagem genérica + opção de retry
- Erros propagados via `try/catch` no ViewModel, nunca silenciados

---

## 9. Fora de Escopo

- Autenticação / autorização
- Cache offline (Room)
- Injeção de dependência (Hilt/Koin)
- Testes automatizados
- Paginação da lista
- Busca/filtro de tarefas
