plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    mavenCentral()
    maven { url = uri("https://www.jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    compileOnly("com.github.Anuken.Arc:arc-core:v104.6")
    compileOnly("com.github.Anuken.Mindustry:core:v104.6")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    jar {
        enabled = false // disable original task
        dependsOn(shadowJar) // add new task
    }

    shadowJar {
        archiveClassifier.set("") // remove jar classifier
        minimize() // remove unused classes
    }
}
