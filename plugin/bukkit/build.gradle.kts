allprojects {
    dependencies {
        compileOnly(project(":api"))
        compileOnly(project(":common"))

        compileOnly("${rootProject.properties["server.paper"]}")
        compileOnly("${rootProject.properties["libs.cc_scheduler"]}")

        compileOnly("${rootProject.properties["libs.log4j_core"]}") {
            exclude("org.apache.logging.log4j")
        }
        compileOnly("${rootProject.properties["libs.fastjson"]}")
        compileOnly("${rootProject.properties["libs.lettuce"]}")
        compileOnly("${rootProject.properties["libs.exp4j"]}")

        compileOnly("${rootProject.properties["libs.packetevents"]}") {
            exclude("net.kyori")
        }
    }
}

dependencies {
    api(project(":plugin:bukkit:api_bukkit"))
    api(project(":plugin:bukkit:common_bukkit"))
    api(project(":plugin:bukkit:compatibility"))
    api(project(":plugin:bukkit:modules"))

    compileOnly("${rootProject.properties["libs.mhdf_database_api"]}") {
        exclude("org.slf4j")
    }
    compileOnly("${rootProject.properties["libs.mhdf_database_mysql"]}")
    compileOnly("${rootProject.properties["libs.mhdf_database_h2"]}")

    compileOnly("${rootProject.properties["plugin.vault"]}") {
        exclude("org.bukkit")
    }
}