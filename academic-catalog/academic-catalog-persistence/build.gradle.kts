plugins {
    id("capstone.layer.persistence")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":academic-catalog:academic-catalog-application"))
    implementation(project(":academic-catalog:academic-catalog-domain"))
}