plugins {
    id("capstone.layer.application")
    id("capstone.spring.autoconfigure")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity:identity-server:identity-server-domain"))

    implementation(project(":identity:identity-core"))
    implementation(project(":identity:identity-server:identity-contract"))

    implementation(project(":kernel:kernel-core"))

    implementation(libs.spring.security.crypto)
    implementation(libs.spring.security.jose)

    testImplementation(project(":kernel:kernel-test-support"))
}
