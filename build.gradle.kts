plugins {
    kotlin("jvm") version "1.4.10"
}

group = "me.haliksar"
version = "1.0-SNAPSHOT"

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks

repositories {
    mavenCentral()
    jcenter()
}

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.4.10"))
    }
}

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8", version = "1.4.10"))
        implementation(kotlin("compiler-embeddable", version = "1.4.10"))
    }

    compileKotlin.kotlinOptions.jvmTarget = "1.8"
    compileTestKotlin.kotlinOptions.jvmTarget = "1.8"
}