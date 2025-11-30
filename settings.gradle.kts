rootProject.name = "MHDF-Tools"

include(":api")
include(":common")

include(":plugin:bukkit")

include(":plugin:bukkit:compatibility")
include(":plugin:bukkit:compatibility:item")
include(":plugin:bukkit:compatibility:item:craftengine")
include(":plugin:bukkit:compatibility:item:mythicmobs")

include(":plugin:bukkit:modules")

include(":plugin:bungee")
include(":plugin:velocity")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}