plugins {
    id("capstone.spring.module")
    id("capstone.spring.autoconfigure")
    id("java-library")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":identity:identity-core"))

    implementation(libs.spring.security.jose)
    compileOnly(libs.jakarta.servlet.api)

    testImplementation(project(":kernel:kernel-test-support"))
    testImplementation(libs.jakarta.servlet.api)
}
