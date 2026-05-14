plugins {
    id("capstone.spring.security")
    id("capstone.spring.autoconfigure")
    id("capstone.base.library")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity:identity-client-starter"))
    api(project(":identity:identity-core"))
}