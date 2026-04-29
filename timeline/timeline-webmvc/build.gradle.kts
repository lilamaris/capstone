plugins {
    id("capstone.layer.webmvc")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":timeline:timeline-domain"))
    implementation(project(":timeline:timeline-application"))
}