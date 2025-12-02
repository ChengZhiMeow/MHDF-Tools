repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
}

dependencies {
    compileOnly(project(":common"))
    compileOnly("${rootProject.properties["plugin.placeholderapi"]}")
}