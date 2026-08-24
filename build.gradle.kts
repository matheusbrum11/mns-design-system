plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.baselineprofile) apply false
}

/**
 * Task de conveniência: roda a suíte completa de qualidade que a esteira de CI
 * exige antes de liberar um merge para `main`.
 */
tasks.register("qualityCheck") {
    group = "verification"
    description = "Lint + testes de integracao + verificacao de cobertura (>= 90%)."
    dependsOn(
        ":design_system:lintRelease",
        ":app_demo:lintDebug",
        ":design_system:koverVerify",
    )
}
