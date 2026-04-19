# TasksFaculdade — App Android de Gerenciamento de Tarefas

App Android desenvolvido em **Kotlin + Jetpack Compose** para gerenciar tarefas via API REST. Consome um backend **Spring Boot** (microserviço separado), usa arquitetura **MVVM com StateFlow** e tem interface visual moderna com o tema **Deep Ocean**.

---

## Índice

1. [Visão Geral](#1-visão-geral)
2. [Stack Tecnológica](#2-stack-tecnológica)
3. [Pré-requisitos](#3-pré-requisitos)
4. [Como rodar o projeto](#4-como-rodar-o-projeto)
5. [Estrutura de Pacotes](#5-estrutura-de-pacotes)
6. [Arquitetura — MVVM](#6-arquitetura--mvvm)
7. [Camada de Dados](#7-camada-de-dados)
8. [Camada de ViewModel](#8-camada-de-viewmodel)
9. [Camada de UI](#9-camada-de-ui)
10. [Tema Deep Ocean](#10-tema-deep-ocean)
11. [Navegação](#11-navegação)
12. [Contrato da API](#12-contrato-da-api)
13. [Histórico de Commits](#13-histórico-de-commits)
14. [Decisões de Design](#14-decisões-de-design)

---

## 1. Visão Geral

O app exibe uma lista de tarefas buscadas de um servidor local, permite criar novas tarefas, editar existentes, marcar como concluídas e excluir. Toda a persistência é feita no backend — o app é puramente um cliente REST.

**Funcionalidades:**

| Funcionalidade | Descrição |
|---|---|
| Listar tarefas | Exibe todas as tarefas com título, descrição e status |
| Criar tarefa | Formulário com título (obrigatório) e descrição |
| Editar tarefa | Pré-preenche o formulário com dados existentes |
| Marcar como concluída | Checkbox alterna o status via PUT na API |
| Excluir tarefa | Botão de lixeira com diálogo de confirmação |
| Loading/Error | Indicadores visuais para cada estado da requisição |
| Light + Dark mode | Tema automático baseado na preferência do sistema |

---

## 2. Stack Tecnológica

| Componente | Versão | Função |
|---|---|---|
| Kotlin | 2.2.10 | Linguagem principal |
| Android Gradle Plugin | 9.1.1 | Build system |
| minSdk / targetSdk | 35 / 36 | Compatibilidade Android |
| Jetpack Compose BOM | 2026.02.01 | Toolkit de UI declarativo |
| Material3 | (BOM) | Sistema de design |
| Navigation Compose | 2.9.0 | Navegação entre telas |
| Lifecycle / ViewModel | 2.10.0 | MVVM + StateFlow |
| Retrofit | 2.11.0 | Cliente HTTP |
| OkHttp | 4.12.0 | Camada HTTP + logging |
| Kotlin Coroutines | 1.10.1 | Chamadas assíncronas |
| Gson | (Retrofit) | Serialização JSON |
| ui-text-google-fonts | (BOM) | Fonte Nunito via Google Fonts |

---

## 3. Pré-requisitos

- **Android Studio** Hedgehog ou mais recente
- **JDK 11+**
- **Backend Spring Boot** rodando localmente na porta `8080`
  - O emulador Android acessa o host via `10.0.2.2` (equivale ao `localhost` da máquina)
  - O projeto do backend está em `C:\Users\talis\IdeaProjects\ProjetoJavaSpringFaculdade`
- **Emulador Android** com API 35+ (Pixel 6 ou equivalente recomendado)

---

## 4. Como rodar o projeto

### 4.1 Subir o backend

```bash
# Na pasta do projeto Spring Boot
cd C:/Users/talis/IdeaProjects/ProjetoJavaSpringFaculdade

# Com Maven Wrapper
./mvnw spring-boot:run

# Ou com Gradle Wrapper (se aplicável)
./gradlew bootRun
```

Confirme que a API responde antes de iniciar o app:

```
GET http://localhost:8080/tasks
→ 200 OK  [ ]
```

### 4.2 Abrir o projeto Android

1. Abra o **Android Studio**
2. `File > Open` → selecione `C:\Users\talis\AndroidStudioProjects\TasksFaculdade`
3. Aguarde o Gradle sync finalizar
4. Inicie o emulador (API 35+)
5. Clique em **Run 'app'** (▶)

### 4.3 Build via linha de comando

```bash
cd C:/Users/talis/AndroidStudioProjects/TasksFaculdade

# Compilar APK de debug
./gradlew assembleDebug

# Instalar direto no emulador conectado
./gradlew installDebug
```

O APK gerado fica em:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 5. Estrutura de Pacotes

```
app/src/main/java/com/kotlincrossplatform/tasksfaculdade/
│
├── MainActivity.kt                  # Entry point — NavHost e tema
│
├── data/
│   ├── model/
│   │   └── Task.kt                  # Data class da entidade
│   └── remote/
│       ├── TaskApiService.kt        # Interface Retrofit (endpoints)
│       ├── RetrofitInstance.kt      # Singleton de configuração HTTP
│       └── TaskRepository.kt       # Abstração sobre a API
│
├── viewmodel/
│   ├── TaskUiState.kt               # Sealed classes de estado da UI
│   └── TaskViewModel.kt             # Lógica + StateFlow expostos para a UI
│
└── ui/
    ├── components/
    │   └── TaskItem.kt              # Card de tarefa reutilizável
    ├── screens/
    │   ├── TaskListScreen.kt        # Tela de lista de tarefas
    │   └── TaskFormScreen.kt        # Tela de criação/edição
    └── theme/
        ├── Color.kt                 # Paleta de cores Deep Ocean
        ├── Type.kt                  # Tipografia Nunito
        └── Theme.kt                 # MaterialTheme completo
```

---

## 6. Arquitetura — MVVM

O app segue o padrão **MVVM (Model-View-ViewModel)** recomendado pelo Google para apps Compose:

```
┌─────────────────────────────────────────────────────┐
│  UI Layer (Compose)                                 │
│  TaskListScreen / TaskFormScreen / TaskItem         │
│       │ observa StateFlow             │ chama fun   │
└───────┼─────────────────────────────────────────────┘
        │
┌───────▼─────────────────────────────────────────────┐
│  ViewModel Layer                                    │
│  TaskViewModel                                      │
│  - listUiState: StateFlow<TaskListUiState>          │
│  - formUiState: StateFlow<TaskFormUiState>          │
│  - selectedTask: StateFlow<Task?>                   │
└───────┼─────────────────────────────────────────────┘
        │ suspend fun
┌───────▼─────────────────────────────────────────────┐
│  Data Layer                                         │
│  TaskRepository → TaskApiService → Retrofit → HTTP  │
└─────────────────────────────────────────────────────┘
```

**Regras da arquitetura:**
- A UI **nunca** chama a API diretamente — sempre via ViewModel
- O ViewModel **nunca** importa classes de UI (nenhum `Context`, `Activity`, `Composable`)
- O Repository é a única porta de saída para a rede
- Estados da UI são imutáveis e observados via `collectAsStateWithLifecycle()`

---

## 7. Camada de Dados

### `Task.kt` — Modelo de Dados

```kotlin
data class Task(
    val id: Long? = null,          // null na criação, preenchido pela API
    val title: String,             // obrigatório
    val description: String,       // pode estar vazio
    val completed: Boolean = false // padrão: não concluída
)
```

`@SerializedName` garante que os campos do JSON do backend são mapeados corretamente mesmo se os nomes divergirem no futuro.

---

### `TaskApiService.kt` — Interface Retrofit

Define os 5 endpoints consumidos:

```kotlin
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
```

Todas as funções são `suspend` — rodam dentro de coroutines sem bloquear a thread principal.

---

### `RetrofitInstance.kt` — Configuração HTTP

```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"
```

> `10.0.2.2` é o endereço especial do emulador Android que aponta para o `localhost` da máquina host. Em dispositivo físico, use o IP da máquina na rede local.

O `HttpLoggingInterceptor` com nível `BODY` imprime todas as requisições e respostas no Logcat durante o desenvolvimento — útil para depurar problemas de serialização JSON.

---

### `TaskRepository.kt` — Repositório

Camada fina que abstrai a fonte de dados. Hoje delega tudo ao Retrofit, mas isola o ViewModel de saber de onde os dados vêm:

```kotlin
suspend fun getAllTasks(): List<Task> = api.getAllTasks()
suspend fun createTask(task: Task): Task = api.createTask(task)
// ...
```

---

## 8. Camada de ViewModel

### `TaskUiState.kt` — Estados da UI

**Lista de tarefas:**

```kotlin
sealed class TaskListUiState {
    object Loading : TaskListUiState()                 // buscando da API
    data class Success(val tasks: List<Task>)          // lista carregada
    data class Error(val message: String)              // falha na requisição
}
```

**Formulário de criação/edição:**

```kotlin
sealed class TaskFormUiState {
    object Idle    : TaskFormUiState()                 // aguardando ação
    object Loading : TaskFormUiState()                 // salvando
    object Saved   : TaskFormUiState()                 // salvo com sucesso → navegar de volta
    data class Error(val message: String)              // falha ao salvar
}
```

---

### `TaskViewModel.kt` — Lógica Central

Três `StateFlow` expostos para a UI:

| StateFlow | Tipo | Responsável por |
|---|---|---|
| `listUiState` | `TaskListUiState` | Estado da lista de tarefas |
| `formUiState` | `TaskFormUiState` | Estado do formulário de criação/edição |
| `selectedTask` | `Task?` | Tarefa carregada para edição |

**Funções públicas:**

| Função | O que faz |
|---|---|
| `loadTasks()` | Busca todas as tarefas da API e atualiza `listUiState` |
| `loadTaskForEdit(id)` | Busca uma tarefa específica e preenche `selectedTask` |
| `createTask(task)` | Cria nova tarefa via POST, emite `Saved` em sucesso |
| `updateTask(id, task)` | Atualiza tarefa via PUT, emite `Saved` em sucesso |
| `toggleCompleted(task)` | Inverte `completed` via PUT, recarrega a lista |
| `deleteTask(id)` | Remove via DELETE, recarrega a lista |
| `resetFormState()` | Reseta `formUiState` para `Idle` e limpa `selectedTask` |

Todas as operações de rede rodam em `viewModelScope.launch { }` — canceladas automaticamente quando o ViewModel é destruído.

---

## 9. Camada de UI

### `TaskItem.kt` — Componente de Tarefa

Card reutilizável exibido em cada linha da lista:

- Clique no card → navega para edição
- Checkbox → chama `toggleCompleted`
- Botão lixeira → abre `AlertDialog` de confirmação antes de excluir
- Título com `TextDecoration.LineThrough` quando concluída
- Texto esmaecido (`alpha 0.5f`) na coluna de texto de tarefas concluídas

---

### `TaskListScreen.kt` — Tela Principal

Gerencia três estados via `when (uiState)`:

| Estado | O que exibe |
|---|---|
| `Loading` | `CircularProgressIndicator` centralizado |
| `Error` | Mensagem de erro + botão "Tentar novamente" |
| `Success` (lista vazia) | Texto de instrução para criar a primeira tarefa |
| `Success` (com tarefas) | `LazyColumn` com um `TaskItem` por tarefa |

**TopAppBar** com cores `primaryContainer` e FAB teal para criar nova tarefa.

---

### `TaskFormScreen.kt` — Tela de Criação/Edição

Funciona em dois modos:
- **Criação:** `taskId == null` → título da barra "Nova Tarefa", botão "Salvar"
- **Edição:** `taskId != null` → título da barra "Editar Tarefa", botão "Atualizar", campos pré-preenchidos

**Ciclo de vida da tela:**

```
Tela entra
    → DisposableEffect(Unit): resetFormState() limpa estado anterior imediatamente
    → LaunchedEffect(taskId): se edição, busca tarefa da API
    → LaunchedEffect(selectedTask): quando tarefa chega, preenche os campos
    → LaunchedEffect(formUiState): quando estado vira Saved, navega de volta
```

O `DisposableEffect` é fundamental para evitar que um `Saved` residual de uma operação anterior dispare navegação imediata ao abrir a tela novamente.

**Validação:**
- Título em branco → exibe `supportingText` com mensagem de erro no campo
- Botão desabilitado durante `Loading` e antes de `selectedTask` chegar (modo edição)

---

## 10. Tema Deep Ocean

O app usa um tema customizado chamado **Deep Ocean** — inspirado em cores do oceano: teal, navy e cyan. Substitui completamente o tema padrão gerado pelo Android Studio.

### Paleta de Cores

**Light Mode:**

| Token Material3 | Cor | Hex |
|---|---|---|
| `primary` | Teal 600 | `#00897B` |
| `primaryContainer` | Teal 100 | `#B2DFDB` |
| `background` | Branco azulado | `#F0F7F7` |
| `surfaceVariant` | Teal 50 | `#E0F2F1` |
| `secondary` | Cyan 700 | `#0097A7` |
| `error` | Vermelho | `#D32F2F` |

**Dark Mode:**

| Token Material3 | Cor | Hex |
|---|---|---|
| `primary` | Teal 200 | `#80CBC4` |
| `background` | Navy profundo | `#0D1B2A` |
| `surface` | Navy médio | `#102232` |
| `surfaceVariant` | Navy claro | `#1A3044` |
| `tertiary` | Cyan elétrico | `#00E5FF` |

---

### Tipografia — Nunito

Fonte **Nunito** carregada via Google Fonts (`ui-text-google-fonts`). Nunito tem letterforms arredondados que combinam visualmente com os cantos arredondados dos cards.

```
Display  → Nunito Bold
Headline → Nunito Bold
Title    → Nunito SemiBold
Body     → Nunito Regular
Label    → Nunito Medium
```

A fonte é baixada em runtime pelo Google Play Services — sem arquivos `.ttf` no APK.

---

### Shapes — Cantos Arredondados

| Tamanho | Raio | Usado em |
|---|---|---|
| `extraSmall` | 8 dp | Chips, tooltips |
| `small` | 10 dp | Botões |
| `medium` | 16 dp | Cards de tarefa, dialogs |
| `large` | 24 dp | Bottom sheets |
| `extraLarge` | 28 dp | FAB |

---

### Arquivos do Tema

| Arquivo | Responsabilidade |
|---|---|
| `Color.kt` | 25 constantes Light + 25 Dark com os valores hex |
| `Type.kt` | `NunitoFontFamily` + objeto `Typography` com 15 estilos |
| `Theme.kt` | `LightColorScheme`, `DarkColorScheme`, `AppShapes`, `TasksFaculdadeTheme` |

**Dynamic Color desativado:** Em Android 12+ o sistema pode sobrescrever cores com as do papel de parede do usuário. O app força sempre a paleta Deep Ocean — `dynamicColor` foi removido completamente do `Theme.kt`.

---

## 11. Navegação

O app usa **Navigation Compose** com um `NavHost` central em `MainActivity`:

```
task_list              → TaskListScreen
task_form              → TaskFormScreen (criação, taskId = null)
task_form/{taskId}     → TaskFormScreen (edição, taskId = Long)
```

Um único `TaskViewModel` é instanciado na `MainActivity` e compartilhado entre as duas telas. Isso garante que a lista seja atualizada automaticamente após criar ou editar uma tarefa.

**Fluxo de navegação:**

```
TaskListScreen
    ├── FAB (+) ──────────────────────────────► task_form
    ├── Clique no card ──────────────────────► task_form/{id}
    └── (aguarda popBackStack de TaskFormScreen)

TaskFormScreen
    ├── Botão Voltar (←) ────────────────────► popBackStack
    └── Salvar com sucesso ──────────────────► popBackStack (via LaunchedEffect on Saved)
```

---

## 12. Contrato da API

O backend deve expor os seguintes endpoints em `http://localhost:8080`:

| Método | Endpoint | Body | Retorno | Descrição |
|---|---|---|---|---|
| `GET` | `/tasks` | — | `List<Task>` | Listar todas |
| `GET` | `/tasks/{id}` | — | `Task` | Buscar por ID |
| `POST` | `/tasks` | `Task` (sem id) | `Task` (com id) | Criar |
| `PUT` | `/tasks/{id}` | `Task` | `Task` | Atualizar |
| `DELETE` | `/tasks/{id}` | — | `204 No Content` | Excluir |

**Estrutura JSON de uma tarefa:**

```json
{
  "id": 1,
  "title": "Estudar Compose",
  "description": "Revisar LazyColumn e StateFlow",
  "completed": false
}
```

> O campo `id` é `null` ao enviar no `POST` e preenchido pela API na resposta.

---

## 13. Histórico de Commits

O projeto foi construído de forma incremental. Cada commit representa uma camada ou funcionalidade independente:

| Commit | Mensagem | O que foi feito |
|---|---|---|
| `b660b32` | `chore: initial commit` | Projeto base gerado pelo Android Studio |
| `2c9c12e` | `chore: add dependencies` | Retrofit, OkHttp, Coroutines, Navigation, ViewModel |
| `9343474` | `chore: add BOM comment` | Comentário explicativo no `okhttp-logging` no TOML |
| `adb00a1` | `chore: INTERNET permission` | `AndroidManifest.xml` com permissão de rede e cleartext |
| `00b2bab` | `feat: add Task data class` | Modelo com `@SerializedName` para JSON |
| `1e55813` | `feat: add data layer` | `TaskApiService`, `RetrofitInstance`, `TaskRepository` |
| `4359c76` | `feat: add ViewModel layer` | `TaskUiState` sealed classes + `TaskViewModel` |
| `d70a8fe` | `feat: add TaskItem` | Card de tarefa com dialog de confirmação de exclusão |
| `ff0570b` | `feat: add TaskListScreen` | Tela de lista com estados Loading/Error/Success |
| `a0a27c1` | `feat: add TaskFormScreen` | Formulário com modo criação e edição |
| `42975f2` | `feat: wire up NavHost` | `MainActivity` com rotas e ViewModel compartilhado |
| `a5e2750` | `docs: add Deep Ocean spec` | Especificação do redesign visual |
| `d242f53` | `chore: add google-fonts dep` | Dependência `ui-text-google-fonts` no TOML e gradle |
| `753979f` | `feat(ui): replace Color.kt` | Paleta Deep Ocean completa (50 tokens) |
| `5f804c9` | `fix(ui): correct Theme.kt` | `AppShapes`, esquemas de cor, sem dynamic color |
| `7da784d` | `feat(ui): Nunito typography` | `Type.kt` com NunitoFontFamily + 15 estilos + `arrays.xml` |
| `631495a` | `feat(ui): TaskItem colors` | `surfaceVariant` no card, `shapes.medium`, alpha em concluídas |
| `878a832` | `feat(ui): TopAppBar colors` | `primaryContainer` na TopAppBar, `primary` no FAB |

---

## 14. Decisões de Design

### Por que MVVM com StateFlow e não LiveData?

`StateFlow` é a escolha moderna para Compose. É null-safe por padrão, integra nativamente com `collectAsStateWithLifecycle()` e não requer contexto do Android no ViewModel.

### Por que um único ViewModel compartilhado entre telas?

Ao instanciar o `TaskViewModel` na `MainActivity` e passá-lo para ambas as telas, a lista é automaticamente recarregada quando o formulário salva. Uma alternativa seria usar `SavedStateHandle` por tela, mas seria mais complexo para este escopo.

### Por que `DisposableEffect` para resetar o formulário?

`LaunchedEffect` roda de forma assíncrona após a composição. Se um `Saved` residual existir do salvamento anterior, um `LaunchedEffect` que observe `formUiState` poderia disparar `navigateBack` imediatamente antes de qualquer composição da nova tela. `DisposableEffect(Unit)` roda de forma síncrona na composição inicial, garantindo que o estado seja limpo antes de qualquer efeito.

### Por que `dynamicColor = false`?

Em Android 12+ o sistema pode substituir todas as cores do app pelo esquema extraído do papel de parede. Isso destruiria a identidade visual Deep Ocean em qualquer dispositivo com wallpaper customizado. Fixar `dynamicColor = false` garante que a paleta seja sempre respeitada.

### Por que `10.0.2.2` e não `localhost`?

O emulador Android roda em uma máquina virtual separada. Do ponto de vista do emulador, `localhost` é ele mesmo. O endereço `10.0.2.2` é mapeado pelo Android Emulator para o `localhost` da máquina host onde o Android Studio está rodando.

### Por que `arrays.xml` para o Google Fonts?

A documentação da biblioteca `ui-text-google-fonts` afirma que os certificados são fornecidos automaticamente. Na prática, com o AGP 9.x, o arquivo `R.array.com_google_android_gms_fonts_certs` não é resolvido sem um `arrays.xml` explícito no projeto. O arquivo foi criado manualmente em `app/src/main/res/values/arrays.xml` com os certificados do Google Play Services.
#   T a s k s F a c u l d a d e  
 #   T a s k s F a c u l d a d e  
 #   T a s k s F a c u l d a d e  
 