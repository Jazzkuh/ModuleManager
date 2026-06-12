allprojects {
    group = "com.jazzkuh.modulemanager"
    version = "1.0-SNAPSHOT"
}

subprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
}
