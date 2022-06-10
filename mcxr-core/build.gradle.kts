plugins {
    id("fabric-loom") version "0.11-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "+"
    id("maven-publish")
    id("org.quiltmc.quilt-mappings-on-loom") version "4.0.0"
    id("org.ajoberstar.grgit") version "5.0.0-rc.3"
}

base {
    archivesBaseName = "mcxr-core"
}

version = "${properties["core_version"].toString()}+${getVersionMetadata()}"
group = properties["maven_group"].toString()

repositories {
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["minecraft_version"].toString()}")
    mappings(loom.layered {
        this.addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:${properties["minecraft_version"].toString()}+build.${properties["quilt_mappings"].toString()}:v2"))
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"].toString()}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"].toString()}")

    modImplementation("com.github.Virtuoel:Pehkui:${properties["pehkui_version"].toString()}") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    include(implementation("org.joml:joml:${properties["joml_version"].toString()}")!!)
    include(implementation("com.electronwill.night-config:core:${properties["night_config_version"].toString()}")!!)
    include(implementation("com.electronwill.night-config:toml:${properties["night_config_version"].toString()}")!!)
}

tasks {
    processResources {
        val playVersion = project.properties["play_version"].toString();
        val coreVersion = project.properties["core_version"].toString();
        inputs.property("play_version", playVersion)
        inputs.property("core_version", coreVersion)

        filesMatching("fabric.mod.json") {
            expand("play_version" to playVersion, "core_version" to coreVersion)
        }
    }

    withType<JavaCompile> {
        options.release.set(17)
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.properties["archivesBaseName"].toString()}" }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mod") {
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}

loom {
    runs {
        create("coreClient") {
            client()
            configName = "MCXR Core Client"
            ideConfigGenerated(true)
        }

        create("coreServer") {
            server()
            configName = "MCXR Core Server"
            ideConfigGenerated(true)
        }
    }
}

fun getVersionMetadata(): String {
    val buildId = System.getenv("GITHUB_RUN_NUMBER")

    // CI builds only
    if (buildId != null) {
        return "build.${buildId}"
    }

    val grgit = extensions.getByName("grgit") as org.ajoberstar.grgit.Grgit;
    val head = grgit.head()
    var id = head.abbreviatedId

    // Flag the build if the build tree is not clean
    if (!grgit.status().isClean) {
        id += "-dirty"
    }

    return "rev.${id}"
}