# 👋 Welcome to the Wellness Buddy Repository

## Introduction

Welcome! This guide will walk you through how to set up before contributing to development.

## Getting Started
### Setup your local environment

- [Configure SSH Keys for GitHub](https://docs.github.com/en/authentication/connecting-to-github-with-ssh)

### Continuous Integration Tool
We have integrated GitHub Actions as our primary continuous integration tool to automate, streamline, and enhance our development workflow. The configuration set will automatically triggers a Gradle build check each time a commit is pushed to the repository. This setup ensures that every change is verified for build success before being integrated into the main codebase, thereby maintaining the stability and reliability of our project. This continuous integration process plays a crucial role in identifying and resolving potential issues early, enhancing the overall quality of our software.

### Helpful Commands 
`./gradlew build` 
- Compiles and builds your entire Android project. It includes compiling your application code, resources, and any dependent libraries.

`/gradlew build connectedCheck`
- Instructs Gradle to execute both the standard build process (which includes unit tests) and the Android Instrumentation Tests in sequence. This comprehensive approach ensures that all aspects of your application are built and tested: the application code, unit tests, and UI/instrumentation tests.

`/gradlew connectedCheck`
- Specifically runs the Android Instrumentation Tests, which are tests that need an actual device or an emulator. Instrumentation tests are used to test the UI and integration points of your application that require an Android environment.

## Troubleshooting 
If you are running into issue running `./gradlew` in the terminal, make sure the file permission is enable. 
In the master branch in your IDE, run `gradle wrapper` - this will add all the necessary files.
To enable permission manually, you can run `chmod +x gradlew` to enable permission.

If you are any other questions and concerns, feel free to reach out to [me](https://github.com/emilyyankan)





