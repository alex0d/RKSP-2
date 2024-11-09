plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "RKSP2"

include("simple-tasks")
include("rsocket:rsocket-server")
include("rsocket:rsocket-client")
include("fs-app:fs-spring")
