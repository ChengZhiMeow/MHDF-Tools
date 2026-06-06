plugins {
    id("maven-publish")
}

publishing {
    repositories {
        maven {
            name = "nachorealms-repository-releases"
            url = uri("https://repo-eo.catnies.top/releases")
            credentials(PasswordCredentials::class)
            authentication { create<BasicAuthentication>("basic") }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = "MHDF-Tools-API-bukkit"
        }
    }
}
