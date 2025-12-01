repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
}

dependencies {
    compileOnly("${rootProject.properties["plugin.placeholderapi"]}") {
        exclude("net.kyori")
        exclude("org.bstats")
    }
}