plugins {
    id("capstone.base.module")
    id("java-test-fixtures")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    testImplementation(project(":kernel:kernel-test-support"))
}