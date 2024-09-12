plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.royalitpark.employeemanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.royalitpark.employeemanagement"
        minSdk = 23
        targetSdk = 34
        versionCode = 7
        versionName = "1.0.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    //keyPass:royal@123
//Alias:key0
//Keystore file:untitles


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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    var camerax_version = "1.4.0-alpha01"
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.hbb20:ccp:2.6.0")


    //implementation ("com.github.AppIntro:AppIntro:6.3.1")
    //implementation ("com.github.paolorotolo:appintro:4.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.5.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")


    implementation("com.github.bumptech.glide:glide:4.11.0")

    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    implementation("androidx.camera:camera-view:1.2.3")
    implementation("androidx.camera:camera-lifecycle:1.2.3")

    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("io.github.dzmitry-lakisau:month-year-picker-dialog:1.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    //implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging:23.0.0")


}