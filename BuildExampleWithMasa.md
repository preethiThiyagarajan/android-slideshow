# Introduction #

Masa (http://code.google.com/p/masa) is a set of [Maven](http://maven.apache.org/) plugins that enable maven builds of android projects.


# Details #

The SlideShow example in this project comes with a pom.xml that requires the installation of masa to successfully build in maven.  For more information about setting up masa see http://code.google.com/p/masa/wiki/GettingStarted.

The primary motivation for masa in this project is to deploy junit tests to the emulator.  Unfortunately, this isn't currently exposed in the pom - but realizing this goal is [next on the project roadmap](http://code.google.com/p/android-slideshow/wiki/Version1_0Milestones).  It suffices to say the 0.6 release has gone through sufficient manual testing for general use.

# Instructions #

## Install Masa and Put Android SDK 1.0 JAR (android.jar) in Local Repository ##

  * [Install masa](http://code.google.com/p/masa/wiki/GettingStarted).
  * Copy the Android SDK 1.0 jar to your local maven repo.
```
cp /path/to/android_sdk/android.jar /path/to/maven/local/maven/repo/android/android/1.0/android-1.0.jar
```
  * Make a folder in your local Maven repo for the SDK 1.0 android.jar.
```
mkdir -p /path/to/local/maven/repo/android/android/1.0
```

## Build Using Maven ##
To build the SlideShowExample project and deploy it to the emulator using maven:
  1. Remove R.java from the project in the package com.freebeachler.android.component.slideshow.example.  As of this writing it will cause a conflict because maven is building R.java in target/generated-sources which is causing a conflict with Eclipse's default build settings.  Be sure to disable "Build Automatically" for your project/workspace in Eclipse if you plan on using maven as the primary build tool.
  1. Open a terminal or command prompt.
  1. cd /path/to/your/slideshow/example/project (where the project's AndroidManifest.xml lives)
  1. Build the project using maven.
  1. Install the resulting apk on a running emulator.

A quick way to perform the last step is to enter the following in a terminal:
```
mvn install -Dmasa.debug
```