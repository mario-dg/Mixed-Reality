# Base Framework for "mixed Reality"

## Prerequisites

* JDK 17

## Dependencies

* cg_math
* cg_ui

The dependencies are handled via submodules. In order to use the submodules, one
needs du subsequently run

    git submodule init

und

    git submodule update

in all three subdirectories *cg_math* and *cg_ui*.

## Build

Run in the *pcg*-directory:

    gradle build

or

    ./gradlew build

## Import

* File - Open - select directory with file _buid.gradle_
* as Gradle project
