@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)

    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.kyawzinlinn.movieplayer"
    compileSdk = 33

    buildFeatures { viewBinding = true }

    defaultConfig {
        applicationId = "com.kyawzinlinn.movieplayer"
        minSdk = 24
        targetSdk = 33
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.google.android.exoplayer:exoplayer:2.19.0")
    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.0")
    implementation ("com.google.android.exoplayer:exoplayer-dash:2.19.0")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.0")
    implementation ("com.google.android.exoplayer:exoplayer-hls:2.19.0")

    implementation ("com.github.bumptech.glide:glide:4.15.1")

    val lifecycle_version = "2.6.1"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
}