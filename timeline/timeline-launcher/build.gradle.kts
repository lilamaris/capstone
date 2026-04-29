plugins {
    id("capstone.spring.observability")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":timeline:timeline-domain"))
    implementation(project(":timeline:timeline-application"))
    implementation(project(":timeline:timeline-webmvc"))
    implementation(project(":timeline:timeline-persistence"))
}