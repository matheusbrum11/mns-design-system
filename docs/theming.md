# Tematização

[← Documentação](README.md) · [Tokens](tokens/README.md) · [Design Contract](design-contract.md)

Tokenizar o MNS para a sua marca não exige fork, não exige tocar em componente
e não exige recompilar a biblioteca. Existem quatro caminhos, do mais rápido ao
mais completo.

---

## Caminho 1 — usar um preset

A biblioteca acompanha três presets, cada um extraído de um print de design
real. Eles existem tanto para uso direto quanto como ponto de partida.

```kotlin
MnsTheme(provider = MnsIndigoTicket) { AppRoot() }
```

| Preset | `id` | Identidade | Bom para |
|---|---|---|---|
| `MnsIndigoTicket` | `indigo-ticket` | Índigo `#6255F4` sobre branco, cantos 12dp, listas em fio de cabelo | **Padrão.** Alto contraste, uma cor de marca, nada disputando com o conteúdo |
| `MnsMonoEvents` | `mono-events` | Monocromático; cor só onde há seleção (menta) ou destaque (lavanda), botões em pílula, cantos 16dp | Produtos guiados por imagem — a UI some e a foto manda |
| `MnsPastelGlass` | `pastel-glass` | Fundo lilás, cards 20dp, turquesa e pervinca de apoio, espaçamento `Comfortable` | Lazer e descoberta. Ruim para telas densas de dados |

O catálogo completo está em `MnsThemePresets.all` — é o que alimenta o seletor
do `:app_demo` e o [teste de matriz](testing.md#matriz-tema--componente).

---

## Caminho 2 — derivar de um preset

Para 90% dos casos, mudar de 2 a 5 cores resolve:

```kotlin
val meuSpec = MnsIndigoTicket.light.copy(
    id = "acme",
    name = "ACME",
    colors = MnsIndigoTicket.light.colors.copy(
        primary = Color(0xFFFF6B00),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFE8D6),
        onPrimaryContainer = Color(0xFF7A2E00),
    ),
    shapes = MnsShapes.fromBaseRadius(base = 8.dp, pillButtons = true),
    spacing = MnsSpacing.Compact,
)

MnsTheme(spec = meuSpec) { AppRoot() }
```

Se você não quer nem escolher os containers, use as fábricas — elas derivam por
interpolação tudo que você não informar:

```kotlin
val cores = MnsColors.light(
    primary = Color(0xFFFF6B00),
    accent = Color(0xFF00B8A9),
    background = Color(0xFFFAFAFA),
)
```

---

## Caminho 3 — implementar `MnsThemeProvider`

É o ponto de extensão oficial. Implemente a interface e você controla 100% dos
tokens, com light e dark tratados como cidadãos de primeira classe.

```kotlin
object TemaAcme : MnsThemeProvider {

    override val id = "acme"
    override val displayName = "ACME"

    private val laranja = Color(0xFFFF6B00)

    override val light = MnsThemeSpec(
        id = id,
        name = displayName,
        isDark = false,
        colors = MnsColors.light(primary = laranja),
        typography = MnsTypography.default(FontFamily(Font(R.font.inter))),
        shapes = MnsShapes.fromBaseRadius(base = 10.dp),
        spacing = MnsSpacing.Default,
        sizing = MnsSizing.Default,
        elevation = MnsElevation.Light,
        borders = MnsBorders.Default,
        opacity = MnsOpacity.Default,
        motion = MnsMotion.Default,
    )

    override val dark = light.copy(
        id = "$id-dark",
        isDark = true,
        colors = MnsColors.dark(primary = Color(0xFFFF8A3D)),
        elevation = MnsElevation.Dark,
    )
}
```

Se o seu produto não tem modo escuro, devolva o mesmo spec em `dark`:
`supportsDarkMode` passa a reportar `false` e a UI para de oferecer o toggle.

Quando o spec já vem pronto (por exemplo, desserializado de um contrato), não
vale a pena declarar um `object` — use `MnsSimpleThemeProvider`:

```kotlin
val provider = MnsSimpleThemeProvider(
    id = "acme",
    displayName = "ACME",
    light = specClaro,
    dark = specEscuro,
)
```

---

## Caminho 4 — gerar a partir de um print

Você entrega o screenshot do design; o agente devolve o JSON de tokens.

```bash
# no Claude Code, dentro do repositório
/agents mns-design-contract  →  "gere o contrato para ~/Design/checkout.png"
```

```kotlin
val json = context.assets.open("acme-contract.json").bufferedReader().readText()
val provider = MnsDesignContractCodec.toProvider(json)

MnsTheme(provider = provider) { AppRoot() }
```

O formato, o codec e as regras de derivação estão em
[Design Contract](design-contract.md).

---

## Modo escuro

```kotlin
// Segue o sistema (padrão)
MnsTheme(provider = TemaAcme) { … }

// Controlado pelo app
MnsTheme(provider = TemaAcme, darkTheme = preferenciaDoUsuario) { … }
```

Três coisas mudam entre `light` e `dark` além das cores:

1. **Elevação.** Use `MnsElevation.Dark` — no escuro a sombra some e quem separa
   as camadas é a cor da superfície.
2. **Cores de status.** As fábricas já usam tons mais claros e saturados no
   escuro (`#4ADE80` em vez de `#16A34A`), que é o que preserva legibilidade.
3. **`colors.isLight`.** Componentes usam para decidir elevação versus borda; o
   app usa para pintar a status bar.

---

## Trocando tokens em tempo de execução

Como `MnsTheme` aceita um `spec` pronto, tema dinâmico é só estado:

```kotlin
var spec by remember { mutableStateOf(MnsIndigoTicket.light) }

MnsTheme(spec = spec) {
    AppRoot(onTrocarMarca = { nova -> spec = nova })
}
```

É exatamente o que o playground do `:app_demo` faz. Como os
`CompositionLocal`s são estáticos, a troca recompõe a subárvore inteira uma vez
— não há custo por leitura.

---

## Checklist antes de publicar um tema novo

- [ ] `contrastRatio(colors.textPrimary, colors.surface) ≥ 4.5`
- [ ] `contrastRatio(colors.onPrimary, colors.primary) ≥ 4.5`
- [ ] Idem para `onSuccess/success`, `onDanger/danger`, `onWarning/warning`
- [ ] `sizing.touchTarget ≥ 48.dp`
- [ ] Variante escura definida (ou `supportsDarkMode` honestamente `false`)
- [ ] Tema aberto no `:app_demo`, percorrendo **todas** as abas de categoria

Os dois primeiros itens já são verificados automaticamente para os presets da
biblioteca em `MnsThemeTest`. Se você contribuir um preset novo, ele entra
nessa mesma verificação — ver [Contribuição](../CONTRIBUTING.md).
