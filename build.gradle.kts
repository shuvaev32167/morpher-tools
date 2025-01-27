plugins {
    java
    kotlin("jvm") version "2.1.10"
    `java-library`
     `maven-publish`
    id("org.gradlex.extra-java-module-info") version "1.9"
}

group = "ru.shuvaev.morpher"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
//    maven("https://raw.github.com/morpher-ru/morpher-ws3-java-client/mvn-repo")
}



publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

java{
    withSourcesJar()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.jetbrains.annotations)
    implementation(libs.petrovich4j)
//    implementation(libs.aot)
//    implementation("ru.morpher:ws3.client:1.0-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

//application{
//    mainModule = "ru.shuvaev.morpher.tools"
//    mainClass = "ru.shuvaev.morpher.tools.MainKt"
//}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

extraJavaModuleInfo {
    module("com.github.petrovich4j:petrovich4j", "petrovich4j"){
        exports("com.github.petrovich4j")
    }
//    automaticModule("com.github.petrovich4j:petrovich4j", "petrovich4j")
//    automaticModule("com.github.demidko:aot", "aot")
//    automaticModule("com.github.demidko:aot-bytecode", "aot.bytecode")
//    automaticModule("com.github.demidko:bits", "bits")
//    module("com.github.demidko:aot", "aot"){
//        exports("com.github.demidko.aot")
//        opens("com.github.demidko.aot")
//
//        requires("aot.bytecode")
//        requires("bits")
//    }
//
//    module("com.github.demidko:aot-bytecode", "aot.bytecode"){
//        opens("com.github.demidko.aot")
//    }
//
//    module("com.github.demidko:bits", "bits"){
//    }
}

//sourceSets.main {
//    java.srcDirs("src/main/java", "src/main/kotlin")
//}
//
//val moduleName = "ru.shuvaev.morpher.tools"
//
//tasks.compileJava {
//        inputs.property("moduleName", moduleName)
//        doFirst {
//            options.compilerArgs = listOf(
//                "--module-path", classpath.asPath,
//                "--patch-module", "$moduleName=${sourceSets["main"].output.asPath}"
//            )
//            classpath = files()
//        }
//}
