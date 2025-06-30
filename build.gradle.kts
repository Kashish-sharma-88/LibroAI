// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file

buildscript {
    dependencies {
        classpath(libs.google.services)
    }
    repositories {
        google()
        mavenCentral()
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.google.gms.google-services") version "4.4.3" apply false
}