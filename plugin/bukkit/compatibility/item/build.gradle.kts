subprojects {
    dependencies {
        compileOnly(project(":plugin:bukkit:compatibility:item"))
    }
}

allprojects {
    dependencies {
        compileOnly(project(":common"))
    }
}
