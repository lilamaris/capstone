plugins {
    id("capstone.spring.security")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity:identity-auth:identity-auth-application"))
    implementation(project(":identity:identity-auth:identity-auth-domain"))
    implementation(project(":identity:identity-core"))

    implementation(libs.spring.boot.starter.oauth2.client)
    implementation(libs.tools.jackson.databind)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
}