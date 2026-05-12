plugins {
    id("capstone.layer.domain")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    testImplementation(project(":kernel:kernel-test-support"))
}