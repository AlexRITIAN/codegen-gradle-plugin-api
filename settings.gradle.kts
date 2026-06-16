dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from("io.github.alexritian:catalog:0.0.8")
        }
    }
}

rootProject.name = "codegen-gradle-plugin-api"
