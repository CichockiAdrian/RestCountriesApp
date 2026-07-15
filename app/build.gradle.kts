import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { input ->
            load(input)
        }
    }
}

fun String.asBuildConfigString(): String {
    val escapedValue = replace("\\", "\\\\")
        .replace("\"", "\\\"")

    return "\"$escapedValue\""
}

val restCountriesToken = localProperties.getProperty("REST_COUNTRIES_TOKEN")
    ?: System.getenv("REST_COUNTRIES_TOKEN")
    ?: ""

// Produkcyjne identyfikatory z Twojego panelu AdMob.
// Mogą zostać nadpisane przez local.properties lub zmienne środowiskowe.
val admobAppId = localProperties.getProperty("ADMOB_APP_ID")
    ?: System.getenv("ADMOB_APP_ID")
    ?: "ca-app-pub-1917524648787737~9441078574"

val admobBannerId = localProperties.getProperty("ADMOB_BANNER_ID")
    ?: System.getenv("ADMOB_BANNER_ID")
    ?: "ca-app-pub-1917524648787737/9896374145"

android {
    namespace = "com.example.restcountriesapp"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.restcountriesapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "REST_COUNTRIES_TOKEN",
            restCountriesToken.asBuildConfigString()
        )
    }

    buildTypes {
        debug {
            // Oficjalne testowe ID Google.
            // W debug nie pobieramy prawdziwych reklam z Twojego konta.
            manifestPlaceholders["ADMOB_APP_ID"] =
                "ca-app-pub-3940256099942544~3347511713"

            buildConfigField(
                "String",
                "ADMOB_BANNER_ID",
                "ca-app-pub-3940256099942544/9214589741".asBuildConfigString()
            )
        }

        release {
            // Produkcyjne ID z Twojego konta AdMob.
            manifestPlaceholders["ADMOB_APP_ID"] = admobAppId

            buildConfigField(
                "String",
                "ADMOB_BANNER_ID",
                admobBannerId.asBuildConfigString()
            )

            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("io.insert-koin:koin-android:4.0.0")
    implementation("io.insert-koin:koin-androidx-compose:4.0.0")

    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(platform("com.google.firebase:firebase-bom:34.15.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.firebaseui:firebase-ui-auth:9.0.0")

    // Google AdMob
    implementation("com.google.android.gms:play-services-ads:25.4.0")

    // Formularz zgody użytkownika, wymagany m.in. dla użytkowników z EOG.
    implementation("com.google.android.ump:user-messaging-platform:4.0.0")

    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}