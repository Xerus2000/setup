import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.2.41"
}

group = "xerus.setup"

java.sourceSets["main"].java.srcDir("src")

application {
    mainClassName = "xerus.setup.MainKt"
}

repositories {
    jcenter()
    maven { setUrl("https://github.com/javaterminal/terminalfx/raw/master/releases") }
    maven { setUrl("https://github.com/javaterminal/pty4j/raw/master/releases") }
    maven { setUrl("http://www.sparetimelabs.com/maven2") }
}

dependencies {
    compile("xerus.util", "javafx")
    
    compile("com.asciidocfx", "terminalfx", "1.3") {
        exclude("com.asciidocfx.pty4j", "pty4j")
    }
    compile("com.kodedu.pty4j", "pty4j", "0.7.4") {
        exclude("com.sparetimelabs:purejavacomm:0.0.17")
    }
    compile("com.sparetimelabs:purejavacomm:0.0.17")
    
    compile(files("PreferencesFX-1.3.0-SNAPSHOT.jar"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
