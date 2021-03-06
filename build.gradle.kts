import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.xethlyx"
version = System.getenv("X_VERSION") ?: "DEV"

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.17-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.3")
    compileOnly("com.github.limework:redisbungee:0.6.5")

    implementation(kotlin("script-runtime"))
}

tasks.register<Copy>("devbuild") {
    dependsOn(tasks.getByPath("shadowJar"))
    group = "build"

    from("build/libs")
    include("**-all.jar")
    into("server/plugins")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ProcessResources>() {
    filesMatching("*.yml") {
        expand("version" to project.version)
    }
}