plugins {
    id("capstone.layer.launcher")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity:identity-auth:identity-auth-domain"))
    implementation(project(":identity:identity-auth:identity-auth-application"))
    implementation(project(":identity:identity-auth:identity-auth-persistence"))
    implementation(project(":identity:identity-auth:identity-auth-webmvc"))
    implementation(project(":identity:identity-auth:identity-auth-security"))
}