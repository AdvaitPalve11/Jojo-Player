plugins {
    id("java")
}

group = "com.AdvaitPalve"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.openjfx:javafx-controls:24")
    implementation("org.openjfx:javafx-media:24")

}

tasks.test {
    useJUnitPlatform()
}