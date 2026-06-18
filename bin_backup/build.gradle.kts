plugins {
    id("java")
    id("application")
    id("org.beryx.jlink") version "3.1.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.AdvaitPalve"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("com.jojo.player.Main")
}

javafx {
    version = "21.0.2"
    modules = listOf(
        "javafx.controls",
        "javafx.fxml",
        "javafx.media"
    )
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

jlink {
    imageZip.set(file("$buildDir/distributions/app.zip"))

    launcher {
        name = "JojoPlayer"
    }

    jpackage {
        installerType = "exe"
        imageName = "JojoPlayer"
        appVersion = "0.1.0"
    }
}