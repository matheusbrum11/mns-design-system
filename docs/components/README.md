# Componentes

> Gerado por `tools/generate_component_docs.py`. **Não edite à mão.**

[← Documentação](../README.md) · [Tokens](../tokens/README.md) · [Tematização](../theming.md) · [Design Contract](../design-contract.md)

São **45 páginas** cobrindo todos os componentes públicos do `:design_system`. Cada página traz a descrição, quando usar, e a tabela completa de parâmetros com tipo, valor padrão e o que cada um faz.

Todo componente listado aqui tem uma demonstração interativa no módulo `:app_demo` — abra o app, encontre-o pela aba da categoria e mexa nos parâmetros para ver o efeito em tempo real.

## Ações

_Botões e gatilhos de ação._

| Componente | Quando usar |
|---|---|
| [MnsButton](action/mns-button.md) | Botão com 5 níveis de ênfase, 3 tamanhos, ícones e estado de carregamento. |
| [MnsIconButton](action/mns-icon-button.md) | Ação representada só por ícone, com alvo de toque garantido em 48dp. |
| [MnsFab](action/mns-fab.md) | Ação flutuante única da tela, com estado expandido opcional. |
| [MnsSegmentedControl](action/mns-segmented-control.md) | Alternância entre 2 e 4 modos exclusivos, com indicador deslizante. |

## Entrada

_Coleta de dados do usuário._

| Componente | Quando usar |
|---|---|
| [MnsTextField](input/mns-text-field.md) | Campo de texto com rótulo fixo, dica, erro, contador e máscara. |
| [MnsSearchField](input/mns-search-field.md) | Campo de busca em pílula com botão de limpar contextual. |
| [MnsCurrencyField](input/mns-currency-field.md) | Campo monetário em centavos, com máscara aplicada a cada tecla. |
| [MnsPasswordField](input/mns-password-field.md) | Campo de senha com alternância de visibilidade acessível. |
| [MnsOtpField](input/mns-otp-field.md) | Código de verificação com uma caixa por dígito e colagem preservada. |
| [MnsCheckbox · MnsRadioButton · MnsSwitch](input/mns-selection-controls.md) | Controles de seleção com alvo de toque na linha inteira. |
| [MnsChip](input/mns-chip.md) | Filtro selecionável, com ícone e remoção opcional. |
| [MnsStepper](input/mns-stepper.md) | Incremento/decremento para intervalos pequenos, sem estado inválido. |
| [MnsSlider](input/mns-slider.md) | Faixa de valor contínua ou discreta, desenhada com tokens. |

## Texto

_Tipografia e formatação de valores._

| Componente | Quando usar |
|---|---|
| [MnsText](text/mns-text.md) | Texto base: herda estilo do container e cor da superfície. |
| [MnsHeading](text/mns-heading.md) | Título com semântica de cabeçalho para leitores de tela. |
| [MnsSectionHeader](text/mns-section-header.md) | Título de seção com ação à direita — o padrão "… See All". |
| [MnsCurrencyText](text/mns-currency-text.md) | Valor monetário a partir de centavos, sem erro de arredondamento. |
| [MnsPercentText](text/mns-percent-text.md) | Percentual formatado, com sinal e cor opcionais. |
| [MnsCompactNumberText](text/mns-compact-number-text.md) | Número grande abreviado, com valor completo na acessibilidade. |

## Status

_Feedback e estado do sistema._

| Componente | Quando usar |
|---|---|
| [MnsBadge](status/mns-badge.md) | Contador ou ponto de novidade ancorado a um ícone. |
| [MnsTag](status/mns-tag.md) | Rótulo estático de estado. Não é interativo — para isso use MnsChip. |
| [MnsAlert](status/mns-alert.md) | Mensagem contextual anunciada automaticamente por leitores de tela. |
| [MnsCircularProgress · MnsLinearProgress](status/mns-progress.md) | Progresso determinado ou indeterminado, com semântica correta. |
| [MnsRating](status/mns-rating.md) | Avaliação por estrelas, somente leitura ou interativa. |
| [MnsEmptyState](status/mns-empty-state.md) | Estado vazio que responde o quê, por quê e qual o próximo passo. |

## Layout

_Estrutura e containers de tela._

| Componente | Quando usar |
|---|---|
| [MnsSurface](layout/mns-surface.md) | Primitivo de todos os containers: forma, cor, borda, sombra e cor de conteúdo. |
| [MnsCard](layout/mns-card.md) | Container de conteúdo agrupado, com 5 estratégias de separação visual. |
| [MnsTopBar](layout/mns-top-bar.md) | Barra superior com navegação, título alinhável e ações. |
| [MnsBottomNavBar](layout/mns-bottom-nav-bar.md) | Navegação inferior para 2 a 5 destinos, com badge por item. |
| [MnsTabBar · MnsFixedTabBar](layout/mns-tab-bar.md) | Abas roláveis com indicador, ou segmentadas de largura fixa. |
| [MnsBottomSheet](layout/mns-bottom-sheet.md) | Sheet modal com alça, cabeçalho e cores vindas dos tokens. |
| [MnsDialog · MnsConfirmDialog](layout/mns-dialog.md) | Dialog modal e sua variante de confirmação com proteção contra exclusão acidental. |
| [MnsDivider](layout/mns-divider.md) | Separadores horizontal, vertical e com rótulo. |
| [MnsScaffold](layout/mns-scaffold.md) | Container de tela que mede as barras de verdade e devolve o padding exato. |

## Listas

_Coleções e itens acionáveis._

| Componente | Quando usar |
|---|---|
| [MnsListAction](list/mns-list-action.md) | Item de lista acionável com overline, subtítulo, metadados e slot final. |
| [MnsAvatar · MnsAvatarGroup](list/mns-avatar.md) | Avatar com fallback determinístico de iniciais e pilha com contador. |

## Atalhos

_Grades de acesso rápido._

| Componente | Quando usar |
|---|---|
| [MnsShortcutCard](shortcut/mns-shortcut-card.md) | Quadrado de categoria com ícone, rótulo e badge. |
| [MnsShortcutGrid](shortcut/mns-shortcut-grid.md) | Grade de categorias com seleção múltipla ou única. |

## Carregamento

_Placeholders e progresso._

| Componente | Quando usar |
|---|---|
| [mnsShimmer · MnsShimmerBox](loading/mns-shimmer.md) | Modifier de shimmer aplicável a qualquer elemento, com bloco pronto. |
| [MnsShimmerParagraph · MnsShimmerListItem · MnsShimmerCard](loading/mns-skeletons.md) | Esqueletos que espelham o layout real, evitando salto ao carregar. |

## Códigos

_QR Code e ingressos._

| Componente | Quando usar |
|---|---|
| [MnsQrCode](code/mns-qr-code.md) | QR Code renderizado com tokens, a partir de texto ou de matriz pronta. |
| [MnsTicketCard](code/mns-ticket-card.md) | Ingresso completo: cabeçalho, detalhes, picote e QR. |

## Mídia

_Ícones e imagens._

| Componente | Quando usar |
|---|---|
| [MnsIcon · MnsIcons](media/mns-icon.md) | Ícone que herda a cor da superfície, mais o conjunto próprio da lib. |
| [MnsAsyncImage](media/mns-async-image.md) | Imagem remota com shimmer de carregamento e fallback tokenizados. |
| [MnsCover](media/mns-cover.md) | Área de capa com placeholder shimmer, véu de legibilidade e overlay. |
