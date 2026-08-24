# Changelog

Formato baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/);
versionamento conforme [SemVer](https://semver.org/lang/pt-BR/).

## [Não publicado]

### Adicionado
- `MnsAsyncImage`: carregamento de imagem remota com shimmer de placeholder e
  fallback, sobre Coil. Sobrecargas por URL em `MnsIcon`, `MnsAvatar` e
  `MnsCover`, mais `MnsListLeading.RemoteThumbnail`.

### Corrigido
- `MnsAvatarGroup` espalhava os avatares em vez de sobrepô-los: o deslocamento
  estava com o sinal invertido. Agora usa espaçamento negativo, o que também
  corrige a largura medida do grupo.
- `MnsSurface` pintava um fundo preto semiopaco ao desabilitar um container
  transparente, afetando `MnsButton` `TEXT`/`OUTLINED` e `MnsCard` `GHOST`.
- `MnsSlider` gravava a largura na fase de desenho: o primeiro toque era
  descartado e arrastes eram abortados quando o container mudava de tamanho.
- `MnsChip` tinha alvo de toque de 16dp no botão de remover, abaixo do piso de
  24dp do WCAG 2.5.8.
- `MnsShimmer` recriava `Brush`, `Outline` e `Path` a cada frame.
- Coordenadas de publicação apontavam para `matheusbrum`; o repositório é
  `matheusbrum11`, o que inviabilizaria a verificação de namespace no Central
  Portal e o espelho no GitHub Packages.
- Os grupos de concorrência de `ci.yml` faziam a execução direta e a chamada
  pelo `release.yml` se cancelarem, podendo impedir a publicação.

## [0.1.0] — 2026-08-20

Primeira versão do **MNS Design System**.

### Adicionado

**Tokens**
- Três camadas de token: `MnsPalette`/`MnsColorRamp` (reference), os grupos
  semânticos e o consumo por componente.
- 10 famílias: cor (53 papéis), tipografia (18 papéis), forma, espaçamento,
  dimensão, elevação, traço, opacidade e movimento.
- Fábricas `MnsColors.light` / `MnsColors.dark`, que derivam por interpolação
  tudo que não for informado.
- Utilitários de acessibilidade: `contrastRatio` (WCAG 2.1),
  `relativeLuminance`, `contentColorFor`.
- `MnsColors.byRole` e `roleNames` para acesso por nome.

**Tema**
- `MnsThemeSpec`, `MnsThemeProvider` e `MnsSimpleThemeProvider`.
- `MnsTheme` publicando todos os tokens via `CompositionLocal` estático.
- Três presets com variante clara e escura: `MnsIndigoTicket`, `MnsMonoEvents`,
  `MnsPastelGlass`.

**Componentes** — 44 páginas em 10 categorias
- Ações: `MnsButton`, `MnsIconButton`, `MnsFab`, `MnsSegmentedControl`.
- Entrada: `MnsTextField`, `MnsSearchField`, `MnsCurrencyField`,
  `MnsPasswordField`, `MnsOtpField`, `MnsCheckbox`, `MnsRadioButton`,
  `MnsSwitch`, `MnsChip`, `MnsStepper`, `MnsSlider`.
- Texto: `MnsText`, `MnsHeading`, `MnsSectionHeader`, `MnsCurrencyText`,
  `MnsPercentText`, `MnsCompactNumberText`.
- Status: `MnsBadge`, `MnsTag`, `MnsAlert`, `MnsCircularProgress`,
  `MnsLinearProgress`, `MnsRating`, `MnsEmptyState`.
- Layout: `MnsSurface`, `MnsCard`, `MnsScaffold`, `MnsTopBar`,
  `MnsBottomNavBar`, `MnsTabBar`, `MnsFixedTabBar`, `MnsBottomSheet`,
  `MnsDialog`, `MnsConfirmDialog`, `MnsDivider`.
- Listas: `MnsListAction`, `MnsAvatar`, `MnsAvatarGroup`.
- Atalhos: `MnsShortcutCard`, `MnsShortcutGrid`.
- Carregamento: `mnsShimmer`, `MnsShimmerBox`, `MnsShimmerParagraph`,
  `MnsShimmerListItem`, `MnsShimmerCard`.
- Códigos: `MnsQrCode`, `MnsQrEncoder`, `MnsTicketCard`.
- Mídia: `MnsIcon`, `MnsIcons`, `MnsCover`.

**Formatação**
- `MnsCurrencyFormatter` (centavos em `Long`), `MnsPercentFormatter`,
  `MnsCompactNumberFormatter`.
- `MnsCurrencyVisualTransformation` e `MnsMaskVisualTransformation`.

**Design Contract**
- Formato JSON versionado, codec bidirecional e derivação de campos ausentes.
- Contratos de exemplo para os três presets.
- Agente `mns-design-contract` que gera um contrato a partir de um print.

**Demonstração**
- `:app_demo` com catálogo por categoria, busca global, tela por componente com
  parâmetros interativos e playground de tokens com exportação de contrato.

**Qualidade**
- Suíte de integração com Robolectric e Compose UI Test — 96,4% de linhas.
- Matriz tema × componente cobrindo o catálogo em 6 temas.
- Kover com régua de 90%.
- `:benchmark` com macrobenchmark de startup e de rolagem.
- Esteira de CI/CD até a publicação no Maven Central.
- Gerador de documentação a partir do KDoc, com modo `--check` na CI.

[Não publicado]: https://github.com/matheusbrum11/mns-design-system/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/matheusbrum11/mns-design-system/releases/tag/v0.1.0
