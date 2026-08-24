# MnsButton

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsButton`

Botão do design system.

```kotlin
MnsButton(
    text = "Pay Now",
    variant = MnsButtonVariant.PRIMARY,
    size = MnsButtonSize.LARGE,
    fillMaxWidth = true,
    onClick = ::pagar,
)
```

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `text` | `String` | — | rótulo do botão. |
| `onClick` | `() -> Unit` | — | ação disparada no toque. Não é chamada quando `enabled` é `false` nem quando `loading` é `true`. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `variant` | `MnsButtonVariant` | `MnsButtonVariant.PRIMARY` | nível de ênfase — ver `MnsButtonVariant`. |
| `size` | `MnsButtonSize` | `MnsButtonSize.MEDIUM` | tamanho — ver `MnsButtonSize`. |
| `enabled` | `Boolean` | `true` | quando `false`, o botão fica opaco e ignora toques. |
| `loading` | `Boolean` | `false` | quando `true`, substitui o ícone à esquerda por um spinner e bloqueia o clique. O rótulo **permanece visível** de propósito: trocar o texto por um spinner faz o botão mudar de largura e o layout pular. |
| `leadingIcon` | `ImageVector?` | `null` | ícone antes do rótulo. |
| `trailingIcon` | `ImageVector?` | `null` | ícone depois do rótulo. |
| `fillMaxWidth` | `Boolean` | `false` | ocupa toda a largura disponível. |
| `shape` | `Shape` | `MnsTheme.shapes.button` | forma; default `MnsTheme.shapes.button`. |
| `contentDescription` | `String?` | `null` | descrição alternativa para leitores de tela; por padrão usa `text`. |
| `interactionSource` | `MutableInteractionSource` | `rememberMnsInteractionSource()` | Fonte de interação. Injete a sua para observar ou compartilhar os estados de toque/foco com outro elemento. |

## `MnsButtonVariant`

Nível de ênfase de um `MnsButton`.

A regra do design system é: **uma** ação `PRIMARY` por tela. Se você precisa
de duas, uma delas na verdade é `SECONDARY` ou `TEXT` — e a tela provavelmente
está pedindo duas decisões ao usuário de uma vez.

| Valor | Significado |
|---|---|
| `PRIMARY` | Ação principal, preenchida com `colors.primary`. |
| `SECONDARY` | Ação de apoio, preenchida com `colors.primaryContainer`. |
| `OUTLINED` | Ação de apoio com contorno, sem preenchimento. |
| `TEXT` | Ação terciária: só texto, sem container. |
| `DANGER` | Ação destrutiva (excluir, cancelar pedido). |

## `MnsButtonSize`

Tamanho de um `MnsButton`. Controla altura, padding e papel tipográfico.

| Valor | Significado |
|---|---|
| `SMALL` | 36dp — ações dentro de cards e listas. |
| `MEDIUM` | 48dp — **padrão**. |
| `LARGE` | 56dp — CTA de rodapé (checkout, confirmar). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/action/MnsButton.kt`
