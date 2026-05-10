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
include("identity:identity-client-starter")
include("identity:identity-server")
include("identity:identity-server:identity-contract")
include("identity:identity-server:identity-server-domain")
include("identity:identity-server:identity-server-application")
include("identity:identity-server:identity-server-webmvc")
include("identity:identity-server:identity-server-security")
include("identity:identity-server:identity-server-persistence")
include("identity:identity-server:identity-server-launcher")

include("timeline")
include("timeline:timeline-domain")
include("timeline:timeline-application")
include("timeline:timeline-webmvc")
include("timeline:timeline-persistence")
include("timeline:timeline-launcher")
include("gateway")