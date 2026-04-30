plugins {
    id("capstone.base.library")
    id("java-test-fixtures")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":kernel:kernel-core"))
    testImplementation(project(":kernel:kernel-test-support"))
}
