# CI/CD e publicação

[← Documentação](README.md) · [Testes](testing.md) · [Contribuição](../CONTRIBUTING.md)

## Onde colocar os secrets

**`Settings` → `Secrets and variables` → `Actions` → aba `Secrets` → `New repository secret`**

Link direto:
`https://github.com/matheusbrum11/mns-design-system/settings/secrets/actions`

São cinco, com **estes nomes exatos** — os workflows procuram por eles:

| Secret | O que é | Onde obter |
|---|---|---|
| `MAVEN_CENTRAL_USERNAME` | *Username* do **User Token** | central.sonatype.com → canto superior direito → View Account → Generate User Token |
| `MAVEN_CENTRAL_PASSWORD` | *Password* do mesmo token | idem |
| `SIGNING_KEY` | Chave PGP privada em ASCII armor | `gpg --armor --export-secret-keys <KEY_ID>` |
| `SIGNING_KEY_ID` | Últimos 8 caracteres do ID | `gpg --list-secret-keys --keyid-format=short` |
| `SIGNING_PASSWORD` | Senha da chave privada | definida na criação da chave |

> O token do Central Portal **não** é o login antigo do OSSRH. Usar o login de
> conta resulta em `401 Unauthorized` na hora do upload.

Antes do primeiro release, publique a chave pública:

```bash
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>
```

E registre o namespace `io.github.matheusbrum11` em central.sonatype.com →
**Namespaces** → *Add Namespace*. A verificação é contra a posse da conta
GitHub de mesmo nome — por isso `GROUP` precisa bater com o dono do repositório,
e a esteira reprova quando não bate.

`GITHUB_TOKEN` e `GITHUB_ACTOR` são fornecidos pelo Actions. **Não crie.**

---

## A esteira

```
   Pull request                Merge em main              Release publicada
        │                           │                            │
        ▼                           ▼                            ▼
   ┌──────────┐              ┌─────────────┐            ┌────────────────┐
   │  ci.yml  │              │ snapshot.yml│            │  release.yml   │
   ├──────────┤              ├─────────────┤            ├────────────────┤
   │ lint     │              │ (chama ci)  │            │ valida versão  │
   │ docs     │              │      ↓      │            │ valida secrets │
   │ testes   │              │  publica    │            │ trava imutável │
   │ cobertura│              │  SNAPSHOT   │            │  (chama ci)    │
   │ build    │              └─────────────┘            │      ↓         │
   └──────────┘                                         │  publica       │
        │                                               │  espelha       │
   bloqueia merge                                       │  anexa à release│
                                                        │  verifica sync │
                                                        └────────────────┘
```

Três workflows, um gatilho cada, sem sobreposição:

| Workflow | Gatilho | O que faz |
|---|---|---|
| `ci.yml` | pull request, ou chamado | Portão de qualidade. Não tem gatilho de push em `main`: quem cobre a `main` é o snapshot, que o chama. |
| `snapshot.yml` | push em `main` | Qualidade + publica `X.Y.Z-SNAPSHOT`. |
| `release.yml` | **release publicada** no GitHub | Qualidade + publica a versão final, anexa os artefatos à release e confirma a sincronização. |

---

## Como publicar uma versão

A **tag da release é a fonte da verdade da versão**. Não existe passo de editar
`gradle.properties` antes de publicar — e portanto não existe o modo de falha
"esqueci de subir a versão".

1. Atualize o `CHANGELOG.md`.
2. Crie a release no GitHub com a tag `v0.2.0` (*Releases → Draft a new release*).
3. Ao publicar, o `release.yml` dispara sozinho e:
   - deriva `0.2.0` da tag e valida que é semver e que não é snapshot;
   - confere os secrets e se `GROUP` bate com o dono do repositório;
   - recusa se `0.2.0` já existe no Maven Central (lá é imutável);
   - roda a bateria de qualidade inteira;
   - publica no Central Portal com liberação automática;
   - espelha no GitHub Packages;
   - anexa `.aar`, `-sources.jar`, `-javadoc.jar` e `.pom` à release;
   - fica conferindo o `repo1.maven.org` até o artefato aparecer.

`VERSION_NAME` em `gradle.properties` permanece `-SNAPSHOT` para sempre: ele é a
versão de desenvolvimento, não a de release.

### Ensaio

*Actions → Release → Run workflow*, marcando **dry_run**. Valida versão e
empacota sem publicar, sem tocar a rede e sem exigir chave PGP.

Localmente:

```bash
./gradlew :design_system:publishToMavenLocal -PVERSION_NAME=0.0.0-local-SNAPSHOT
find ~/.m2/repository/io/github/matheusbrum11/mns-design-system/0.0.0-local-SNAPSHOT -type f
```

O sufixo `-SNAPSHOT` não é decorativo: o plugin só exige assinatura fora de
snapshots, e é isso que deixa a validação rodar em qualquer PR.

---

## O que trava um merge (`ci.yml`)

| Passo | Falha quando |
|---|---|
| Documentação | `tools/generate_component_docs.py --check` acusa doc defasada do KDoc |
| Links | `tools/check_docs_links.py` acha link interno quebrado |
| Android Lint | Qualquer *warning* (`warningsAsErrors = true`) |
| Testes | Qualquer teste falhando |
| Cobertura | Linha abaixo de **90%** (`koverVerify`) |
| Build | Erro de compilação ou empacotamento |
| Empacotamento Maven | Falta `.pom`, `-sources.jar`, `-javadoc.jar` ou `.aar`, ou o POM sai sem `<distribution>repo</distribution>` |

Localmente: `./gradlew qualityCheck`.

---

## Verificações prévias do release

Tudo isto roda **antes de compilar qualquer coisa**, para que o erro apareça em
segundos e com mensagem legível:

| Verificação | Reprova quando |
|---|---|
| Formato da versão | A tag não é `vX.Y.Z` (semver, pré-lançamento permitido) |
| Snapshot em release | A tag contém `SNAPSHOT` — snapshot sai do `snapshot.yml` |
| `GROUP` × dono | O namespace é `io.github.<conta>` e `<conta>` não é o dono do repositório |
| Secrets | Qualquer um dos cinco ausente **ou vazio** |
| Imutabilidade | A versão já existe no `repo1.maven.org` |

O teste dos secrets é de conteúdo, não de existência: um secret que não existe
**não some do ambiente**, o GitHub o injeta como string vazia. Sem essa
checagem, o sintoma seria um `401 Unauthorized` vindo de dentro do Gradle,
depois de compilar o projeto inteiro.

---

## O plugin de publicação

[`com.vanniktech.maven.publish`](https://vanniktech.github.io/gradle-maven-publish-plugin/),
declarado em `design_system/build.gradle.kts`. Monta o POM a partir das
propriedades `POM_*` de `gradle.properties`, gera os jars de sources e javadoc,
assina com PGP e envia ao **Central Portal** já disparando a liberação
(`automaticRelease = true`).

> Versão presa em **0.34.x** de propósito. A 0.35 exige Gradle 8.13; a 0.36/0.37
> exigem Gradle 9 + AGP 8.13 + Kotlin 2.2. A toolchain atual é Gradle 8.11.1 /
> AGP 8.9.1 / Kotlin 2.1.10 — subir o plugin exige subir a toolchain junto.

### Tasks

| Task | O que faz |
|---|---|
| `publishToMavenCentral` | Envia ao Central Portal e libera. |
| `publishAllPublicationsToGithubPackagesRepository` | Espelho no GitHub Packages. |
| `publishToMavenLocal` | Publica em `~/.m2` — a validação offline do CI. |
| `dropMavenCentralDeployment` | Descarta um deployment preso no portal. |

A publicação criada pelo plugin chama-se `maven` (não `release`): tasks antigas
como `publishReleasePublicationTo…` não existem mais.

---

## Versionamento

[SemVer](https://semver.org/lang/pt-BR/):

- **MAJOR** — remoção ou mudança incompatível na API pública ou nos tokens
  (`explicitApi()` torna isso visível no diff).
- **MINOR** — componente ou token novo, retrocompatível.
- **PATCH** — correção sem mudança de API.

Antes de `1.0.0`, `MINOR` pode conter quebras — sinalizadas no `CHANGELOG.md`.
