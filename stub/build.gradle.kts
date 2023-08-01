plugins {
    id("com.android.application")
    id("org.lsposed.lsparanoid")
}

lsparanoid {
    seed = if (RAND_SEED != 0) RAND_SEED else null
    includeDependencies = true
    global = true
}

android {
    namespace = "com.topjohnwu.liorsmagic"

    val canary = !Config.version.contains(".")

    val url = if (canary) null
    else "https://cdn.jsdelivr.net/gh/topjohnwu/liorsmagic-files@${Config.version}/app-release.apk"

    defaultConfig {
        applicationId = "com.topjohnwu.liorsmagic"
        versionCode = 1
        versionName = "1.0"
        buildConfigField("int", "STUB_VERSION", Config.stubVersion)
        buildConfigField("String", "APK_URL", url?.let { "\"$it\"" } ?: "null" )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles("proguard-rules.pro")
        }
    }
}

setupStub()

dependencies {
    implementation(project(":app:shared"))
}
