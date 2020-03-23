import com.moowork.gradle.node.npm.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("org.siouan.frontend") version "1.3.0"
    id("com.github.node-gradle.node") version "2.2.0"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
}

javafx {
    // will pull in transitive modules
    modules("javafx.controls", "javafx.fxml") // replace with what you modules need

    // another option is to use:
    // modules = listOf("javafx.controls", "javafx.fxml")

    version = "12.0.1" // or whatever version you're using
}

group = "com.github.chr1stian"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

frontend.nodeVersion.set("13.0.6")

tasks.register<Copy>("processFrontendResources") {
    description = "Process frontend resources"
    from("../master-app/build")
    into("${project.buildDir}/resources/main")
    dependsOn(tasks.named("assembleFrontend"))
}

tasks.named("processResources").configure {
    dependsOn(tasks.named("processFrontendResources"))
}


// Task for installing frontend dependencies in web
val installDependencies by tasks.registering(NpmTask::class) {
    setArgs(listOf("install"))
    setExecOverrides(closureOf<ExecSpec> {
        setWorkingDir("../masterapp")
    })
}

// Task for executing build:gradle in web
val buildWeb by tasks.registering(NpmTask::class) {
    // Before buildWeb can run, installDependencies must run
    dependsOn(installDependencies)

    setArgs(listOf("run", "build:gradle"))
    setExecOverrides(closureOf<ExecSpec> {
        setWorkingDir("../masterapp")
    })
}


// Before build can run, buildWeb must run
tasks.build {
    dependsOn(buildWeb)
}