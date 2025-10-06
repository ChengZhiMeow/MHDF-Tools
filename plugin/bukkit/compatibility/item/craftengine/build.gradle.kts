repositories {
    maven("https://repo.momirealms.net/releases")
}

dependencies {
    compileOnly(project(":plugin:bukkit:compatibility:item"))

    compileOnly("${rootProject.properties["plugin.craftengine_core"]}")
    compileOnly("${rootProject.properties["plugin.craftengine_bukkit"]}")
}