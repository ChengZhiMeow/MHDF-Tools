repositories {
    maven("https://mvn.lumine.io/repository/maven-public")
}

dependencies {
    compileOnly("${rootProject.properties["plugin.mythicmobs"]}")
}