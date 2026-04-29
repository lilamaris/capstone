plugins {
    id("capstone.spring.platform")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("implementation", libs.findLibrary("jakarta-persistence-api").get())
    add("testImplementation", libs.findLibrary("jakarta-persistence-api").get())
}