# MnsTextField

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsTextField`

Campo de texto do design system.

Diferente do `OutlinedTextField` do Material, o rótulo aqui fica **acima** do
campo e não flutua. É uma escolha deliberada: rótulo flutuante economiza
altura mas some quando o campo está preenchido, o que prejudica revisão de
formulário longo — exatamente o caso de checkout.

```kotlin
MnsTextField(
    value = email,
    onValueChange = { email = it },
    label = "E-mail",
    placeholder = "voce@exemplo.com",
    errorMessage = erro,
)
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `value` | `String` | — | texto atual do campo. |
| `onValueChange` | `(String) -> Unit` | — | chamado a cada alteração. Não é chamado quando `readOnly` ou `enabled` impedem a edição. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `label` | `String?` | `null` | rótulo acima do campo. |
| `placeholder` | `String?` | `null` | texto exibido quando `value` está vazio. |
| `helperText` | `String?` | `null` | linha de apoio abaixo do campo. É suprimida quando há `errorMessage` — mostrar dica e erro juntos compete por atenção. |
| `errorMessage` | `String?` | `null` | mensagem de erro. Quando não-nula, o campo entra em estado de erro (borda `danger`) e a mensagem é anunciada por leitores de tela. |
| `enabled` | `Boolean` | `true` | Quando `false`, o componente ignora interação e reduz a opacidade. |
| `readOnly` | `Boolean` | `false` | permite seleção e cópia, bloqueia edição. |
| `leadingIcon` | `ImageVector?` | `null` | ícone no início do campo. |
| `trailingIcon` | `(@Composable () -> Unit)?` | `null` | slot livre no fim do campo (botão de limpar, olho de senha). |
| `singleLine` | `Boolean` | `true` | impede quebra de linha e troca Enter por "próximo". |
| `maxLines` | `Int` | `if (singleLine) 1 else 4` | limite de linhas quando `singleLine` é `false`. |
| `maxLength` | `Int?` | `null` | limite de caracteres. Quando definido, exibe contador. |
| `visualTransformation` | `VisualTransformation` | `VisualTransformation.None` | máscara visual — ver `MnsMaskVisualTransformation`. |
| `keyboardOptions` | `KeyboardOptions` | `KeyboardOptions.Default` | tipo de teclado e ação de IME. |
| `keyboardActions` | `KeyboardActions` | `KeyboardActions.Default` | Callbacks das ações de IME. |
| `shape` | `Shape` | `MnsTheme.shapes.input` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `textStyle` | `TextStyle` | `MnsTheme.typography.bodyLarge` | Estilo de texto. Use um papel de `MnsTheme.typography`. |
| `interactionSource` | `MutableInteractionSource` | `rememberMnsInteractionSource()` | Fonte de interação. Injete a sua para observar ou compartilhar os estados de toque/foco com outro elemento. |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/input/MnsTextField.kt`
