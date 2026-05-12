plugins {
    id("capstone.layer.application")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":academic-catalog:academic-catalog-domain"))
}