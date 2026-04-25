plugins {
    id("capstone.module-base")
    id("capstone.logging-base")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

dependencies {
    add("implementation", libs.findLibrary("spring-boot-starter-webmvc").get())
    add("implementation", libs.findLibrary("spring-boot-starter-validation").get())
    add("testImplementation", libs.findLibrary("spring-boot-starter-test").get())
}
