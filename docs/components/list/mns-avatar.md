# MnsAvatar · MnsAvatarGroup

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsAvatar`

Avatar de usuário ou entidade.

Resolve o conteúdo em ordem de preferência: `imageUrl` → `painter` → `icon` →
iniciais de `name`. Ou seja, nunca renderiza vazio — o pior caso ainda mostra
as iniciais sobre uma cor derivada do nome, o que dá identidade estável ao
usuário mesmo sem foto. Uma URL que falha cai nas iniciais, não em um ícone
de imagem quebrada.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `name` | `String` | — | nome usado para gerar iniciais e a cor de fundo determinística. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `imageUrl` | `String?` | `null` | foto remota. Tem precedência sobre `painter` e `icon`; se a carga falhar, o avatar exibe as iniciais de `name`. |
| `painter` | `Painter?` | `null` | imagem do avatar já carregada, quando disponível. |
| `icon` | `ImageVector?` | `null` | ícone alternativo (ex.: entidade sem nome). |
| `size` | `Dp` | `MnsTheme.sizing.avatarMd` | diâmetro. Use `MnsTheme.sizing.avatar*`. |
| `shape` | `Shape` | `MnsTheme.shapes.avatar` | Forma do componente. Use um papel de `MnsTheme.shapes`. |
| `backgroundColor` | `Color?` | `null` | — |
| `borderWidth` | `Dp` | `MnsTheme.borders.none` | anel externo — use em grupos sobrepostos para separar. |
| `borderColor` | `Color` | `MnsTheme.colors.surface` | — |
| `contentDescription` | `String?` | `null` | descrição para leitores de tela; default é `name`. |

## `MnsAvatarGroup`

Pilha de avatares sobrepostos com contador de excedentes — o "+6" que aparece
nos cards de evento dos designs de referência.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `names` | `List<String>` | — | nomes dos participantes, na ordem de exibição. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `max` | `Int` | `3` | quantos avatares exibir antes de resumir no contador. |
| `size` | `Dp` | `MnsTheme.sizing.avatarSm` | Dimensão do componente. Use um degrau de `MnsTheme.sizing`. |
| `overlap` | `Float` | `0.35f` | fração de sobreposição entre avatares (0f = lado a lado). |
| `showOverflowCount` | `Boolean` | `true` | — |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/list/MnsAvatar.kt`
