repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
    maven("https://mvn.lumine.io/repository/maven-public")
    maven("https://repo.momirealms.net/releases")
}

dependencies {
    api("${rootProject.properties["libs.mhdf_library"]}")
    api("${rootProject.properties["libs.cc_yaml"]}")
}