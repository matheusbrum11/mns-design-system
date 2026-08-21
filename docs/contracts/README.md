# Contratos de exemplo

[← Design Contract](../design-contract.md) · [Tematização](../theming.md)

Três contratos gerados a partir dos prints de design usados como referência do
projeto. Eles são o material de estudo para escrever o seu:

| Arquivo | Preset equivalente | Identidade |
|---|---|---|
| [`indigo-ticket.json`](indigo-ticket.json) | `MnsIndigoTicket` | Uma cor de marca índigo, alto contraste, cantos moderados |
| [`mono-events.json`](mono-events.json) | `MnsMonoEvents` | Monocromático com acentos de seleção, botões em pílula |
| [`pastel-glass.json`](pastel-glass.json) | `MnsPastelGlass` | Pastel arredondado, densidade folgada |

Repare no campo `source.notes` de cada um: ele separa o que foi **amostrado do
print** do que foi **inferido**. Cores de status, por exemplo, raramente
aparecem em um screenshot de tela feliz — vale registrar isso em vez de
apresentá-las como decisão da marca.

## Carregando

```kotlin
val json = context.assets.open("pastel-glass.json").bufferedReader().readText()
MnsTheme(provider = MnsDesignContractCodec.toProvider(json)) { AppRoot() }
```

## Verificando

Os três contratos são exercitados por `MnsDesignContractTest`, que confirma o
ciclo completo de exportação e reimportação preservando identidade, cor
primária, fundo e raio-base.
