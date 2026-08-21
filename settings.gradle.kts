pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "mns-design-system"

// ── Módulos ──────────────────────────────────────────────────────────────────
// :design_system → biblioteca publicada no Maven (o coração do projeto)
// :app_demo      → app de demonstração / playground interativo
// :benchmark     → macrobenchmark dos fluxos do app_demo
include(":design_system")
include(":app_demo")
include(":benchmark")
