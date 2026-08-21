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

---

## Como cortar um release

1. Atualize `VERSION_NAME` em `gradle.properties` (ex.: `0.2.0`).
2. Atualize o `CHANGELOG.md`.
3. Abra o PR. A `ci.yml` precisa ficar verde.
4. Faça o merge em `main`.
5. A `release.yml` roda sozinha: valida de novo, assina, publica e cria a tag
   `v0.2.0` com uma GitHub Release.

Publicação manual, se necessário:

```bash
./gradlew :design_system:publishReleasePublicationToMavenCentralRepository
```

### Snapshots

Versões terminadas em `-SNAPSHOT` vão para o repositório de snapshots, sem
assinatura. Útil para validar um consumidor antes de fechar a versão.

---

## Repositórios configurados

`design_system/build.gradle.kts` declara três destinos:

| Nome | Uso |
|---|---|
| `mavenCentral` | Destino oficial. Releases e snapshots. |
| `githubPackages` | Espelho para consumo interno e pré-release. |
| `localStaging` | `build/local-maven-repo` — usado para testar a publicação sem rede. |

Testar o empacotamento localmente:

```bash
./gradlew :design_system:publishReleasePublicationToLocalStagingRepository
find build/local-maven-repo -name "*.aar" -o -name "*.pom"
```

---

## Coordenadas do artefato

```
io.github.matheusbrum:mns-design-system:<versão>
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
