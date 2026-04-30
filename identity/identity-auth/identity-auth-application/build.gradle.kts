plugins {
    id("capstone.layer.application")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity:identity-auth:identity-auth-domain"))

    implementation(project(":identity:identity-core"))
    implementation(project(":kernel:kernel-core"))

    implementation(libs.spring.security.crypto)
    implementation(libs.spring.security.jose)

    testImplementation(testFixtures(project(":identity:identity-auth:identity-auth-domain")))
    testImplementation(testFixtures(project(":kernel:kernel-core")))
    testImplementation(project(":kernel:kernel-test-support"))
}