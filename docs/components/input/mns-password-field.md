# MnsPasswordField

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsPasswordField`

Campo de senha com alternância de visibilidade.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `value` | `String` | — | senha atual. |
| `onValueChange` | `(String) -> Unit` | — | chamado a cada alteração. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `label` | `String?` | `"Senha"` | Rótulo textual do componente. |
| `placeholder` | `String?` | `null` | — |
| `helperText` | `String?` | `null` | — |
| `errorMessage` | `String?` | `null` | — |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `initiallyVisible` | `Boolean` | `false` | começa com a senha revelada. Deixe `false` — revelar por padrão expõe a senha a quem estiver olhando a tela. |
| `imeAction` | `ImeAction` | `ImeAction.Done` | — |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsSpecializedFields.kt`
