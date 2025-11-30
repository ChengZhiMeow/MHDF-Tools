subprojects {
    dependencies {
        compileOnly(project(":plugin:bukkit:compatibility:placeholder"))
    }
}

allprojects {
    dependencies {
        compileOnly(project(":common"))
    }
}
