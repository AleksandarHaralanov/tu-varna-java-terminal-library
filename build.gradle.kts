plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("de.codeshelf.consoleui:consoleui:0.0.13")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.pty4j:purejavacomm:0.0.11.1")
    implementation("org.jetbrains.pty4j:pty4j:0.12.26")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("org.slf4j:slf4j-log4j12:2.0.13")
    implementation("org.jetbrains.jediterm:jediterm-pty:2.42")
    implementation("com.google.guava:guava:33.2.0-jre")
    implementation("commons-io:commons-io:2.16.1")
}

tasks.jar {
    manifest {
        attributes(
                "Implementation-Title" to "Gradle",
                "Implementation-Version" to archiveVersion,
                "Main-Class" to "me.beezle.library.Main"
        )
    }
}

