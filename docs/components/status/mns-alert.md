# MnsAlert

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsAlert`

Bloco de mensagem contextual (banner de aviso, confirmação, erro de
formulário).

É marcado como `liveRegion`, ou seja: quando aparece, leitores de tela
anunciam o conteúdo sem que o usuário precise navegar até ele. É a diferença
entre um erro percebido e um erro invisível.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `message` | `String` | — | texto principal. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `title` | `String?` | `null` | título opcional em negrito acima da mensagem. |
| `status` | `MnsStatus` | `MnsStatus.INFO` | intenção semântica; define cor e ícone padrão. |
| `icon` | `ImageVector?` | `null` | sobrescreve o ícone derivado de `status`. |
| `onDismiss` | `(() -> Unit)?` | `null` | quando não-nulo, exibe o botão de fechar. |
| `action` | `(@Composable () -> Unit)?` | `null` | slot para uma ação (ex.: "Tentar de novo"). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/status/MnsAlert.kt`
