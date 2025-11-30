subprojects {
    dependencies {
        compileOnly(project(":plugin:bukkit:modules:impl"))
    }
}

allprojects {
    dependencies {
        compileOnly(project(":api"))
        compileOnly(project(":common"))
        compileOnly(project(":plugin:bukkit:compatibility"))
    }
}