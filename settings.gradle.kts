pluginManagement {
    includeBuild("build-include")

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenCentral()
    }
}

rootProject.name = "capstone"

include("kernel")
include("kernel:kernel-core")
include("kernel:kernel-test-support")

include("identity")
include("identity:identity-core")
include("identity:identity-auth")
include("identity:identity-auth:identity-auth-domain")
include("identity:identity-auth:identity-auth-application")

include("timeline")
include("timeline:timeline-domain")
include("timeline:timeline-application")
include("timeline:timeline-webmvc")
include("timeline:timeline-persistence")
include("timeline:timeline-launcher")