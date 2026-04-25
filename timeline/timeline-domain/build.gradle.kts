plugins {
    id("capstone.domain-base")
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(libs.jakarta.persistence.api)
}