plugins {
    id("capstone.spring.persistence")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("runtimeOnly", libs.findLibrary("postgresql").get())
}