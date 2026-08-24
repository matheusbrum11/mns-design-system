# Documentação — MNS Design System

Design system Android em Jetpack Compose: **100% tokenizável, desacoplado e
publicado como artefato Maven**.

[← Voltar ao README do projeto](../README.md)

---

## Por onde começar

| Se você quer… | Vá para |
|---|---|
| Usar a biblioteca no seu app | [Primeiros passos](getting-started.md) |
| Ver a lista de componentes | [Componentes](components/README.md) |
| Entender ou trocar os tokens | [Tokens](tokens/README.md) |
| Criar o tema da sua marca | [Tematização](theming.md) |
| Gerar tokens a partir de um print de design | [Design Contract](design-contract.md) |
| Entender como o projeto é organizado | [Arquitetura](architecture.md) |
| Rodar o projeto e os testes | [Testes e qualidade](testing.md) |
| Contribuir | [Guia de contribuição](../CONTRIBUTING.md) |
| Publicar uma versão | [CI/CD e publicação](ci-cd.md) |
| Entender as regras da `main` | [Proteção da main](protecao-da-main.md) |
| Medir performance | [Benchmark](benchmark.md) |

---

## Mapa completo

### Fundamentos

- **[Primeiros passos](getting-started.md)** — instalação, primeiro `MnsTheme`,
  requisitos de SDK e a primeira tela.
- **[Arquitetura](architecture.md)** — os quatro módulos, as três camadas de
  token e as regras que mantêm a biblioteca desacoplada.
- **[Tematização](theming.md)** — `MnsThemeProvider`, os três presets de
  referência e como derivar a sua marca sem fork.
- **[Design Contract](design-contract.md)** — o formato JSON de tokens, o
  codec e o agente que gera um contrato a partir de um screenshot.

### Tokens

Cada página traz a tabela completa de tokens daquela família, com tipo, valor
padrão e uma descrição breve do que cada um faz.

- [Visão geral](tokens/README.md)
- [Cor](tokens/colors.md) · [Paleta e utilitários](tokens/palette.md)
- [Tipografia](tokens/typography.md)
- [Forma](tokens/shapes.md)
- [Espaçamento](tokens/spacing.md) · [Dimensão](tokens/sizing.md)
- [Elevação](tokens/elevation.md) · [Traço](tokens/borders.md) · [Opacidade](tokens/opacity.md)
- [Movimento](tokens/motion.md)

### Componentes

**[Índice completo — 45 páginas](components/README.md)**, agrupadas por função:
ações, entrada, texto, status, layout, listas, atalhos, carregamento, códigos
e mídia.

### Processo

- **[Testes e qualidade](testing.md)** — como rodar, o que a suíte cobre e a
  régua de **90% de cobertura de integração**.
- **[CI/CD e publicação](ci-cd.md)** — a esteira, os *secrets* do GitHub
  Actions e o fluxo de release para o Maven Central.
- **[Proteção da main](protecao-da-main.md)** — o ruleset versionado: CI
  obrigatório, nenhum push direto e revisão por CODEOWNERS (no ruleset
  completo).
- **[Benchmark](benchmark.md)** — macrobenchmark de startup e de rolagem.
- **[Assets](assets/README.md)** — como capturar e nomear os prints usados na
  documentação.

---

## Convenções desta documentação

- **Tabelas de parâmetros e de tokens são geradas** a partir do KDoc por
  `tools/generate_component_docs.py`. Editá-las à mão é trabalho perdido: a CI
  regenera e compara. Para mudar uma descrição, mude o KDoc.
- Todo exemplo de código é Kotlin com Compose e assume que existe um
  `MnsTheme { }` acima na árvore.
- Termos em inglês (`token`, `shape`, `scaffold`) são mantidos quando são o
  nome do símbolo no código; o texto ao redor é em português.
