import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
    `maven-publish`
    signing
}

private val artifactGroup: String = providers.gradleProperty("GROUP").get()
private val artifactId: String = providers.gradleProperty("POM_ARTIFACT_ID").get()
private val artifactVersion: String = providers.gradleProperty("VERSION_NAME").get()
private val coverageMinimum: Int = providers.gradleProperty("mns.coverage.minimum").get().toInt()

group = artifactGroup
version = artifactVersion

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

    // Gera artefatos `release` + sources + javadoc para publicação no Maven.
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
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
//  Publicação Maven
//  Segredos vêm SEMPRE do ambiente (GitHub Actions Secrets). Nunca commite
//  chave ou senha — veja docs/ci-cd.md para a lista de constantes.
// ─────────────────────────────────────────────────────────────────────────────
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = artifactGroup
            this.artifactId = artifactId
            this.version = artifactVersion

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set(providers.gradleProperty("POM_NAME"))
                description.set(providers.gradleProperty("POM_DESCRIPTION"))
                url.set(providers.gradleProperty("POM_URL"))
                inceptionYear.set(providers.gradleProperty("POM_INCEPTION_YEAR"))
                licenses {
                    license {
                        name.set(providers.gradleProperty("POM_LICENSE_NAME"))
                        url.set(providers.gradleProperty("POM_LICENSE_URL"))
                        distribution.set(providers.gradleProperty("POM_LICENSE_URL"))
                    }
                }
                developers {
                    developer {
                        id.set(providers.gradleProperty("POM_DEVELOPER_ID"))
                        name.set(providers.gradleProperty("POM_DEVELOPER_NAME"))
                        url.set(providers.gradleProperty("POM_DEVELOPER_URL"))
                    }
                }
                scm {
                    url.set(providers.gradleProperty("POM_SCM_URL"))
                    connection.set(providers.gradleProperty("POM_SCM_CONNECTION"))
                    developerConnection.set(providers.gradleProperty("POM_SCM_DEV_CONNECTION"))
                }
            }
        }
    }

    repositories {
        // 1) Maven Central (Sonatype / Central Portal)
        maven {
            name = "mavenCentral"
            val releasesUrl = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://central.sonatype.com/repository/maven-snapshots/")
            url = if (artifactVersion.endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = System.getenv("MAVEN_CENTRAL_USERNAME")
                password = System.getenv("MAVEN_CENTRAL_PASSWORD")
            }
        }
        // 2) GitHub Packages (espelho, útil para consumo interno / pré-release)
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/matheusbrum/mns-design-system")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        // 3) Repositório local — usado pelos testes de publicação em CI
        maven {
            name = "localStaging"
            url = uri(rootProject.layout.buildDirectory.dir("local-maven-repo"))
        }
    }
}

signing {
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    isRequired = !artifactVersion.endsWith("SNAPSHOT") && signingKey != null
    if (signingKey != null) {
        useInMemoryPgpKeys(System.getenv("SIGNING_KEY_ID"), signingKey, signingPassword)
        sign(publishing.publications)
    }
}
