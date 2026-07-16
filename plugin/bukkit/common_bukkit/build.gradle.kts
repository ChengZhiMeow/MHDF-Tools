dependencies {
    compileOnly(project(":plugin:bukkit:api_bukkit"))
    compileOnly(project(":plugin:bukkit:compatibility"))

    api("${rootProject.properties["libs.cc_action"]}")
    api("${rootProject.properties["libs.cc_condition"]}")
    api("${rootProject.properties["libs.nyana_message"]}")
    api("${rootProject.properties["libs.nyana_nbt_tag"]}")
    api("${rootProject.properties["libs.nyana_nbt_codec"]}")
}
