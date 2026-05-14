plugins {
    id("capstone.spring.webmvc")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    implementation(libs.spring.security.core)
}
