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
            name = "chengzhimeow-maven-repo"
            url = uri("https://maven.chengzhimeow.cn/releases")
            credentials(PasswordCredentials::class)
            authentication { create<BasicAuthentication>("basic") }
        }
    }
}