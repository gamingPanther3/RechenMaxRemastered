plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mlprograms.rechenmax"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mlprograms.rechenmax"
        minSdk = 29
        targetSdk = 34
        versionCode = 110
        versionName = "3.0.47"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Splashscreen API
    implementation(libs.core.splashscreen.v100)

    implementation(libs.big.math)

    implementation(libs.androidx.interpolator)
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.animation.core.android)
    implementation(libs.app.update)

    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // This dependency is downloaded from the Google’s Maven repository.
    // So, make sure you also include that repository in your project's build.gradle file.
    implementation(libs.review)

    // For Kotlin users also import the Kotlin extensions library for Play In-App Review:
    implementation(libs.review.ktx)
}