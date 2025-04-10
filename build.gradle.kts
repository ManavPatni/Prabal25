// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.4.1")
    }
}


plugins {
    id("com.android.application") version "8.8.0" apply false
    id("com.android.library") version "8.8.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false

    //crashlytics
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}