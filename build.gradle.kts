import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    val kotlinVersion = "1.3.72"

    kotlin("multiplatform") version "$kotlinVersion"
    kotlin("native.cocoapods") version "$kotlinVersion"
    kotlin("plugin.serialization") version "$kotlinVersion"

    id("maven-publish")
}

val ktorVersion = "1.3.2"
val artifactId = "SharedAnalytics"

group = "org.manychat.analytics"
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
            artifactId = "$artifactId"
        }
    }

     sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
            }
        }

         val iosMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }

       val androidMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
            }
        }
     }
}
