plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

val baseUrlProp = "BASE_URL"
val BASE_URL: String = (project.findProperty(baseUrlProp) as? String)
    ?: error("$baseUrlProp is not defined")

android {
    namespace = "net.ifmain.monologue"
    compileSdk = 35

    defaultConfig {
        applicationId = "net.ifmain.monologue"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"$BASE_URL\"")
    }

    signingConfigs {
        register("release") {
            storeFile = file("android-key.keystore")
            storePassword = project.findProperty("KEYSTORE_PASSWORD")?.toString()
            keyAlias = project.findProperty("KEY_ALIAS")?.toString()
            keyPassword = project.findProperty("KEY_PASSWORD")?.toString()
            enableV4Signing = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs["release"]
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.material3)

    implementation(libs.androidx.navigation.compose)

    // Room
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    implementation("androidx.room:room-guava:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")
    implementation("androidx.room:room-paging:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    annotationProcessor(libs.androidx.hilt.compiler.v120)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.hilt.navigation.compose)
}

kapt {
    correctErrorTypes = true
}

androidComponents {
    onVariants { variant ->
        val versionName = android.defaultConfig.versionName
        variant.outputs.forEach { output ->
            if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                output.outputFileName = "monologue_${versionName}.apk"
            }
        }

        tasks.configureEach {
            if (name == "bundle${variant.name.capitalize()}") {
                doLast {
                    val aabDir = file("${buildDir}/outputs/bundle/${variant.name}/")
                    val aabFile = aabDir.listFiles()?.find { it.extension == "aab" }

                    if (aabFile != null) {
                        val newAabFile = File(aabDir, "monologue_${versionName}.aab")
                        aabFile.renameTo(newAabFile)
                        println("✅ AAB renamed to: ${newAabFile.name}")
                    } else {
                        println("⚠️ No AAB file found in ${aabDir.path}")
                    }
                }
            }
        }
    }
}