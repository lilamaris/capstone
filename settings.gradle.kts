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

include("academic-catalog")
include("academic-catalog:academic-catalog-domain")
include("academic-catalog:academic-catalog-application")
include("academic-catalog:academic-catalog-webmvc")
include("academic-catalog:academic-catalog-persistence")
include("academic-catalog:academic-catalog-launcher")
include("gateway")