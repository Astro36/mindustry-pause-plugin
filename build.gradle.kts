plugins {
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenCentral()
    maven { url = uri("https://www.jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib"))
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
        from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}
