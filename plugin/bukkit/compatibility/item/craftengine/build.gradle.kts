repositories {
    maven("https://repo.momirealms.net/releases")
}

dependencies {
    compileOnly("${rootProject.properties["plugin.craftengine_core"]}")
    compileOnly("${rootProject.properties["plugin.craftengine_bukkit"]}")
}