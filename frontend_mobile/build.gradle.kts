// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
<<<<<<< Updated upstream
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
=======
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
>>>>>>> Stashed changes
}