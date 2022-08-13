plugins {
    id("fabric-loom") version "0.11-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "+"
    id("maven-publish")
    id("org.quiltmc.quilt-mappings-on-loom") version "4.0.0"
    id("org.ajoberstar.grgit") version "5.0.0-rc.3"
}

base {
    archivesBaseName = "mcxr-desktop"
}

version = "${properties["desktop_version"].toString()}+mc${properties["minecraft_version"].toString()}"
group = properties["maven_group"].toString()

repositories {
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    include(implementation(project(path = ":mcxr-core", configuration = "namedElements"))!!)
    include(implementation(project(path = ":mcxr-play", configuration = "namedElements"))!!)

    minecraft("com.mojang:minecraft:${properties["minecraft_version"].toString()}")
    mappings(loom.layered {
        this.addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:${properties["minecraft_version"].toString()}+build.${properties["quilt_mappings"].toString()}:v2"))
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"].toString()}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"].toString()}")

    implementation("org.joml:joml:${properties["joml_version"].toString()}")
    implementation("com.electronwill.night-config:core:${properties["night_config_version"].toString()}")
    implementation("com.electronwill.night-config:toml:${properties["night_config_version"].toString()}")
}

sourceSets {
    main {
        resources {
            srcDir("loader")
        }
    }
}

tasks {
    processResources {
        val desktopVersion = project.properties["desktop_version"].toString();
        inputs.property("desktop_version", desktopVersion)

        filesMatching("fabric.mod.json") {
            expand("desktop_version" to desktopVersion)
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
        create("mcxrDesktop") {
            client()
            configName = "MCXR Desktop"
            ideConfigGenerated(true)
        }
    }
}
