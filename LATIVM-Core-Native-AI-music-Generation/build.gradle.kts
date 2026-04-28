import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.lativm_native_ai_music_generation145645"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val localProperties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }
        val hfToken = localProperties.getProperty("HF_TOKEN") ?: ""
        buildConfigField("String", "HF_TOKEN", "\"$hfToken\"")
    }
    
    buildFeatures {
        buildConfig = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // Мрежна комуникација
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Ова му овозможува на 'app' модулот да ги користи истите библиотеки
    api("com.squareup.retrofit2:retrofit:2.11.0")

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    
    // Media3 ExoPlayer за свирење на стриминг аудио
    val media3Version = "1.3.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-common:$media3Version")
}