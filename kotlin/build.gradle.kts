plugins {
    kotlin("jvm") version "2.0.21"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass = "mp.xray.Xray"
}

tasks.test {
    testLogging {
        events("passed", "failed")
    }
}
