plugins {
    id("java")
    id("application")
}

group = "com.AdvaitPalve"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("com.jojo.player.Main")
}

repositories {
    mavenCentral()
}

val javafxSdkRoot = "C:/javafx-sdk-21.0.2"

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(fileTree("$javafxSdkRoot/lib") {
        include("*.jar")
    })

}

tasks.test {
    useJUnitPlatform()
}

application {
    applicationDefaultJvmArgs = listOf(
        "--module-path", "$javafxSdkRoot/lib",
        "--add-modules", "javafx.controls,javafx.media",
        "-Djava.library.path=$javafxSdkRoot/bin"
    )
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}