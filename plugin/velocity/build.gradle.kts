dependencies {
    implementation(project(":common"))

    compileOnly("${rootProject.properties["server.velocity"]}")
    compileOnly("${rootProject.properties["libs.fastjson"]}")
}