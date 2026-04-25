plugins {
    id("capstone.persistence-base")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":timeline:timeline-application"))
    implementation(project(":timeline:timeline-domain"))
}