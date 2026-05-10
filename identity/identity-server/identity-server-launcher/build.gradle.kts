plugins {
    id("capstone.layer.launcher")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity:identity-server:identity-server-domain"))
    implementation(project(":identity:identity-server:identity-server-application"))
    implementation(project(":identity:identity-server:identity-server-persistence"))
    implementation(project(":identity:identity-server:identity-server-webmvc"))
    implementation(project(":identity:identity-server:identity-server-security"))
}