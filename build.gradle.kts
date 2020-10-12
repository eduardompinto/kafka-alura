plugins {
    kotlin("jvm") version "1.4.10"
}

group = "eduardompinto"
version = "1.0-SNAPSHOT"


subprojects {
    version = "0.0.0"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.apache.kafka:kafka-clients:2.6.0")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")
        implementation("com.google.code.gson:gson:2.8.6")
        runtimeOnly("org.slf4j:slf4j-simple:1.7.30")
    }

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}
