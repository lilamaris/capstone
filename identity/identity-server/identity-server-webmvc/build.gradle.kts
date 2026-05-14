plugins {
    id("capstone.layer.webmvc")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":bootstrap:webmvc-starter"))

    implementation(project(":identity:identity-server:identity-server-application"))
    implementation(project(":identity:identity-server:identity-server-domain"))
    implementation(project(":identity:identity-core"))

    implementation(libs.nimbus.jose.jwt)
    compileOnly(libs.spring.security.core)

    testImplementation(libs.spring.boot.starter.security)
}
