rootProject.name = "MHDF-Tools"

include("api")
include("common")

include("plugin:bukkit")
include("plugin:bungee")
include("plugin:velocity")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}