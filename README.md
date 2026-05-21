# TasksFaculdade

App Android em **Kotlin + Jetpack Compose** para gerenciar tarefas via API REST. Consome um backend Spring Boot, usa arquitetura **MVVM com StateFlow** e tema visual **Deep Ocean**.

---

## Funcionalidades

| Funcionalidade | Descrição |
|---|---|
| Listar tarefas | Exibe todas as tarefas com título, descrição e status |
| Criar tarefa | Formulário com título (obrigatório) e descrição |
| Editar tarefa | Pré-preenche o formulário com dados existentes |
| Marcar como concluída | Checkbox alterna o status via `PUT` na API |
| Excluir tarefa | Botão de lixeira com diálogo de confirmação |
| Loading / Error | Indicadores visuais para cada estado da requisição |
| Light + Dark mode | Tema automático baseado na preferência do sistema |

---

## Arquitetura

MVVM recomendado pelo Google para Compose.

```
app/src/main/java/com/kotlincrossplatform/tasksfaculdade/
├── MainActivity.kt                  Entry point — NavHost e tema
├── data/
│   ├── model/          Task (data class)
│   └── remote/         TaskApiService, RetrofitInstance, TaskRepository
├── viewmodel/
│   ├── TaskUiState.kt  Sealed classes de estado (Loading, Success, Error, Saved)
│   └── TaskViewModel.kt Lógica + StateFlow expostos para a UI
└── ui/
    ├── components/     TaskItem (card reutilizável)
    ├── screens/        TaskListScreen, TaskFormScreen
    └── theme/          Color, Type, Theme (Deep Ocean)
```

### Fluxo de dados

```
Screen → ViewModel (StateFlow) → Repository → TaskApiService (Retrofit) → HTTP → Spring Boot
```

---

## Stack

| Biblioteca | Versão | Uso |
|---|---|---|
| Kotlin | 2.2.10 | Linguagem principal |
| Android Gradle Plugin | 9.1.1 | Build system |
| Jetpack Compose BOM | 2026.02.01 | UI declarativa |
| Material3 | (BOM) | Design system |
| Navigation Compose | 2.9.0 | Navegação entre telas |
| Lifecycle / ViewModel | 2.10.0 | MVVM + StateFlow |
| Retrofit | 2.11.0 | Cliente HTTP |
| OkHttp | 4.12.0 | Camada HTTP + logging |
| Kotlin Coroutines | 1.10.1 | Chamadas assíncronas |
| ui-text-google-fonts | (BOM) | Fonte Nunito |

---

## Endpoints do backend

```
GET    /tasks
GET    /tasks/{id}
POST   /tasks
PUT    /tasks/{id}
DELETE /tasks/{id}
```

Backend Spring Boot separado rodando em `http://localhost:8080`. O emulador acessa via `10.0.2.2:8080`.

---

## Como executar

### Backend (Spring Boot)
```shell
cd ~/IdeaProjects/ProjetoJavaSpringFaculdade
./mvnw spring-boot:run
```

### Android (emulador)
```shell
./gradlew assembleDebug
./gradlew installDebug
```

Ou via **Run 'app'** no Android Studio com emulador API 35+.

---

## O que falta (próximos passos)

| Prioridade | Item |
|---|---|
| Alta | Testes unitários nos ViewModels e Repository |
| Alta | Injeção de dependência (Hilt ou Koin) |
| Média | Paginação na lista de tarefas |
| Média | Persistência local com Room (offline-first) |
| Baixa | Animações nas transições de tela |
| Baixa | Customizar `applicationId` (remover `com.kotlincrossplatform`) |
