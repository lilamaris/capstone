plugins {
    id("capstone.base.module")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("implementation", platform(libs.findLibrary("spring-boot-dependencies").get()))
    add("implementation", libs.findLibrary("slf4j-api").get())

    add("testImplementation", platform(libs.findLibrary("spring-boot-dependencies").get()))
}