import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val kotlinVersion = "1.3.72"
val ktorVersion = "1.3.2"
val serializationVersion = "0.20.0"
val coroutinesVersion = "1.3.3"
val navigationVersion = "2.2.0-rc04"
val sqlDelightVersion = "1.3.0"

plugins {
    kotlin("multiplatform") version "1.3.72"
    id("org.jetbrains.kotlin.native.cocoapods") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    id("maven-publish")
}

group = "org.gradle.analytics"
version = "1.1.1"

repositories {
    mavenCentral()
    google()
    jcenter()
}

 publishing {
    repositories {
        maven {
            name = "SharedAnalytics"
            url = uri("https://maven.pkg.github.com/aleksiosdev/SharedAnalytcs")
            credentials {
                username = "aleksiosdev" ?: System.getenv("USERNAME")
                password = "700c778d3c8127d6b97ba8a36426ebf119eabfba" ?: System.getenv("TOKEN")
            }
        }
    }
}

kotlin {
    cocoapods {
        summary = "Shared module for Android and iOS"
        homepage = "Link to a Kotlin/Native module homepage"
    }

    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        compilations {
            val main by getting {
                kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
            }
        }
    }

    jvm("android") {
         mavenPublication { 
            artifactId = "SharedAnalytics"
        }
    }

     sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
            }
        }

         val iosMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutinesVersion")
                implementation("io.ktor:ktor-client-json-native:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-native:$ktorVersion")
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }

       val androidMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutinesVersion")
            }
        }
     }
}
