plugins {
    id("capstone.module-base")
    id("capstone.logging-base")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("api", libs.findLibrary("jakarta-persistence-api").get())
}
