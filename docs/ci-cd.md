# CI/CD e publicação

[← Documentação](README.md) · [Testes](testing.md) · [Contribuição](../CONTRIBUTING.md)

## A esteira

```
  Pull Request                        Merge em main
       │                                    │
       ▼                                    ▼
 ┌───────────────┐                  ┌───────────────┐
 │ ci.yml        │                  │ release.yml   │
 ├───────────────┤                  ├───────────────┤
 │ Android Lint  │                  │ (roda ci.yml) │
 │ Docs em dia   │                  │ Assinatura PGP│
 │ Testes        │                  │ Publicação    │
 │ Kover ≥ 90%   │                  │ Tag + Release │
 │ Build         │                  └───────────────┘
 └───────────────┘
       │                                    │
   bloqueia merge                    artefato no Maven Central
```

**Integração contínua** valida todo PR. **Entrega contínua** publica ao entrar
em `main` — e só depois de a mesma bateria de qualidade passar de novo.

---

## `ci.yml` — o que trava um merge

| Passo | Comando | Falha quando |
|---|---|---|
| Lint | `:design_system:lintRelease` e `:app_demo:lintDebug` | Qualquer *warning* do Android Lint (`warningsAsErrors = true`) |
| Documentação | `python3 tools/generate_component_docs.py --check` | KDoc mudou e a doc não foi regerada |
| Testes | `:design_system:testDebugUnitTest`, `:app_demo:testDebugUnitTest` | Qualquer teste falhando |
| Cobertura | `:design_system:koverVerify` | Cobertura de linha abaixo de **90%** |
| Build | `:design_system:assembleRelease`, `:app_demo:assembleDebug` | Erro de compilação ou empacotamento |
| Empacotamento Maven | `:design_system:publishToMavenLocal` com `-PVERSION_NAME=0.0.0-ci-SNAPSHOT` | Falta `.pom`, `-sources.jar`, `-javadoc.jar` ou `.aar`, ou o POM sai sem `<distribution>repo</distribution>` |

Localmente, o atalho equivalente é:

```bash
./gradlew qualityCheck
```

---

## Secrets do GitHub Actions

Crie estes secrets em **Settings → Secrets and variables → Actions → New
repository secret**. Os nomes abaixo são exatamente os que os workflows leem —
não renomeie.

### Publicação no Maven Central

| Secret | O que é | Onde obter |
|---|---|---|
| `MAVEN_CENTRAL_USERNAME` | *Username* do token do Central Portal | central.sonatype.com → View Account → Generate User Token |
| `MAVEN_CENTRAL_PASSWORD` | *Password* do mesmo token | idem |

> As duas precisam ser um **User Token** do Central Portal (View Account →
> Generate User Token), não o login antigo do OSSRH — o portal recusa
> credenciais de conta.

### Assinatura PGP (obrigatória para releases)

| Secret | O que é | Como gerar |
|---|---|---|
| `SIGNING_KEY_ID` | Últimos 8 caracteres do ID da chave | `gpg --list-secret-keys --keyid-format=short` |
| `SIGNING_KEY` | Chave privada **em ASCII armor** | `gpg --armor --export-secret-keys <KEY_ID>` |
| `SIGNING_PASSWORD` | Senha da chave privada | definida na criação da chave |

Publique a chave pública antes do primeiro release:

```bash
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>
```

### GitHub Packages (espelho, opcional)

| Secret | Observação |
|---|---|
| `GITHUB_TOKEN` | Fornecido automaticamente pelo Actions. **Não crie.** |
| `GITHUB_ACTOR` | Idem. |

### Resumo para copiar

```
MAVEN_CENTRAL_USERNAME
MAVEN_CENTRAL_PASSWORD
SIGNING_KEY_ID
SIGNING_KEY
SIGNING_PASSWORD
```

Os workflows repassam esses secrets ao Gradle como propriedades de projeto, no
formato que o plugin espera:

| Secret | Variável de ambiente no workflow | Propriedade Gradle |
|---|---|---|
| `MAVEN_CENTRAL_USERNAME` | `ORG_GRADLE_PROJECT_mavenCentralUsername` | `mavenCentralUsername` |
| `MAVEN_CENTRAL_PASSWORD` | `ORG_GRADLE_PROJECT_mavenCentralPassword` | `mavenCentralPassword` |
| `SIGNING_KEY` | `ORG_GRADLE_PROJECT_signingInMemoryKey` | `signingInMemoryKey` |
| `SIGNING_KEY_ID` | `ORG_GRADLE_PROJECT_signingInMemoryKeyId` | `signingInMemoryKeyId` |
| `SIGNING_PASSWORD` | `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword` | `signingInMemoryKeyPassword` |

Para publicar da sua máquina, coloque as **propriedades** (coluna da direita) em
`~/.gradle/gradle.properties` — nunca no `gradle.properties` do repositório.

---

## Verificação prévia da publicação

Antes de compilar qualquer coisa, o `release.yml` confere:

| Verificação | Falha quando |
|---|---|
| `GROUP` × dono do repositório | O namespace é `io.github.<conta>` e `<conta>` não é o dono do repositório — o Central Portal recusaria, porque a verificação de namespace é contra a posse da conta GitHub |
| `MAVEN_CENTRAL_USERNAME` / `PASSWORD` | Ausentes ou **vazios** |
| `SIGNING_KEY` / `SIGNING_KEY_ID` / `SIGNING_PASSWORD` | Ausentes ou vazios **e** a versão não é `-SNAPSHOT` (o plugin só exige assinatura fora de snapshots) |

O teste é de conteúdo, não de existência: um secret que não existe **não some do
ambiente**, o GitHub o injeta como string vazia. Sem essa checagem, o sintoma
seria um `401 Unauthorized` vindo de dentro do Gradle, depois de compilar o
projeto inteiro — caro de ler e fácil de confundir com problema de rede.

Quando falha, a lista do que falta aparece no resumo do run, com o passo a passo
para resolver.

## Como cortar um release

1. Atualize `VERSION_NAME` em `gradle.properties` (ex.: `0.2.0`).
2. Atualize o `CHANGELOG.md`.
3. Abra o PR. A `ci.yml` precisa ficar verde.
4. Faça o merge em `main`.
5. A `release.yml` roda sozinha: valida de novo, assina, publica e cria a tag
   `v0.2.0` com uma GitHub Release.

Publicação manual, se necessário (exige as credenciais em
`~/.gradle/gradle.properties`):

```bash
./gradlew :design_system:publishToMavenCentral
```

### Snapshots

Versões terminadas em `-SNAPSHOT` vão para o repositório de snapshots, sem
assinatura. Útil para validar um consumidor antes de fechar a versão.

---

## O plugin de publicação

A publicação é feita por [`com.vanniktech.maven.publish`](https://vanniktech.github.io/gradle-maven-publish-plugin/),
declarado em `design_system/build.gradle.kts`. Ele monta o POM a partir das
propriedades `POM_*` de `gradle.properties`, gera os jars de sources e javadoc,
assina com PGP e envia ao **Central Portal** já disparando a liberação
(`automaticRelease = true`).

> A versão do plugin está presa em **0.34.x** de propósito. A 0.35 exige Gradle
> 8.13; a 0.36/0.37 exigem Gradle 9 + AGP 8.13 + Kotlin 2.2. A toolchain atual é
> Gradle 8.11.1 / AGP 8.9.1 / Kotlin 2.1.10 — subir o plugin exige subir a
> toolchain junto, e isso é uma migração à parte.

### Tasks

| Task | O que faz |
|---|---|
| `publishToMavenCentral` | Envia ao Central Portal e libera (por causa de `automaticRelease = true`). |
| `publishAndReleaseToMavenCentral` | Idem, com a liberação declarada explicitamente. Redundante aqui. |
| `publishAllPublicationsToGithubPackagesRepository` | Espelho no GitHub Packages. |
| `publishToMavenLocal` | Publica em `~/.m2` — é a validação offline usada no CI. |
| `dropMavenCentralDeployment` | Descarta um deployment que ficou preso no portal. |

A publicação criada pelo plugin chama-se `maven` (não `release`): tasks antigas
como `publishReleasePublicationTo…` não existem mais.

### Repositórios

| Nome | Uso |
|---|---|
| `mavenCentral` | Destino oficial, configurado pelo plugin. Releases via Central Portal, snapshots no repositório de snapshots. |
| `githubPackages` | Espelho para consumo interno e pré-release. |

Testar o empacotamento localmente, sem rede e sem chave PGP:

```bash
./gradlew :design_system:publishToMavenLocal -PVERSION_NAME=0.0.0-local-SNAPSHOT
find ~/.m2/repository/io/github/matheusbrum11/mns-design-system/0.0.0-local-SNAPSHOT -type f
```

O sufixo `-SNAPSHOT` não é decorativo: o plugin só exige assinatura fora de
snapshots, e é isso que deixa a validação rodar em qualquer PR.

---

## Coordenadas do artefato

```
io.github.matheusbrum11:mns-design-system:<versão>
```

Todas as coordenadas e metadados do POM ficam em `gradle.properties` — nada de
string cravada no `build.gradle.kts`.

---

## Versionamento

[SemVer](https://semver.org/lang/pt-BR/):

- **MAJOR** — remoção ou mudança incompatível na API pública ou nos tokens
  (`explicitApi()` torna isso visível no diff).
- **MINOR** — componente ou token novo, retrocompatível.
- **PATCH** — correção sem mudança de API.

Antes de `1.0.0`, `MINOR` pode conter quebras — sinalizadas no `CHANGELOG.md`.
