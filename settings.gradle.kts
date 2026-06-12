dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "ModuleManager"

include("common")
include("spigot")
include("velocity")
include("minestom")
include("jda")
include("generic")
