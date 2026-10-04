# Mobile Applications

This repository contains three small Android applications. Each folder is a
standalone Gradle project that can be opened and run independently in Android
Studio.

## Applications

| Project | Description |
| --- | --- |
| [TaskFlow](./TaskFlow) | A task planner with a task list, task creation and details, pending-task count, and an About screen. Tasks are kept in memory and are cleared when the app process ends. |
| [myLogin](./myLogin) | A form demo that validates email and password fields, collects gender and hobby selections, checks terms acceptance, and opens a second screen. |
| [myRedirect](./myRedirect) | An Android intents demo with actions to open a website, start a phone dialer, compose an email, and find a place on a map. |

## Open and run

1. Install Android Studio and the Android SDK versions required by the project.
2. In Android Studio, select **Open** and choose one project folder: `TaskFlow`,
   `myLogin`, or `myRedirect`.
3. Let Gradle sync and install any SDK components Android Studio requests.
4. Select an emulator or connected Android device, then run the `app`
   configuration.

The projects use Kotlin, Gradle Kotlin DSL, and AndroidX/Material components.
Their application modules declare Java 11 source and target compatibility.

## Tests

Each project has local unit tests and Android instrumentation-test sources under
`app/src/test` and `app/src/androidTest`. Open a project in Android Studio and
run the relevant tests from the IDE or Gradle tool window.
