# Paleta e utilitários de cor

> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir do KDoc. **Não edite a tabela à mão.**

[← Todos os tokens](README.md) · [Componentes](../components/README.md) · [Tematização](../theming.md)

A camada de **reference tokens**: valores crus, sem significado de interface. Nada na UI consome uma rampa diretamente — a rampa alimenta `MnsColors`, e é `MnsColors` que os componentes leem.

```
MnsPalette (cru)  →  MnsColors (semântico)  →  Componente
#6255F4           →  colors.primary          →  MnsButton
```

O módulo também expõe utilitários de acessibilidade: `contrastRatio(a, b)` (WCAG 2.1), `Color.relativeLuminance()` e `Color.contentColorFor()`, que escolhe entre claro e escuro o tom de maior contraste sobre uma cor arbitrária.

## `MnsPalette`

**Camada 1 — Reference tokens (tokens primitivos).**

São valores crus de cor, sem nenhum significado de interface. Nada na UI deve
consumir um `MnsPalette` diretamente: a UI consome `MnsColors` (camada 2), que
por sua vez aponta para estes primitivos. Essa indireção é o que permite
trocar a identidade visual inteira sem tocar em um único componente.

```
MnsPalette (cru)  →  MnsColors (semântico)  →  Componente
#6255F4           →  colors.primary          →  MnsButton
```

Cada rampa segue a escala 0–100 do Material (0 = mais escuro, 100 = branco),
mas sem qualquer acoplamento com o Material: são apenas nomes de degrau.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `brand` | `MnsColorRamp` | — | — |
| `neutral` | `MnsColorRamp` | — | — |
| `accent` | `MnsColorRamp` | — | — |
| `success` | `MnsColorRamp` | — | — |
| `warning` | `MnsColorRamp` | — | — |
| `danger` | `MnsColorRamp` | — | — |
| `info` | `MnsColorRamp` | — | — |

## `MnsColorRamp`

Rampa de 11 degraus de uma mesma matiz. Os degraus são intencionalmente
poucos: rampas grandes demais viram um catálogo que ninguém consegue manter
coerente entre light e dark.

### Propriedades

| Token | Tipo | Padrão | O que faz |
|---|---|---|---|
| `s0` | `Color` | — | degrau mais escuro da rampa — usado como texto sobre superfícies claras. |
| `s10` | `Color` | — | quase preto da matiz. |
| `s20` | `Color` | — | tom profundo, bom para estados `pressed` em tema escuro. |
| `s30` | `Color` | — | tom escuro de suporte. |
| `s40` | `Color` | — | tom escuro-médio, geralmente o `onContainer` no tema claro. |
| `s50` | `Color` | — | tom médio — normalmente a cor de marca em tema escuro. |
| `s60` | `Color` | — | **degrau base**: é daqui que sai `colors.primary` no tema claro. |
| `s70` | `Color` | — | tom claro-médio, bom para bordas e ícones secundários. |
| `s80` | `Color` | — | tom claro, usado em `container` de tema claro. |
| `s90` | `Color` | — | tom muito claro, fundos de destaque sutil. |
| `s95` | `Color` | — | quase branco da matiz — fundo de página tingido. |
| `s100` | `Color` | — | branco puro da rampa. |

---

**Fonte:** `design_system/src/main/java/com/mns/designsystem/token/MnsPalette.kt`
