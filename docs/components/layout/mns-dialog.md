# MnsDialog · MnsConfirmDialog

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsDialog`

Dialog modal tokenizado com slot livre de conteúdo.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `onDismissRequest` | `() -> Unit` | — | chamado ao tocar fora ou apertar "voltar". |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `dismissOnClickOutside` | `Boolean` | `true` | permite fechar tocando no scrim. Desligue em decisões destrutivas — fechar sem querer não pode ser possível ali. |
| `dismissOnBackPress` | `Boolean` | `true` | — |
| `containerColor` | `Color` | `MnsTheme.colors.surfaceElevated` | Cor de fundo do componente. |
| `shape` | `Shape` | `MnsTheme.shapes.dialog` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `content` | `@Composable ColumnScope.() -> Unit` | — | conteúdo do dialog. |

## `MnsConfirmDialog`

Dialog de confirmação pronto: ícone, título, mensagem e duas ações.

A ação destrutiva **nunca** vira a ação primária visual quando `status` é
`DANGER` sem confirmação explícita — o botão de confirmar usa a variante
`DANGER`, e o de cancelar fica com o peso visual maior. É uma barreira barata
contra exclusão acidental.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `title` | `String` | — | pergunta ou afirmação curta. |
| `message` | `String` | — | detalhamento e consequência da ação. |
| `confirmText` | `String` | — | rótulo do botão de confirmação. |
| `onConfirm` | `() -> Unit` | — | — |
| `onDismissRequest` | `() -> Unit` | — | — |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `dismissText` | `String?` | `"Cancelar"` | rótulo do botão de cancelamento; `null` esconde o botão. |
| `status` | `MnsStatus` | `MnsStatus.NEUTRAL` | intenção; `DANGER` troca o botão de confirmação para destrutivo. |
| `icon` | `ImageVector?` | `null` | ícone acima do título. |
| `loading` | `Boolean` | `false` | — |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/layout/MnsDialog.kt`
