plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "me.RareHyperIon"
version = "1.1"
description = "A plugin aimed to add fun chat based games to your server."

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val properties = mapOf(
            "version" to project.version,
            "name" to project.name,
            "apiVersion" to "1.21",
            "authors" to "RareHyperIon, DaveDuart",
            "description" to project.description
        )
        inputs.properties(properties)
        filesMatching("paper-plugin.yml") {
            expand(properties)
        }
    }
}