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
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    /*versionCatalogs {
        create("libs") {
            // Retrofit versions
            version("retrofit", "2.9.0")
            version("okhttp", "4.11.0")
            version("gson", "2.10.1")

            // Retrofit and Network libraries
            library("retrofit-core", "com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            library("retrofit-gson", "com.squareup.retrofit2", "converter-gson").versionRef("retrofit")
            library("okhttp-core", "com.squareup.okhttp3", "okhttp").versionRef("okhttp")
            library("okhttp-logging", "com.squareup.okhttp3", "logging-interceptor").versionRef("okhttp")
            library("gson", "com.google.code.gson", "gson").versionRef("gson")
        }
    }*/
}

rootProject.name = "Skyber"
include(":app")