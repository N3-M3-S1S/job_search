plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.serialization) apply false
}

subprojects {
    afterEvaluate {
        configureAndroidDefaults()
    }
}

fun Project.configureAndroidDefaults() {
    extensions.configure<com.android.build.gradle.BaseExtension> {
        compileSdkVersion(35)

        defaultConfig {
            minSdk = 24
            targetSdk = 35
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension> {
        jvmToolchain(11)
    }
}