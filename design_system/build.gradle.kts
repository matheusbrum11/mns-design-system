import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.vanniktech.maven.publish)
}

private val coverageMinimum: Int = providers.gradleProperty("mns.coverage.minimum").get().toInt()

// `group` e `version` são definidos pelo plugin de publicação a partir de
// GROUP e VERSION_NAME em gradle.properties — não os repita aqui.

android {
    namespace = "com.mns.designsystem"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.jvmArgs("-Xmx2g")
                it.testLogging { showStandardStreams = true }
            }
        }
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
        checkDependencies = true
        htmlReport = true
        sarifReport = true
        xmlReport = true
        // Só desabilitamos checagens de "existe versão mais nova": elas
        // quebram o build por passagem do tempo, não por regressão do código.
        // Tudo o mais é erro — inclusive os warnings.
        disable += setOf("GradleDependency", "NewerVersionAvailable", "AndroidGradlePluginVersion")
    }
}

/**
 * Os testes de unidade da variante `release` são desligados de propósito.
 *
 * Eles executariam exatamente o mesmo código-fonte da variante `debug`, só que
 * sem as dependências `debugImplementation` que a suíte de composição exige
 * (`ui-test-manifest`). O resultado seria uma suíte duplicada que falha por
 * configuração, não por regressão — e que dobra o tempo de CI à toa.
 */
androidComponents {
    beforeVariants(selector().withBuildType("release")) { variant ->
        variant.enableUnitTest = false
    }
}

// Explicit API: nenhum símbolo público pode escapar sem tipo/visibilidade
// declarados. É o que mantém o contrato da lib estável entre versões.
kotlin {
    explicitApi()
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    // A API do design system expõe tipos do Compose ⇒ `api`, não `implementation`.
    api(platform(libs.compose.bom))
    api(libs.compose.ui)
    api(libs.compose.ui.graphics)
    api(libs.compose.ui.text)
    api(libs.compose.foundation)
    api(libs.compose.animation)
    api(libs.compose.material3)

    implementation(libs.compose.ui.util)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.zxing.core)

    compileOnly(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    // ── Testes de integração (JVM + Robolectric) ─────────────────────────────
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(platform(libs.compose.bom))
    testImplementation(libs.compose.ui.test.junit4)
    // Necessário para `createAndroidComposeRule<ComponentActivity>()`, que dá aos
    // testes uma janela de tamanho real — sem ela o Robolectric usa uma janela
    // wrap-content e `assertIsDisplayed()` falha em tudo que usa fillMaxWidth.
    testImplementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.test.manifest)

    // ── Testes instrumentados (device/emulador) ──────────────────────────────
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.compose.ui.test.junit4)
}

// ─────────────────────────────────────────────────────────────────────────────
//  Cobertura — Kover
//  A régua é a mesma cobrada de qualquer contribuidor: 90% de linhas cobertas
//  pelos testes de integração. Código puramente declarativo (Previews, tokens
//  gerados, ComposableSingletons) fica fora da conta por não ter lógica.
// ─────────────────────────────────────────────────────────────────────────────
kover {
    currentProject {
        createVariant("default") {
            add("debug")
        }
    }
    reports {
        filters {
            excludes {
                annotatedBy("androidx.compose.ui.tooling.preview.Preview")
                classes(
                    "*.ComposableSingletons*",
                    "*_Factory*",
                    "*.BuildConfig",
                    "com.mns.designsystem.preview.*",
                )
            }
        }
        verify {
            rule("Cobertura de integracao minima do design system") {
                bound {
                    minValue.set(coverageMinimum)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Publicação Maven — com.vanniktech.maven.publish
//
//  O plugin cuida de tudo que antes era manual: monta o POM a partir das
//  propriedades POM_* de gradle.properties, gera os jars de sources e javadoc,
//  assina com PGP e faz o upload para o Central Portal já disparando a
//  liberação. O bloco manual anterior parava no staging da OSSRH e o artefato
//  nunca saía de lá.
//
//  Segredos vêm SEMPRE do ambiente (ORG_GRADLE_PROJECT_*), nunca do repositório
//  — veja docs/ci-cd.md para a lista de constantes.
// ─────────────────────────────────────────────────────────────────────────────
mavenPublishing {
    // `automaticRelease = true`: assim que a validação do Central Portal passa,
    // o deployment é liberado sozinho. Sem isso o artefato fica parado
    // aguardando um clique no portal — exatamente o problema que tínhamos.
    // Versões `-SNAPSHOT` ignoram este flag e vão para o repositório de
    // snapshots do Central Portal.
    publishToMavenCentral(automaticRelease = true)

    // A assinatura só é exigida em versões que não sejam `-SNAPSHOT`, então a
    // validação offline de CI continua rodando sem chave PGP.
    signAllPublications()

    // Substitui o `android { publishing { singleVariant("release") } }` manual:
    // o plugin configura a variante e anexa os jars de sources e javadoc, que o
    // Maven Central exige.
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = true,
            publishJavadocJar = true,
        ),
    )
}

// Espelho no GitHub Packages. O plugin acima cobre só o Maven Central, mas
// compõe com a publicação padrão do Gradle: basta declarar o repositório extra
// e a publicação criada pelo plugin (chamada `maven`) ganha mais um alvo, via
// `publishAllPublicationsToGithubPackagesRepository`.
publishing {
    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/matheusbrum/mns-design-system")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
