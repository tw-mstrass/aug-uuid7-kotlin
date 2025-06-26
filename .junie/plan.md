# Kotlin JVM Project Setup

This document outlines the setup for a basic Kotlin JVM project using Gradle.

## Project Requirements

- Kotlin 2.2.0
- JVM 21
- Kotest 6.0.0.M4 for testing
- Gradle 8.14.2

## Setup Plan

1. [x] Create a basic Gradle project structure
   - [x] Create build.gradle.kts with Kotlin 2.2.0 and JVM 21 configuration
   - [x] Create settings.gradle.kts to set project name to aug-uuid7
   - [x] Create basic directory structure (src/main/kotlin, src/test/kotlin)

2. [x] Configure Gradle dependencies
   - [x] Add Kotest 6.0.0.M4 for testing

3. [x] Create a simple Main.kt file with a "Hello, World!" program

4. [x] Verify the project builds correctly

5. [x] Upgrade Gradle to version 8.14.2
   - [x] Update the Gradle version in gradle/wrapper/gradle-wrapper.properties
   - [x] Run the Gradle wrapper task to update the wrapper files
   - [x] Verify the project builds correctly with the new Gradle version

6. [x] Change the group and package to mjs.uuid7
   - [x] Update the group ID in build.gradle.kts
   - [x] Update the mainClass in build.gradle.kts
   - [x] Create new directory structure for the new package
   - [x] Create Main.kt in the new package with updated package declaration
   - [x] Remove the old Main.kt file
   - [x] Verify the project builds and runs correctly with the new package structure

7. [x] Move dependencies to a standard version management system
   - [x] Define version constants in build.gradle.kts
   - [x] Update dependencies to use the version constants
   - [x] Update JVM toolchain to use the version constant
   - [x] Verify the project builds correctly with the new dependency management

8. [x] Move dependencies to a standard libs.versions.toml file for plugins and libraries
   - [x] Create a gradle/libs.versions.toml file with versions, libraries, and plugins sections
   - [x] Update settings.gradle.kts to enable and configure the version catalog
   - [x] Update build.gradle.kts to use the version catalog
   - [x] Verify the project builds correctly with the version catalog

   Note: The build.gradle.kts file has been fixed to use the version catalog correctly. The file now uses:

   1. `alias(libs.plugins.kotlin.jvm)` in the plugins block
   2. `libs.kotest.runner`, `libs.kotest.assertions`, and `libs.kotest.property` in the dependencies block
   3. A hardcoded value for the JVM version in the kotlin block

   The project now builds correctly with the version catalog.

9. [x] Remove the buildSrc directory
   - [x] Examine the buildSrc directory and its contents
   - [x] Verify that buildSrc is not being used in the project
   - [x] Delete the buildSrc directory and all its contents
   - [x] Verify that the project still builds correctly after removal

# UUID v7 Implementation

Remember to follow the instructions in guidelines.md, especially about writing simple tests first.

1. Create an object called Uuid7 with a generate() function that returns an instance of kotlin.uuid.Uuid.

   [x] It returns a Uuid instance that is non-zero
   [x] It returns a Uuid instance that parses correctly
