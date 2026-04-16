# Design: UI Redesign — Deep Ocean Theme

**Data:** 2026-04-16
**Projeto:** TasksFaculdade
**Escopo:** UI apenas — zero alterações em lógica, ViewModel, Repository ou navegação

---

## 1. Objetivo

Modernizar a interface do app Android TasksFaculdade substituindo o tema padrão genérico do Android Studio por uma identidade visual "Deep Ocean": teal + navy + cyan, cantos arredondados, tipografia Nunito, suporte completo a light e dark mode.

---

## 2. Restrições

- Apenas arquivos da camada de UI são modificados
- Nenhum arquivo de lógica (`ViewModel`, `Repository`, `Model`, `ApiService`, navegação em `MainActivity`) é tocado
- Funcionalidade 100% preservada
- `dynamicColor` desativado (fixado em `false`) para garantir que a paleta customizada prevaleça em todos os dispositivos, inclusive Android 12+

---

## 3. Dependência Nova

```toml
# gradle/libs.versions.toml
androidx-compose-ui-text-google-fonts = { group = "androidx.compose.ui", name = "ui-text-google-fonts" }
```

```kotlin
// app/build.gradle.kts
implementation(libs.androidx.compose.ui.text.google.fonts)
```

> Versão gerenciada pelo Compose BOM — sem `version.ref` necessário.

---

## 4. Paleta de Cores

### Light Theme

| Token Material3 | Hex | Descrição |
|---|---|---|
| `background` | `#F0F7F7` | Branco com leve tint teal |
| `onBackground` | `#1A2E2E` | Quase preto azulado |
| `surface` | `#FFFFFF` | Branco puro |
| `onSurface` | `#1A2E2E` | Texto principal |
| `surfaceVariant` | `#E0F2F1` | Fundo dos cards |
| `onSurfaceVariant` | `#37474F` | Texto secundário |
| `primary` | `#00897B` | Teal 600 — cor principal |
| `onPrimary` | `#FFFFFF` | |
| `primaryContainer` | `#B2DFDB` | Teal 100 — TopAppBar bg |
| `onPrimaryContainer` | `#004D40` | |
| `secondary` | `#0097A7` | Cyan 700 |
| `onSecondary` | `#FFFFFF` | |
| `secondaryContainer` | `#B2EBF2` | Cyan 100 |
| `onSecondaryContainer` | `#006064` | |
| `tertiary` | `#00838F` | Cyan 800 |
| `onTertiary` | `#FFFFFF` | |
| `tertiaryContainer` | `#E0F7FA` | |
| `onTertiaryContainer` | `#006064` | |
| `error` | `#D32F2F` | |
| `onError` | `#FFFFFF` | |
| `errorContainer` | `#FFCDD2` | |
| `onErrorContainer` | `#B71C1C` | |
| `outline` | `#80CBC4` | Bordas e divisores |
| `outlineVariant` | `#B2DFDB` | |
| `scrim` | `#000000` | |

### Dark Theme

| Token Material3 | Hex | Descrição |
|---|---|---|
| `background` | `#0D1B2A` | Navy profundo |
| `onBackground` | `#E0F2F1` | Teal claro |
| `surface` | `#102232` | Navy médio |
| `onSurface` | `#E0F2F1` | |
| `surfaceVariant` | `#1A3044` | Navy claro — fundo cards |
| `onSurfaceVariant` | `#80CBC4` | |
| `primary` | `#80CBC4` | Teal 200 |
| `onPrimary` | `#003733` | |
| `primaryContainer` | `#00574F` | |
| `onPrimaryContainer` | `#B2DFDB` | |
| `secondary` | `#80DEEA` | Cyan 200 |
| `onSecondary` | `#003E47` | |
| `secondaryContainer` | `#00626E` | |
| `onSecondaryContainer` | `#B2EBF2` | |
| `tertiary` | `#00E5FF` | Cyan elétrico |
| `onTertiary` | `#003640` | |
| `tertiaryContainer` | `#004E5A` | |
| `onTertiaryContainer` | `#B2EBF2` | |
| `error` | `#EF9A9A` | |
| `onError` | `#6F0000` | |
| `errorContainer` | `#B71C1C` | |
| `onErrorContainer` | `#FFCDD2` | |
| `outline` | `#4DB6AC` | |
| `outlineVariant` | `#1A3044` | |
| `scrim` | `#000000` | |

---

## 5. Tipografia — Nunito (Google Fonts)

Font family: **Nunito** — letterforms arredondados, combina com o estilo visual geral.

```kotlin
val NunitoFontFamily = FontFamily(
    Font(GoogleFont("Nunito"), weight = FontWeight.Normal),
    Font(GoogleFont("Nunito"), weight = FontWeight.Medium),
    Font(GoogleFont("Nunito"), weight = FontWeight.SemiBold),
    Font(GoogleFont("Nunito"), weight = FontWeight.Bold),
)
```

Estilos aplicados em `Type.kt`:
- `displayLarge/Medium/Small`: Nunito Bold
- `headlineLarge/Medium/Small`: Nunito Bold
- `titleLarge/Medium/Small`: Nunito SemiBold
- `bodyLarge/Medium/Small`: Nunito Regular
- `labelLarge/Medium/Small`: Nunito Medium

---

## 6. Shapes (Corner Radius)

```kotlin
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(10.dp),   // botões
    medium     = RoundedCornerShape(16.dp),   // cards, dialogs
    large      = RoundedCornerShape(24.dp),   // bottom sheets
    extraLarge = RoundedCornerShape(28.dp),   // FAB
)
```

Passado como `shapes = AppShapes` em `MaterialTheme(...)`.

---

## 7. Alterações por Arquivo

### `gradle/libs.versions.toml`
- Adicionar: `androidx-compose-ui-text-google-fonts`

### `app/build.gradle.kts`
- Adicionar: `implementation(libs.androidx.compose.ui.text.google.fonts)`

### `ui/theme/Color.kt`
- Substituição completa: definir todas as cores Light e Dark listadas na seção 4

### `ui/theme/Type.kt`
- Substituição completa: configurar `NunitoFontFamily` + `Typography` com todos os estilos

### `ui/theme/Theme.kt`
- `dynamicColor = false` (hardcoded, não mais parâmetro)
- `LightColorScheme` com todos os tokens da seção 4
- `DarkColorScheme` com todos os tokens da seção 4
- `AppShapes` definido e passado para `MaterialTheme`

### `ui/components/TaskItem.kt`
- `Card` com `colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)`
- `shape = MaterialTheme.shapes.medium` (16dp explícito via tema)
- Tarefa concluída: aplicar `alpha(0.6f)` na `Column` de texto para efeito visual de "done"

### `ui/screens/TaskListScreen.kt`
- `TopAppBar` com `colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer, titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer)`

### `ui/screens/TaskFormScreen.kt`
- `TopAppBar` com as mesmas cores de `TaskListScreen`

---

## 8. Fora de Escopo

- Animações e transições entre telas
- Splash screen
- Ícone do app
- Qualquer mudança em lógica de negócio, navegação ou camada de dados
