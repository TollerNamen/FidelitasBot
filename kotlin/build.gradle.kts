plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.tollernamen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.dv8tion:JDA:5.0.0-beta.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
    implementation("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")
    //implementation("com.squareup.okhttp3:okhttp:4.9.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("org.tollernamen.fidelitas.MainKt")
}
