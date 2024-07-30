import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
}

group = "net.azisaba"
version = "1.0-SNAPSHOT"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/public/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.15.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.0.1")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from(sourceSets.main.get().resources.srcDirs) {
            filter(ReplaceTokens::class, mapOf("tokens" to mapOf("version" to project.version.toString())))
            filteringCharset = "UTF-8"
        }
    }
}
