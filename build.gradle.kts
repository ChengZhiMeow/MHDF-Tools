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
        maven("https://jitpack.io")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.21")

        compileOnly(platform("${rootProject.properties["libs.adventure_bom"]}"))
        compileOnly("${rootProject.properties["libs.adventure_api"]}")
        compileOnly("${rootProject.properties["libs.adventure_text_minimessage"]}")
        compileOnly("${rootProject.properties["libs.adventure_text_serializer_gson"]}")
        compileOnly("${rootProject.properties["libs.adventure_text_serializer_legacy"]}")
        compileOnly("${rootProject.properties["libs.adventure_text_serializer_plain"]}")

        compileOnly("${rootProject.properties["libs.lombok"]}")
        annotationProcessor("${rootProject.properties["libs.lombok"]}")
    }

    tasks {
        processResources {
            filesMatching("**/*.yml") {
                val props = mutableMapOf<String, Any?>()
                project.properties.forEach { (key, value) ->
                    key.split(".").let { keys ->
                        keys.dropLast(1).fold(props) { map, k ->
                            map.getOrPut(k) { mutableMapOf<String, Any?>() } as MutableMap<String, Any?>
                        }[keys.last()] = value
                    }
                }

                filter { it.replace(Regex("""\$(?!\{)"""), """\$""") }
                expand(props)
            }
        }
    }
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))

    implementation(project(":plugin:bukkit"))
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

        mutableListOf(
            "org.h2",
            "com.mysql",
            "cn.chengzhimeow.ccscheduler",
            "cn.chengzhimeow.ccyaml",
            "cn.chengzhimeow.ccaction",
            "cn.chengzhimeow.cccondition",
            "cn.chengzhiya",
            "com.alibaba",
            "org.reflections",
            "com.github.retrooper",
            "io.github.retrooper",
            "com.j256.ormlite",
            "com.zaxxer",
            "io.lettuce",
            "net.objecthunter",
            "org.intellij",
            "org.jetbrains",
            "org.yaml"
        ).forEach { relocate(it, "cn.chengzhimeow.mhdftools.libs.$it") }
    }

    jar {
        dependsOn(shadowJar)
    }

    runServer {
        dependsOn(build)
        minecraftVersion("1.21.4")
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
