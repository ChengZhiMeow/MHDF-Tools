plugins {
    id("maven-publish")
}

dependencies {
    compileOnly("${rootProject.properties["libs.mhdf_database_api"]}")
}

// 发布到 Maven 仓库
publishing {
    repositories {
        maven {
            name = "nachorealms-repository-releases"
            url = uri("https://repo.catnies.top/releases")
            credentials(PasswordCredentials::class)
            authentication { create<BasicAuthentication>("basic") }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = "MHDF-Tools-API"
        }
    }
}