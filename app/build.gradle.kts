import com.android.build.api.variant.BuildConfigField

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    signingConfigs {
        var certPath = "debug.keystore"
        getByName("debug") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = rootProject.file(certPath)
            storePassword = "android"
        }
        //Ideally this parameters are set up using the environment of a CI server.
        create("release") {
            keyAlias = "release"
            keyPassword = ""
            storeFile = rootProject.file(certPath)
            storePassword = ""
        }
    }
    namespace = "com.pardo.frogmitest"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.pardo.frogmitest"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            //ideally, we would evaluate if the host machine is the CI server and provide the signature from it.
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = false
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        buildConfig = true
    }
    androidComponents {
        onVariants {
            var auth = if(System.getenv("frogmi_key")!=null){
                println("auth field is present!")
                System.getenv("frogmi_key")
            } else {
                println("auth field is absent. Set it manually and make sure to not commit it.")
                ""
            }
            var companyId = if(System.getenv("frogmi_company_id")!=null){
                println("company id field is present!")
                System.getenv("frogmi_company_id")
            } else {
                println("company id field is absent. Set it manually and make sure to not commit it.")
                ""
            }
            it.buildConfigFields.put(
                "AUTH", BuildConfigField(
                    "String", "\"" + auth + "\"", "dynamic key"
                )
            )
            it.buildConfigFields.put(
                "COMPANY_ID", BuildConfigField(
                    "String", "\"" + companyId + "\"", "dynamic value"
                )
            )
        }
    }

}

dependencies {

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.annotations)
    implementation(libs.okhttp)
    //implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    //implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    //implementation("com.squareup.okhttp3:okhttp:4.11.0")



    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$1.7.3")
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}