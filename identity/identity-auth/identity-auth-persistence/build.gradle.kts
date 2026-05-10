plugins {
    id("capstone.spring.persistence")
    id("capstone.spring.autoconfigure")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity:identity-auth:identity-auth-application"))
    implementation(project(":identity:identity-auth:identity-auth-domain"))
    implementation(project(":identity:identity-core"))
    implementation(project(":kernel:kernel-core"))
}