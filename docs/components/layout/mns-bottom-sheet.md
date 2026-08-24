# MnsBottomSheet

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsBottomSheet`

Bottom sheet modal tokenizado.

Envolve o `ModalBottomSheet` do Material 3 apenas para aplicar forma, cores,
scrim e alça vindos dos tokens — e para padronizar o cabeçalho (título +
botão de fechar), que é onde as implementações costumam divergir.

```kotlin
if (mostrarFiltros) {
    MnsBottomSheet(title = "Filtros", onDismissRequest = { mostrarFiltros = false }) {
        MnsChipRow(opcoes, selecionados, ::alternar)
        MnsButton("Aplicar", ::aplicar, fillMaxWidth = true)
    }
}
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `onDismissRequest` | `() -> Unit` | — | chamado ao arrastar para baixo, tocar no scrim ou acionar o botão de fechar. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `title` | `String?` | `null` | título do cabeçalho. `null` remove o cabeçalho inteiro. |
| `subtitle` | `String?` | `null` | linha de apoio sob o título. |
| `showHandle` | `Boolean` | `true` | exibe a alça de arraste no topo. |
| `showCloseButton` | `Boolean` | `true` | exibe o "✕" no cabeçalho. Mantenha ligado: nem todo usuário descobre o gesto de arrastar. |
| `containerColor` | `Color` | `MnsTheme.colors.surfaceElevated` | Cor de fundo do componente. |
| `scrimColor` | `Color` | `MnsTheme.colors.scrim.copy(alpha = MnsTheme.opacity.scrim)` | — |
| `shape` | `Shape` | `MnsTheme.shapes.bottomSheet` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `sheetState` | `SheetState` | `rememberModalBottomSheetState(skipPartiallyExpanded = false)` | estado do sheet; injete para controlar expansão programática. |
| `content` | `@Composable ColumnScope.() -> Unit` | — | conteúdo do sheet. Já recebe padding lateral dos tokens. |

## `MnsSheetHeader`

Cabeçalho padrão de sheets e dialogs: título, subtítulo e fechar.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `title` | `String` | — | — |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `subtitle` | `String?` | `null` | — |
| `onClose` | `(() -> Unit)?` | `null` | — |

## `MnsSheetHandle`

Alça de arraste do bottom sheet.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsBottomSheet.kt`
