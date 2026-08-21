# Design Contract

[← Documentação](README.md) · [Tematização](theming.md) · [Tokens](tokens/README.md)

O **Design Contract** é a representação portátil de um tema: um JSON pequeno,
legível e revisável em pull request, que vira `MnsThemeSpec` sem recompilar
nada.

Ele resolve um problema concreto: o caminho entre "o designer mandou o print" e
"o app está com essas cores" costuma ser alguém transcrevendo hex à mão, com o
erro de digitação que isso implica.

---

## O ciclo

```
   print de design (.png)
            │
            │  agente mns-design-contract
            ▼
   design-contract.json  ────────────────┐
            │                            │
            │  MnsDesignContractCodec    │  revisão em PR
            ▼                            │
      MnsThemeSpec                       │
            │                            │
            ▼                            │
   app rodando com a marca               │
            │                            │
            │  playground do :app_demo    │
            └────────────────────────────┘
                 exporta de volta
```

---

## O formato

Apenas `identity` e `colors.primary` são obrigatórios. Todo o resto é derivado.
Um print raramente revela 53 cores; o contrato precisa funcionar com o que dá
para ver.

```json
{
  "schemaVersion": 1,
  "identity": {
    "id": "acme",
    "name": "ACME",
    "dark": false,
    "description": "Marca laranja sobre fundo neutro, cantos moderados."
  },
  "colors": {
    "primary": "#FF6B00",
    "onPrimary": "#FFFFFF",
    "primaryContainer": "#FFE8D6",
    "accent": "#00B8A9",
    "background": "#FAFAFA",
    "surface": "#FFFFFF",
    "surfaceVariant": "#F0F0F2",
    "outline": "#E3E3E8",
    "textPrimary": "#16161A",
    "textSecondary": "#5B5B66",
    "danger": "#DC2626"
  },
  "shapes": {
    "baseRadiusDp": 10,
    "buttonRadiusDp": 14,
    "pillButtons": false
  },
  "typography": { "fontFamily": "Inter", "scale": 1.0 },
  "spacing":    { "density": "default", "scale": 1.0 },
  "elevation":  { "style": "light" },
  "motion":     { "durationScale": 1.0, "reduceMotion": false },
  "source": {
    "kind": "screenshot",
    "reference": "docs/assets/design-acme-checkout.png",
    "generatedAt": "2026-08-20T21:00:00Z",
    "notes": [
      "primary lida do botão de confirmação (amostra em 3 pontos).",
      "surfaceVariant inferida: o print não mostra campo de input."
    ]
  }
}
```

### Campos

| Bloco | Campo | Obrigatório | O que faz |
|---|---|---|---|
| — | `schemaVersion` | não (default `1`) | Versão do formato. O codec recusa versão maior que a suportada, com mensagem acionável. |
| `identity` | `id` | **sim** | Slug kebab-case; vira `MnsThemeSpec.id`. |
| `identity` | `name` | **sim** | Nome de exibição. |
| `identity` | `dark` | não | `true` quando o contrato descreve a variante escura. |
| `identity` | `description` | não | Resumo da intenção visual. |
| `colors` | `primary` | **sim** | Cor da ação principal. Tudo o mais pode ser derivado dela. |
| `colors` | 19 demais | não | Ver [tokens de cor](tokens/colors.md). Ausentes são interpolados. |
| `shapes` | `baseRadiusDp` | não (16) | Raio dos cards; origem da escala inteira. |
| `shapes` | `buttonRadiusDp` | não | Raio próprio dos botões. |
| `shapes` | `pillButtons` | não | `true` força botão em pílula e ignora `buttonRadiusDp`. |
| `typography` | `fontFamily` | não | Informativo — registrar a fonte é papel do app. |
| `typography` | `scale` | não (1.0) | Multiplicador da escala inteira. |
| `spacing` | `density` | não (`default`) | `compact`, `default` ou `comfortable`. |
| `spacing` | `scale` | não (1.0) | Multiplicador extra sobre a densidade. |
| `elevation` | `style` | não (`light`) | `light`, `dark` ou `flat`. |
| `elevation` | `shadowAlpha` | não | Sobrescreve a opacidade da sombra. |
| `motion` | `durationScale` | não (1.0) | Multiplica todas as durações. |
| `motion` | `reduceMotion` | não | Zera o movimento. |
| `source` | `kind` | não | `screenshot`, `figma`, `manual` ou `playground`. |
| `source` | `notes` | não | **Use.** É onde o agente registra o que foi *lido* versus *inferido*. |

> O campo `source.notes` não é decorativo. Ele é o que impede alguém tratar uma
> cor inferida como cor oficial da marca.

---

## Usando um contrato

```kotlin
val json = context.assets.open("acme-contract.json").bufferedReader().readText()
val provider = MnsDesignContractCodec.toProvider(json)

MnsTheme(provider = provider) { AppRoot() }
```

Quando o contrato descreve só a variante clara, `toProvider` deriva a escura
automaticamente a partir das mesmas cores de marca.

Para controle fino:

```kotlin
val contrato = MnsDesignContractCodec.decode(json)   // valida schema
val spec = MnsDesignContractCodec.toThemeSpec(contrato)
```

Erros viram `MnsContractException` com mensagem que aponta o campo:

```
Valor de cor inválido em 'colors.primary': 'roxo'. Use #RRGGBB ou #AARRGGBB.
```

---

## Exportando um contrato

Do playground do `:app_demo`: **Tokens → Exportar Design Contract (JSON)**.

Programaticamente:

```kotlin
val contrato = MnsDesignContractCodec.fromThemeSpec(
    spec = MnsTheme.spec,
    source = ContractSource(kind = "playground", reference = "app_demo"),
)
val json = MnsDesignContractCodec.encode(contrato)
```

A conversão é **assimétrica de propósito**: ler um contrato preenche as lacunas
com derivações; exportar grava só o que um print conseguiria revelar. É o que
mantém o JSON pequeno e revisável, em vez de um dump de 53 cores que ninguém lê
no diff.

---

## O agente gerador

`.claude/agents/mns-design-contract.md` define um agente que:

1. lê toda a documentação de tokens deste repositório;
2. recebe um print de design;
3. **amostra as cores de verdade** — não estima de memória;
4. mede raios, espaçamento e alturas em relação a referências conhecidas;
5. emite o JSON, registrando em `source.notes` o que foi lido e o que foi
   inferido;
6. valida contraste WCAG e avisa quando um par cor/conteúdo fica abaixo de
   4.5:1.

Uso:

```
/agents mns-design-contract
> Gere o contrato de tokenização para /Users/voce/Design/checkout.png,
> id "acme", nome "ACME".
```

O resultado vai para `docs/contracts/<id>.json`. Revise o `notes` antes de
promover para produção.

Exemplos prontos, extraídos dos três prints de referência do projeto, estão em
[`docs/contracts/`](contracts/).

---

## Versionamento do schema

`schemaVersion` só muda em alteração **incompatível**. Adicionar um campo
opcional não incrementa nada: o parser ignora chaves desconhecidas e usa
default para ausentes, então contratos antigos continuam funcionando.

Se o contrato declarar uma versão maior que a suportada pelo build, o codec
falha imediatamente pedindo atualização da biblioteca — em vez de aplicar um
tema pela metade.
