import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

fun getApiKey(): String {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        val properties = Properties().apply {
            load(FileInputStream(localPropertiesFile))
        }
        return properties.getProperty("api.key") ?: "default-value"
    }
    return System.getenv("API_KEY") ?: "default-value"
}

android {
    namespace = "com.example.mainactivity"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mainactivity"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_KEY", "\"${getApiKey()}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Build Features used for view bindings KF 11/17/2023
    buildFeatures {
        viewBinding = true
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
        debug {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    sourceSets {
        getByName("test") {
            resources.srcDir("app/src/test/resources")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // HTTP client and JSON library
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    // Allow the use of LiveData and ViewModels objects KF 11/17/2023
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Assists with the management of navigation between fragments KF 11/17/2023
    implementation("androidx.activity:activity-ktx:1.8.1")

    // Dependencies that aid with horizontal card slider: added by BG
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    // Flexbox dependencies for alginItems & justifyContent: added by BG
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Materiel dependencies for the dot indication: added by BG
    implementation("com.google.android.material:material:1.10.0")

    // HTTP client and JSON library
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // Replacing (files("build/libs/jtds-1.3.1.jar")) to this
    implementation("net.sourceforge.jtds:jtds:1.3.1")

    implementation("androidx.compose.foundation:foundation-android:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    // Mock Web Server
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")

}
