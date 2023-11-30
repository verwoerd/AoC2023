
buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
  }
}
rootProject.name = "aoc-template"
include(
  "shared"
         )
