repositories {
    maven("https://mvn.lumine.io/repository/maven-public")
}

dependencies {
    compileOnly(project(":plugin:bukkit:compatibility:item"))

    compileOnly("${rootProject.properties["plugin.mythicmobs"]}")
}