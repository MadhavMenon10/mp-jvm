plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "mp.xray.Xray"
}

tasks.test {
    testLogging {
        events("passed", "failed")
    }
}
