# 👋 Welcome to the Wellness Buddy Repository

## Introduction

Welcome! This guide will walk you through how to set up before contributing to development.

## Getting Started
### Setup your local environment

- [Configure SSH Keys for GitHub](https://docs.github.com/en/authentication/connecting-to-github-with-ssh)
- [Install JDK 17](https://www.oracle.com/java/technologies/downloads/)

### Continuous Integration Tool
We have integrated GitHub Actions as our primary continuous integration tool to automate, streamline, and enhance our development workflow. The configuration set will automatically triggers a Gradle build check running `./gradlew build` each time a commit is pushed to the repository. This setup ensures that every change is verified for build success before being integrated into the main codebase, thereby maintaining the stability and reliability of our project. This continuous integration process plays a crucial role in identifying and resolving potential issues early, enhancing the overall quality of our software.

### Helpful Commands 
`./gradlew build` 
- Compiles and builds your entire Android project. It includes compiling your application code, resources, and any dependent libraries.

`/gradlew build connectedCheck`
- Instructs Gradle to execute both the standard build process (which includes unit tests) and the Android Instrumentation Tests in sequence. This comprehensive approach ensures that all aspects of your application are built and tested: the application code, unit tests, and UI/instrumentation tests.

`/gradlew connectedCheck`
- Specifically runs the Android Instrumentation Tests, which are tests that need an actual device or an emulator. Instrumentation tests are used to test the UI and integration points of your application that require an Android environment.

## Best Practice for Code Review
Coming soon.

## Troubleshooting 
### Issue #1
#### "I cannot run `./gradlew` in my terminal"
- In the master branch in your IDE, run `gradle wrapper` - this will add all the necessary files.
To enable permission manually, you can run `chmod +x gradlew` to enable permission.

### Issue #2
#### "My pull request shows `Android CI / build (pull_request) ` failed"
- This issue occurs because the GitHub Actions CI workflow found bugs in your code, this will prevent you from merging into the codebase until the bugs are fixed. You can just commit your changes after testing your code to your feature branch again, then it will trigger the build check to run again. In addition, ensure you have JDK 17 installed.

If you are any other questions and concerns, feel free to reach out to [me](https://github.com/emilyyankan)
