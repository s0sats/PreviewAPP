plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs")
}

android {
    compileSdk = 34
    buildToolsVersion = "34.0.0"
    namespace = "com.namoadigital.prj001"


    defaultConfig {
        applicationId = "com.namoadigital.prj001"
        minSdk = 21
        targetSdk = 34
        versionCode = 421
        versionName = "6.13.3"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
        lintOptions {
            isCheckReleaseBuilds = false
        }
    }

    buildTypes {

        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }
        register("staging") {
            initWith(getByName("debug"))
            isMinifyEnabled = true // Ativa a ofuscação
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }


    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    flavorDimensions += "default"

    productFlavors {
        create("productionGP") {
            applicationId = "com.namoadigital.prj001.productiongp"
        }
        create("production") {
            applicationId = "com.namoadigital.prj001.production"
        }
        create("development") {
            applicationId = "com.namoadigital.prj001.development"
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.concurrent.futures)
    implementation(libs.androidx.material2)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.iid)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.common)
    implementation(libs.hilt.navigation)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Outras bibliotecas
    implementation(libs.gson)
    implementation(libs.play.services.location)
    implementation("io.socket:socket.io-client:1.0.0") {
        exclude(group = "org.json", module = "json")
    }
    implementation("com.github.bumptech.glide:glide:4.9.0") {
        exclude(group = "com.android.support")
    }
    implementation(libs.androidbootstrap)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(files("json-20160212.jar"))
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

// Google Play Dependency
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
// import de dependecia do guava pelo conflito entre lib de exoplay e concurrent-future
    implementation(libs.guava.free)

// Namoa Library
    configurations.getByName("developmentImplementation") {
        implementation(libs.namoa.library)
    }
    configurations.getByName("productionImplementation") {
        implementation(libs.namoa.library)
    }
    configurations.getByName("productionGPImplementation") {
        implementation(libs.namoa.library)
    }

// Testes
    testImplementation(libs.junit)
    testImplementation(libs.room.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
}

/*apply plugin: 'com.google.gms.google-services'
apply plugin: 'com.google.firebase.crashlytics'
apply plugin: 'androidx.navigation.safeargs'*/

//Mac
/*repositories {
    maven {
        url "file:////Users/neomatrix/Desktop/libTest"
    }
    mavenCentral()
}

//Windows
repositories {
    maven {
        //url "file:///C:/AppsAndroid/NamoaLib"
        url "file:///C:/Android/LIB"
    }

}

//Linux
repositories {
    maven {
        url "file:////home/namoa/Documentos/LIB"
    }
}
//Repo Microblink, OCR reader
repositories {
    //maven { url 'http://maven.microblink.com' }
    maven { url 'https://jitpack.io' }
}*/
/*    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.work:work-runtime:2.7.1'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0'
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"

    androidTestImplementation('androidx.test.espresso:espresso-core:3.1.0', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    implementation 'com.google.code.gson:gson:2.8.0'

    implementation 'androidx.appcompat:appcompat:1.0.0'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'com.google.code.gson:gson:2.8.0'
    //Kotlin
    implementation 'androidx.core:core-ktx:1.3.2'
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"

    //LUCHE - 04/12/2019
    //Glide 4.3.1 era usa até versão oficial 3.13.1
    //Alterei para 4.9.0 para usr place holder - apagar comentario após testar se lib esta ok
    //implementation 'com.github.bumptech.glide:glide:4.3.1'
    implementation('com.github.bumptech.glide:glide:4.9.0') {
        exclude group: "com.android.support"
    }

    implementation 'com.beardedhen:androidbootstrap:2.3.1'

    developmentImplementation namoaLibVersion
    productionImplementation namoaLibVersion
    productionGPImplementation namoaLibVersion

    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'com.google.firebase:firebase-messaging:21.0.1'
    implementation 'com.google.firebase:firebase-crashlytics:18.3.1'




    testImplementation 'junit:junit:4.12'

    //implementation ('io.socket:socket.io-client:0.8.3') {
    implementation('io.socket:socket.io-client:1.0.0') {
        exclude group: 'org.json', module: 'json'
    }
    //--------------Google Play Dependency--------------------
    // Make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:app-update:2.1.0")
    // For Kotlin users, also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")


    implementation "androidx.concurrent:concurrent-futures:1.1.0"

    // Navigation Components
    implementation "androidx.navigation:navigation-fragment-ktx:2.2.2"
    implementation "androidx.navigation:navigation-ui-ktx:2.2.2"

    // Dagger Core
    implementation 'com.google.dagger:dagger:2.25.4'
    kapt 'com.google.dagger:dagger-compiler:2.25.2'

    // Dagger Android
    api 'com.google.dagger:dagger-android:2.23.2'
    api 'com.google.dagger:dagger-android-support:2.23.2'
    kapt 'com.google.dagger:dagger-android-processor:2.23.2'

    // Activity KTX for viewModels()
    implementation "androidx.activity:activity-ktx:1.1.0"

    //Dagger - Hilt
    implementation "com.google.dagger:hilt-android:2.42"
    kapt "com.google.dagger:hilt-android-compiler:2.42"

    // implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    kapt "androidx.hilt:hilt-compiler:1.1.0"

    implementation 'android.arch.lifecycle:extensions:1.1.1'
*/