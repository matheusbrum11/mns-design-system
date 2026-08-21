# MnsIcon · MnsIcons

> Gerado por `tools/generate_component_docs.py` a partir do KDoc. **Não edite à mão** — altere o KDoc do componente e rode o gerador.

[← Todos os componentes](../../components/README.md) · [Tokens](../../tokens/README.md) · [Tematização](../../theming.md)

## `MnsIcon`

Ícone tingido pelo design system.

A cor default é `LocalContentColor`, o que faz o ícone acompanhar
automaticamente a superfície onde está (card claro, botão primário, banner
escuro) sem que o chamador precise passar `tint`.

### Parâmetros

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `imageVector` | `ImageVector` | — | vetor do ícone. |
| `contentDescription` | `String?` | — | descrição para leitores de tela. Passe `null` quando o ícone é decorativo e o significado já está no texto ao lado — descrever duas vezes a mesma coisa polui a navegação por voz. |
| `modifier` | `Modifier` | `Modifier` | `Modifier` aplicado ao nó raiz do componente. |
| `size` | `Dp` | `MnsTheme.sizing.iconMd` | lado do ícone. Use um degrau de `MnsTheme.sizing.icon*`. |
| `tint` | `Color` | `LocalContentColor.current` | cor de preenchimento. `Color.Unspecified` desliga o tingimento, preservando ícones multicoloridos (bandeira, logo de bandeira de cartão). |

---

### Ver no app de demonstração

Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no componente. A tela traz um preview interativo com **todos** os parâmetros acima expostos como controles.

**Fonte:** `design_system/src/main/java/com/mns/designsystem/component/media/MnsIcon.kt`
