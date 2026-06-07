dependencies {
    compileOnly("${rootProject.properties["plugin.vault"]}") {
        exclude("org.bukkit")
    }
}
