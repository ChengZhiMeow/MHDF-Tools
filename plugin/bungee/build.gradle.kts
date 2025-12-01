dependencies {
    compileOnly(project(":common"))

    compileOnly("${rootProject.properties["server.bungeecord"]}")
    compileOnly("${rootProject.properties["libs.fastjson"]}")
}