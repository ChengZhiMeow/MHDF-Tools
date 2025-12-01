subprojects {
    dependencies {
        compileOnly(project(":plugin:bukkit:modules:impl"))
    }
}

allprojects {
    dependencies {
        compileOnly(project(":plugin:bukkit:api_bukkit"))
        compileOnly(project(":plugin:bukkit:compatibility"))
    }
}