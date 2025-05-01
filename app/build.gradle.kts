plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.secrets)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.example.androiddevelopment2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androiddevelopment2"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        defaultConfig {
            buildConfigField("String", "RECIPE_API_URL", "\"https://api.spoonacular.com/recipes/\"")
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit)
    implementation(libs.glide)
    implementation(libs.viewbindingpropertydelegate.noreflection)
    implementation(libs.okhttp)
    implementation(libs.http.logging.interceptor)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.androidx.fragment)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.converter.gson)

    implementation(libs.shimmer)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
}


detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}