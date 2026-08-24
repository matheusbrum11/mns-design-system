# Primeiros passos

[← Documentação](README.md) · [Arquitetura](architecture.md) · [Tematização](theming.md)

## Requisitos

| Item | Versão |
|---|---|
| JDK | 17 |
| Android Gradle Plugin | 8.9.1 |
| Gradle | 8.11.1 (via wrapper) |
| Kotlin | 2.1.10 |
| `compileSdk` | 35 |
| `minSdk` | 24 (o módulo `:benchmark` exige 28) |
| Compose BOM | 2024.12.01 |

O design system expõe tipos do Compose na sua API pública (`Modifier`, `Color`,
`Shape`, `ImageVector`), então essas dependências vêm como `api` — você não
precisa declará-las de novo.

---

## 1. Adicionar a dependência

**`gradle/libs.versions.toml`**

```toml
[versions]
mnsDesignSystem = "0.1.0"

[libraries]
mns-design-system = { module = "io.github.matheusbrum11:mns-design-system", version.ref = "mnsDesignSystem" }
```

**`app/build.gradle.kts`**

```kotlin
dependencies {
    implementation(libs.mns.design.system)
}
```

Enquanto a versão for `-SNAPSHOT`, adicione o repositório de snapshots:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/") {
            mavenContent { snapshotsOnly() }
        }
    }
}
```

---

## 2. Envolver o app no `MnsTheme`

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MnsTheme(provider = MnsIndigoTicket) {
                AppRoot()
            }
        }
    }
}
```

`MnsTheme` é o **único** ponto do app que sabe qual tema está ativo. Ele publica
todos os tokens via `CompositionLocal`; os componentes leem de `MnsTheme.*`.

Por padrão o tema segue a preferência de modo escuro do sistema. Para controlar
manualmente, passe `darkTheme = ...`.

---

## 3. Primeira tela

```kotlin
@Composable
fun AppRoot() {
    var busca by remember { mutableStateOf("") }

    MnsScaffold(
        topBar = {
            MnsTopBar(
                title = "Eventos",
                actions = {
                    MnsIconButton(Icons.Filled.Search, "Buscar", onClick = { })
                },
            )
        },
        floatingActionButton = {
            MnsFab(Icons.Filled.Add, "Criar evento", onClick = { })
        },
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
            modifier = Modifier.padding(horizontal = MnsTheme.spacing.screenHorizontal),
        ) {
            item {
                MnsSearchField(
                    value = busca,
                    onValueChange = { busca = it },
                    placeholder = "Buscar eventos",
                )
            }
            item {
                MnsSectionHeader(
                    title = "Hoje",
                    action = { MnsButton("Ver todos", onClick = { }, variant = MnsButtonVariant.TEXT) },
                )
            }
            items(eventos, key = { it.id }) { evento ->
                MnsListAction(
                    title = evento.nome,
                    overline = evento.local,
                    subtitle = evento.endereco,
                    meta = evento.quando,
                    leading = MnsListLeading.Avatar(evento.nome),
                    trailing = { MnsAvatarGroup(evento.participantes) },
                    onClick = { abrir(evento) },
                )
            }
        }
    }
}
```

> **Aplique o `padding` que o `MnsScaffold` entrega.** Ignorá-lo é a causa nº 1
> de conteúdo escondido atrás da barra inferior.

---

## 4. Configurar o carregamento de imagens (opcional)

Os componentes que aceitam uma URL — `MnsAsyncImage`, `MnsIcon(imageUrl = …)`,
`MnsAvatar(imageUrl = …)`, `MnsCover(imageUrl = …)` e
`MnsListLeading.RemoteThumbnail` — usam o singleton do [Coil](https://coil-kt.github.io/coil/).

Sem nenhuma configuração eles já funcionam, desde que o seu app declare a
permissão de rede:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Para controlar cache, cabeçalhos ou autenticação, implemente `ImageLoaderFactory`
na sua `Application`:

```kotlin
class MeuApp : Application(), ImageLoaderFactory {
    override fun newImageLoader() = ImageLoader.Builder(this)
        .crossfade(true)
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor(InterceptorDeAutenticacao())
                .build()
        }
        .build()
}
```

O design system não impõe nada disso: ele só chama o loader que você configurou.

## 5. Trocar para a sua marca

O caminho mais curto é derivar de um preset:

```kotlin
object TemaAcme : MnsThemeProvider {
    override val id = "acme"
    override val displayName = "ACME"

    override val light = MnsIndigoTicket.light.copy(
        id = id,
        name = displayName,
        colors = MnsIndigoTicket.light.colors.copy(
            primary = Color(0xFFFF6B00),
            onPrimary = Color.White,
        ),
    )

    override val dark = MnsIndigoTicket.dark.copy(id = "$id-dark", name = displayName)
}

MnsTheme(provider = TemaAcme) { AppRoot() }
```

Detalhes, incluindo como partir de um print de design, em
[Tematização](theming.md) e [Design Contract](design-contract.md).

---

## 6. Rodar o app de demonstração

O módulo `:app_demo` é o catálogo vivo: lista todos os componentes por
categoria, e cada tela permite mexer nos parâmetros e ver o efeito em tempo
real.

```bash
./gradlew :app_demo:installDebug
```

A tela **Tokens** (ícone de ajustes na barra superior) troca o preset, a cor
primária, o raio, a densidade e a tipografia — e exporta o resultado como
[Design Contract](design-contract.md) em JSON.

---

## Configuração do projeto (para desenvolver a biblioteca)

```bash
git clone https://github.com/matheusbrum11/mns-design-system.git
cd mns-design-system
```

Crie o `local.properties` apontando para o seu SDK:

```properties
sdk.dir=/Users/voce/Library/Android/sdk
```

Verifique o setup:

```bash
./gradlew :design_system:assembleRelease :app_demo:assembleDebug
```

Os detalhes de build, testes e cobertura estão em
[Testes e qualidade](testing.md).
