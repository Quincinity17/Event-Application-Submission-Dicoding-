plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.halfsubmission"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.halfsubmission"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // BASE_URL untuk build type release
            buildConfigField("String", "BASE_URL", "\"https://event-api.dicoding.dev/\"")
        }
        debug {
            // BASE_URL untuk build type debug
            buildConfigField("String", "BASE_URL", "\"https://event-api.dicoding.dev/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true

    }
}

dependencies {
    // Core AndroidX libraries
    implementation (libs.androidx.core.ktx)
    implementation (libs.androidx.appcompat)
    implementation (libs.material)
    implementation (libs.androidx.constraintlayout.v221)
    implementation (libs.androidx.activity.ktx.v1101)

    // Lifecycle components
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation components
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    // Testing dependencies
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.androidx.espresso.core)

    // Networking and image loading
    implementation (libs.android.async.http)
    implementation (libs.glide)
    implementation (libs.retrofit2.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation (libs.shimmer)
    implementation (libs.picasso)

    // Room Database
    implementation (libs.androidx.room.runtime)
    ksp (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)

    // DataStore Preferences
    implementation (libs.androidx.datastore.preferences.v113)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    // WorkManager
    implementation (libs.androidx.work.runtime)
    implementation (libs.androidx.work.runtime.ktx)
}


