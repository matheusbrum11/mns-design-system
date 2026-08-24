# Assets da documentação

[← Documentação](../README.md) · [Componentes](../components/README.md)

Esta pasta guarda as imagens usadas nas páginas de documentação: prints dos
componentes, prints dos designs de referência e diagramas.

## O que já existe

| Arquivo | Conteúdo |
|---|---|
| `app-demo-home.png` | Catálogo por categoria, preset Indigo Ticket |
| `app-demo-component-screen.png` | Tela de `MnsButton` com o painel de parâmetros |
| `app-demo-tokens.png` | Playground de tokens |
| `app-demo-dark.png` | Preset Pastel Glass em modo escuro |

## Convenção de nomes

```
component-<categoria>-<componente>[-<estado>].png
design-<origem>-<tela>.png
diagram-<assunto>.svg
```

Exemplos:

```
component-action-mns-button-variants.png
component-action-mns-button-loading.png
component-layout-mns-card-variants.png
design-acme-checkout.png
diagram-token-layers.svg
```

## Capturando prints de componente

O `:app_demo` é a fonte oficial: ele renderiza cada componente com os tokens
reais, e a tela do componente permite chegar exatamente no estado que você quer
documentar.

```bash
./gradlew :app_demo:installDebug

# navegue até o componente no app, ajuste os parâmetros, então:
adb exec-out screencap -p > docs/assets/component-action-mns-button-variants.png
```

Recomendações:

- **Pixel 6 / API 34** ou equivalente (411×891dp) — é a densidade em que a
  escala tipográfica foi calibrada.
- Capture **claro e escuro** quando o componente muda de comportamento entre os
  dois (elevação, superfície, contraste).
- Recorte só a área do preview; a moldura do app não acrescenta informação.
- PNG, abaixo de 300 KB por imagem.

## Referenciando

Nas páginas geradas por `tools/generate_component_docs.py`, **não** adicione a
imagem à mão: a página é sobrescrita a cada geração. Coloque a referência nas
páginas escritas à mão (`docs/*.md`) ou no KDoc do componente, que é a fonte da
página gerada.

```markdown
![Variantes do MnsButton](../assets/component-action-mns-button-variants.png)
```

## Prints de design de referência

Os três prints que originaram os presets não são versionados aqui (são material
de terceiros). O que ficou versionado é o resultado da leitura deles: os
[contratos de exemplo](../contracts/README.md), com as cores amostradas e as
notas de rastreabilidade.
