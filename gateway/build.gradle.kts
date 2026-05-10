plugins {
    id("capstone.layer.launcher")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.starter.gateway.server.webflux)
}
