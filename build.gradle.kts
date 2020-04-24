import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val kotlinVersion = "1.3.72"
version = "1.0.0"


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

repositories {
    mavenCentral()
    google()
    jcenter()
}

 publishing {
    publications {
        create<MavenPublication>("SharedAnalytics") {
            groupId = "org.gradle.analytics"
            artifactId = "analyticsShared"
            version = "1.0"
        }
    }
    repositories {
        maven {
            name = "SharedAnalytics"
            url = uri("https://maven.pkg.github.com/aleksiosdev/SharedAnalytcs")
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

    jvm("android")

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
        implementation("io.ktor:ktor-client-json:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization:$ktorVersion")
        implementation("io.ktor:ktor-client-core:$ktorVersion")
    }

    sourceSets["iosMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutinesVersion")
        implementation("io.ktor:ktor-client-json-native:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization-native:$ktorVersion")
        implementation("io.ktor:ktor-client-ios:$ktorVersion")
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutinesVersion")
    }
}
