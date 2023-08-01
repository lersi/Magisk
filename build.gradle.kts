plugins {
    id("LiorsmagicPlugin")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
