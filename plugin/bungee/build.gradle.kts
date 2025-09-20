dependencies {
    implementation(project(":common"))

    compileOnly("${rootProject.properties["server.bungeecord"]}")
    compileOnly("${rootProject.properties["libs.fastjson"]}")
}