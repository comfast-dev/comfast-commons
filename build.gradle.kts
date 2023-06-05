plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    signing

    id("io.freefair.lombok") version "8.0.1"
}

version = "0.2"

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    compileOnly("org.jetbrains:annotations:24.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("file.encoding", "UTF-8")
}


/// COMMON
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

//    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    mavenLocal()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8" //required to properly encode/decode NonASCII characters
//    options.isWarnings = true
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.comfast"
            from(components["java"])

            versionMapping {
                usage("java-api") { fromResolutionOf("runtimeClasspath") }
                usage("java-runtime") { fromResolutionResult() }
            }
        }
    }
    repositories {
        mavenLocal()
    }
}