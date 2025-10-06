// 插件
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("com.gradleup.shadow") version "9.0.0-beta6"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17" apply false
}

// 统一项目配置
allprojects {
    // 应用插件到子项目
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "cn.chengzhimeow"
    version = "${rootProject.properties["project.version"]}"

    // 设置项目JDK版本
    kotlin.jvmToolchain(21)
    java.sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public")
        maven("https://oss.sonatype.org/content/groups/public")
        maven("https://repo.codemc.io/repository/maven-releases")
        maven("https://repo.codemc.io/repository/maven-snapshots")
        maven("https://repo.catnies.top/releases")
        maven("https://maven.chengzhimeow.cn/releases")
        maven("https://jitpack.io")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.21")
        compileOnly("${rootProject.properties["server.paper"]}")

        compileOnly("${rootProject.properties["libs.lombok"]}")
        annotationProcessor("${rootProject.properties["libs.lombok"]}")
    }

    tasks {
        processResources {
            filesMatching("**/*.yml") {
                val expansionProps = mutableMapOf<String, Any?>()

                project.properties.forEach { (k, v) ->
                    if (k.contains(".")) {
                        val args = k.split(".")

                        var currentMap = expansionProps
                        args.dropLast(1).forEach {
                            currentMap = currentMap
                                .getOrPut(it) { mutableMapOf<String, Any>() } as MutableMap<String, Any?>
                        }

                        currentMap[args.last()] = v
                    } else expansionProps[k] = v
                }

                expand(expansionProps)
            }
        }
    }
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))

    implementation(project(":plugin:bukkit"))
    implementation(project(":plugin:bungee"))
    implementation(project(":plugin:velocity"))
}

// 任务配置
tasks {
    clean {
        delete("$rootDir/target")
    }

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}-all.jar")
//        destinationDirectory.set(file("C:\\Users\\ChengZhiYa\\Desktop\\momi\\plugins"))
        destinationDirectory.set(file("$projectDir/target"))

        exclude("META-INF/")

        relocate("org.h2", "cn.chengzhimeow.mhdftools.libs.org.h2")
        relocate("com.mysql", "cn.chengzhimeow.mhdftools.libs.com.mysql")

        relocate("cn.chengzhimeow.ccyaml", "cn.chengzhimeow.mhdftools.libs.cn.chengzhimeow.ccyaml")
        relocate("cn.chengzhiya", "cn.chengzhimeow.mhdftools.libs.cn.chengzhiya")

        relocate("com.alibaba", "cn.chengzhimeow.mhdftools.libs.com.alibaba")
        relocate("org.reflections", "cn.chengzhimeow.mhdftools.libs.org.reflections")

        relocate("com.github.retrooper", "cn.chengzhimeow.mhdftools.libs.com.github.retrooper")
        relocate("io.github.retrooper", "cn.chengzhimeow.mhdftools.libs.io.github.retrooper")

        relocate("com.j256.ormlite", "cn.chengzhimeow.mhdftools.libs.com.j256.ormlite")
        relocate("com.zaxxer", "cn.chengzhimeow.mhdftools.libs.com.zaxxer")

        relocate("io.lettuce", "cn.chengzhimeow.mhdftools.libs.io.lettuce")

        relocate("net.objecthunter", "cn.chengzhimeow.mhdftools.libs.net.objecthunter")

        relocate("org.intellij", "cn.chengzhimeow.mhdftools.libs.org.intellij")
        relocate("org.jetbrains", "cn.chengzhimeow.mhdftools.libs.org.jetbrains")
        relocate("org.yaml", "cn.chengzhimeow.mhdftools.libs.org.yaml")
    }

    jar {
        dependsOn(shadowJar)
    }

    runServer {
        dependsOn(build)
        minecraftVersion("1.21.8")
    }
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }

    jvmArgs("-Ddisable.watchdog=true")
    jvmArgs("-Xlog:redefine+class*=info")
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}

