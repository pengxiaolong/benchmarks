plugins {
    id("java")
    id("io.github.reyerizo.gradle.jcstress") version "0.8.15"
    id("me.champeau.jmh") version "0.7.2"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    jmh("org.openjdk.jmh:jmh-core:1.36")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.36")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")
}



tasks.test {
    useJUnitPlatform()
}

tasks.build {
    doLast {
        tasks.jmhJar
    }
}