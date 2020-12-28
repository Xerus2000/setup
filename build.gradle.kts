import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.72"
}

group = "xerus.setup"

sourceSets["main"].java.srcDir("src")

application {
    mainClassName = "xerus.setup.MainKt"
}

repositories {
    jcenter()
    maven("https://github.com/javaterminal/terminalfx/raw/master/releases")
    maven("http://www.sparetimelabs.com/maven.")
    maven("https://jitpack.io")
}

dependencies {
    compile("com.github.xerus2000.util", "javafx", "master-SNAPSHOT")
    
    compile("com.asciidocfx", "terminalfx", "1.3") {
    
    compile(files("PreferencesFX-1.3.0-SNAPSHOT.jar"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
