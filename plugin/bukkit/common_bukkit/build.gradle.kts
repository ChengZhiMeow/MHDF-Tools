dependencies {
    compileOnly(project(":plugin:bukkit:api_bukkit"))

    api("${rootProject.properties["libs.cc_action"]}")
    api("${rootProject.properties["libs.cc_condition"]}")
}
