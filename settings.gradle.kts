rootProject.name = "MHDF-Tools"

include(":api")
include(":common")

include(":plugin:bukkit")
include(":plugin:bukkit:api_bukkit")

include(":plugin:bukkit:compatibility")
include(":plugin:bukkit:compatibility:item")
include(":plugin:bukkit:compatibility:item:craftengine")
include(":plugin:bukkit:compatibility:item:mythicmobs")
include(":plugin:bukkit:compatibility:packetevents")
include(":plugin:bukkit:compatibility:placeholder")
include(":plugin:bukkit:compatibility:placeholder:placeholderapi")

include(":plugin:bukkit:modules")
include(":plugin:bukkit:modules:impl")
include(":plugin:bukkit:modules:impl:module_bungee")
include(":plugin:bukkit:modules:impl:module_crash")
include(":plugin:bukkit:modules:impl:module_hat")
include(":plugin:bukkit:modules:impl:module_knockback")
include(":plugin:bukkit:modules:impl:module_list")

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