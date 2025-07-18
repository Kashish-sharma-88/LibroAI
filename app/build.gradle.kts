plugins {
    id ("com.android.application")
    id ("com.google.gms.google-services")

}

android {
    namespace = "com.example.libroai"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.libroai"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled= true
        val openRouterKey = project.findProperty("OPENROUTER_API_KEY") ?: ""
        buildConfigField("String", "OPENROUTER_API_KEY", "\"$openRouterKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
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
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.okhttp)
    implementation(libs.moshi.v1150)
    implementation(libs.moshi.kotlin.v1150)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)
    implementation (libs.de.circleimageview)
    implementation(libs.google.generativeai)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.glide)
    implementation (libs.glide.v4151)
    implementation (libs.multidex)
    implementation (libs.firebase.ui.firestore)
    implementation (libs.firebase.firestore.v2444)
    implementation (libs.imagepicker)
    implementation (libs.com.google.firebase.firebase.storage)
    implementation (libs.google.firebase.firestore)
    implementation("com.github.dhaval2404:imagepicker:2.1")

}
