plugins {
    id("capstone.layer.persistence")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":timeline:timeline-application"))
    implementation(project(":timeline:timeline-domain"))
}