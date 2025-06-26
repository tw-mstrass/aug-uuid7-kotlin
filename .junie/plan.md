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

1. [x] Create an object called Uuid7 with a generate() function that returns an instance of java.util.UUID.

   [x] It returns a Uuid instance that is non-zero
   [x] It returns a Uuid instance that parses correctly

2. [x] Implement basic UUID v7 structure
   [x] It has the correct version bits (version 7)
   [x] It has the correct variant bits (variant 10)
   [x] It has the correct string representation format

3. [x] Implement timestamp functionality
   [x] The timestamp component reflects the current time
   [x] UUIDs created in sequence have monotonically increasing timestamps
   [x] Timestamp can be extracted from the UUID

4. [x] Implement randomness and uniqueness
   [x] The random component is properly generated
   [x] UUIDs generated in rapid succession are unique
   [x] UUIDs with the same timestamp have different random components

# Implementation Complete

The UUID v7 implementation is now complete. It satisfies all the requirements:

1. [x] It generates UUIDs with the correct version (7) and variant (10) bits
2. [x] It uses the current timestamp in the most significant bits
3. [x] It ensures monotonically increasing timestamps
4. [x] It provides a way to extract the timestamp from a UUID
5. [x] It ensures uniqueness even for UUIDs generated in rapid succession
6. [x] It ensures UUIDs with the same timestamp have different random components

All tests are passing, and the implementation is ready for use.

# Include a monotonically increasing counter in the UUID

- [x] 2000 UUIDs generated quickly sort in the order they are generated, even if they have the same timestamp

The implementation now includes a monotonically increasing counter that ensures UUIDs sort in the order they were
generated, even if they have the same timestamp. The counter is stored in the 12 bits that were previously random bits
in the most significant bits of the UUID. The counter is incremented for each UUID generated with the same timestamp and
reset when the timestamp changes.

# Use microsecond precision in timestamp

The timestamp uses as much microsecond precision as possible within 48 bits. These constraints will allow
dates from the Unix Epoch until some time in the 22nd century:

1. Get the number of microseconds since the Epoch.
2. Shift that number left by 12 bits. The maximum value 0xF_FFFF_FFFF_FFFF gives the number of microseconds in the year 2112.
3. The last 4 bits of the number of microseconds are overwritten by the version bits (0x07).

Tests:

- [ ] The timestamp used in the UUID can be extracted, to the nearest microsecond but truncated by 4 bits.
- [ ] Generate 10000 UUIDs quickly, then assert that they sort in the same order. 
