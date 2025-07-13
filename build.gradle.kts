// Top-level build file

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // ✅ JitPack added
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.google.gms.google-services") version "4.4.3" apply false
}
