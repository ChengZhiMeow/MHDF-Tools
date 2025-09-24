repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
    maven("https://mvn.lumine.io/repository/maven-public")
    maven("https://repo.momirealms.net/releases")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))

    compileOnly("${rootProject.properties["libs.cc_scheduler"]}")
    compileOnly("${rootProject.properties["libs.mhdf_langutil"]}") {
        exclude("com.alibaba.fastjson2")
    }

    compileOnly("${rootProject.properties["libs.mhdf_database_api"]}") {
        exclude("org.slf4j")
    }
    compileOnly("${rootProject.properties["libs.mhdf_database_mysql"]}")
    compileOnly("${rootProject.properties["libs.mhdf_database_h2"]}")

    compileOnly("${rootProject.properties["libs.log4j_core"]}") {
        exclude("org.apache.logging.log4j")
    }
    compileOnly("${rootProject.properties["libs.fastjson"]}")
    compileOnly("${rootProject.properties["libs.reflections"]}") {
        exclude("org.slf4j")
    }
    compileOnly("${rootProject.properties["libs.lettuce"]}")
    compileOnly("${rootProject.properties["libs.exp4j"]}")
    compileOnly("${rootProject.properties["libs.packetevents"]}") {
        exclude("net.kyori")
    }

    compileOnly("${rootProject.properties["plugin.placeholderapi"]}") {
        exclude("net.kyori")
        exclude("org.bstats")
    }
    compileOnly("${rootProject.properties["plugin.mythicmobs"]}")
    compileOnly("${rootProject.properties["plugin.vault"]}") {
        exclude("org.bukkit")
    }

    compileOnly("${rootProject.properties["plugin.craftengine_core"]}")
    compileOnly("${rootProject.properties["plugin.craftengine_bukkit"]}")
}