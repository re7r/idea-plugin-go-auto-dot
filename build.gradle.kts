import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.3.0"
}

group = "com.github.re7r"
version = "1.0.1"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        goland("2024.2")
        bundledPlugin("org.jetbrains.plugins.go")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
            untilBuild = "261.*"
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    buildSearchableOptions {
        enabled = false
    }

    runIde {
        systemProperty("idea.config.path", "${projectDir}/.idea/app/config")
        systemProperty("idea.system.path", "${projectDir}/.idea/app/system")
    }
}
