plugins {
    id("capstone.spring.jpa")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("runtimeOnly", libs.findLibrary("h2database").get())
    add("implementation", libs.findLibrary("spring-boot-starter-data-jpa").get())

    add("testRuntimeOnly", libs.findLibrary("h2database").get())
    add("testImplementation", libs.findLibrary("spring-boot-starter-data-jpa-test").get())

    add("testImplementation", platform(libs.findLibrary("testcontainers-bom").get()))
    add("testImplementation", libs.findLibrary("testcontainers-postgresql").get())
    add("testImplementation", libs.findLibrary("testcontainers-jdbc").get())
}