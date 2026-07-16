rootProject.name = "MHDF-Tools"

include(":api")
include(":common")

include(":plugin:bukkit")
include(":plugin:bukkit:api_bukkit")
include(":plugin:bukkit:common_bukkit")

include(":plugin:bukkit:compatibility")
include(":plugin:bukkit:compatibility:item")
include(":plugin:bukkit:compatibility:item:craftengine")
include(":plugin:bukkit:compatibility:item:mythicmobs")
include(":plugin:bukkit:compatibility:packetevents")
include(":plugin:bukkit:compatibility:placeholder")
include(":plugin:bukkit:compatibility:placeholder:placeholderapi")

include(":plugin:bukkit:modules")
include(":plugin:bukkit:modules:impl")

val bukkitModulesProject = project(":plugin:bukkit:modules:impl")
bukkitModulesProject.projectDir.listFiles()?.forEach {
    if (!it.isDirectory) return@forEach
    if (!it.name.startsWith("module_")) return@forEach

    include(":plugin:bukkit:modules:impl:${it.name}")
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}
