plugins {
    id("capstone.layer.launcher")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":academic-catalog:academic-catalog-domain"))
    implementation(project(":academic-catalog:academic-catalog-application"))
    implementation(project(":academic-catalog:academic-catalog-webmvc"))
    implementation(project(":academic-catalog:academic-catalog-persistence"))
}
