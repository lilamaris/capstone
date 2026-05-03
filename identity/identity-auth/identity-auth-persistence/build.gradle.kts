plugins {
    id("capstone.spring.persistence")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity:identity-auth:identity-auth-application"))
    implementation(project(":identity:identity-auth:identity-auth-domain"))
}