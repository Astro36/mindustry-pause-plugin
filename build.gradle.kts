import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
