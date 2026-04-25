plugins {
    base
}

group = "com.lilamaris.capstone"
version = "0.0.1-SNAPSHOT"

val modules = listOf(
    "domain",
    "application",
    "web-mvc",
    "persistence"
)

tasks.named("check") {
    dependsOn(
        modules.map { ":timeline:timeline-$it:unitTest" }
    )
}

tasks.register("unitTest") {
    group = "verification"
    description = "Runs all timeline unit tests"

    dependsOn(
        modules.map { ":timeline:timeline-$it:unitTest" }
    )
}
