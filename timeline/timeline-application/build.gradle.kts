plugins {
    id("capstone.application-base")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":timeline:timeline-domain"))
}