import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.room)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "br.com.amanfron.ecommerce_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.amanfron.ecommerce_app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val properties = Properties()
        if (rootProject.file("local.properties").exists()) {
            properties.load(FileInputStream(rootProject.file("local.properties")))
        }
        val baseUrl = System.getenv("BASE_URL") ?: properties.getProperty("BASE_URL", "")
        debug {
            buildConfigField("String", "BASE_URL", baseUrl)
        }

        release {
            buildConfigField("String", "BASE_URL", baseUrl)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dagger Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.dagger.hilt)
    implementation(libs.hilt.navigation.fragment)
    implementation(libs.hilt.navigation.compose)

    // Coil
    implementation(libs.coil)

    // Compose
    implementation(libs.bundles.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose.v280beta01)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Gson
    implementation(libs.gson)

    // Multidex
    implementation(libs.multidex)

    // AndroidX
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.kotlinx.coroutines.core)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)

    // Mockito
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    // Mockito-Kotlin
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android)

    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.junit4)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.testmanifest)
}