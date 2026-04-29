plugins {
    id("capstone.spring.platform")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("implementation", libs.findLibrary("spring-boot-starter-webmvc").get())
    add("implementation", libs.findLibrary("spring-boot-starter-validation").get())
    add("implementation", libs.findLibrary("springdoc-openapi-starter-webmvc-ui").get())

    add("testImplementation", libs.findLibrary("spring-boot-starter-webmvc-test").get())
    add("testImplementation", libs.findLibrary("spring-security-test").get())
}