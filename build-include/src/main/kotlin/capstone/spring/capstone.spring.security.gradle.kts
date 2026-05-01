plugins {
    id("capstone.spring.platform")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("implementation", libs.findLibrary("spring-boot-starter-security").get())
    add("implementation", libs.findLibrary("spring-boot-starter-oauth2-resource-server").get())
    add("compileOnly", libs.findLibrary("jakarta-servlet-api").get())

    add("testImplementation", libs.findLibrary("spring-boot-starter-test").get())
    add("testImplementation", libs.findLibrary("spring-security-test").get())
    add("testImplementation", libs.findLibrary("jakarta-servlet-api").get())
}
