import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SonatypeHost
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

plugins {
    `java-library`
    signing
    alias(libs.plugins.publish)
}

group = "io.github.alexritian"
version = "0.0.1-SNAPSHOT"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // jooq
    api(libs.bundles.jooq.all)

    implementation(libs.jetbrains.annotations)

    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/AlexRITIAN/codegen-gradle-plugin-runtime")
            credentials(PasswordCredentials::class)
        }
    }
}

mavenPublishing {
    configure(JavaLibrary(
        javadocJar = JavadocJar.Javadoc()
    ))
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(project.group.toString(), project.name, project.version.toString())
    pom {
        name.set("${project.group}:${project.name}")
        description.set("This project provides runtime support libraries required by Codegen-gradle-plugin")
        url.set("https://github.com/AlexRITIAN/codegen-gradle-plugin-runtime")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("AlexRITIAN")
                name.set("Too_Young")
                email.set("yhritianfq@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/AlexRITIAN/codegen-gradle-plugin-runtime")
            connection.set("scm:git:git://github.com/AlexRITIAN/codegen-gradle-plugin-runtime")
            developerConnection.set("scm:git:ssh://git@github.com:AlexRITIAN/codegen-gradle-plugin-runtime.git")
        }
    }
}

val generatePomProperties by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/pomProperties")
    outputs.dir(outputDir)

    doLast {
        val props = Properties().apply {
            setProperty("groupId", project.group.toString())
            setProperty("artifactId", project.name)
            setProperty("version", project.version.toString())
        }

        val outputFile = outputDir.get().file("pom.properties").asFile
        outputDir.get().asFile.mkdirs()
        outputFile.writer().use { writer ->
            props.store(writer, null)
        }
    }
}

tasks.named<Jar>("jar") {
    dependsOn("generatePomFileForMavenPublication")

    from(layout.buildDirectory.file("publications/pluginMaven/pom-default.xml")) {
        into("META-INF/${project.group}")
        rename { "pom.xml" }
    }

    from(generatePomProperties.map { layout.buildDirectory.dir("generated/pomProperties") }) {
        into("META-INF/${project.group}")
    }
}