plugins {
    id("org.springframework.boot")
    id("capstone.spring.observability")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("implementation", libs.findLibrary("spring-boot").get())
    add("implementation", libs.findLibrary("spring-boot-autoconfigure").get())
}
