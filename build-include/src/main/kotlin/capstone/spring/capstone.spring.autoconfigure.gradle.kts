plugins {
    id("capstone.spring.platform")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("implementation", libs.findLibrary("spring-boot-autoconfigure").get())
    add("implementation", libs.findLibrary("spring-boot-configuration-processor").get())
    add("implementation", libs.findLibrary("spring-boot-starter-validation").get())
}