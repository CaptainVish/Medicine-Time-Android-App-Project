# Medicine Time

Medicine Time is a native android application meant to aid the forgetful and busy by helping them keep track of their medication schedule.

## Goal

- The application allows the user to store pills and add multiple alarms for when they want to take those pills.
- Alarms can be set up for a particular time of day and can be repeated on multiple days of the week.
- The app provides a collective view of the medications that the user must take during that day, also, the user can choose to view medications for another day.
- The application stores the history of when each medication was taken; this will aid the user in keeping track of their medication usage.
- Store pills, alarms, and history locally using SQLite.

This is a learning project, not a clinically validated medication-management system. Reminder reliability should be tested on the target device before relying on it.

## Technology and project layout

The app uses Java, AndroidX, Material components, RecyclerView, Butter Knife, CompactCalendarView, SQLite, and Android's `AlarmManager`. Screens are organized around view/presenter contracts and a repository/data-source layer.

| Path | Purpose |
| --- | --- |
| `app/src/main/java/com/vishwajeeth/medicinetime/` | App screens, presenters, models, and local storage |
| `app/src/main/res/` | XML layouts, styles, icons, and other resources |
| `app/src/main/AndroidManifest.xml` | Activities and permissions |
| `app/src/mock/` and `app/src/prod/` | Build-flavor dependency injection |
| `app/build.gradle` | Android configuration and dependencies |

## Open and build

```bash
git clone https://github.com/CaptainVish/Medicine-Time-Android-App-Project.git
cd Medicine-Time-Android-App-Project
```

Open the repository root in Android Studio. The committed build configuration uses:

- Gradle wrapper **5.6.4** and Android Gradle Plugin **3.6.3**.
- Java **8** source/target compatibility.
- App compile/target SDK **29**, Build Tools **29.0.3**, and minimum SDK **16**.
- `mock` and `prod` flavors; `mockRelease` is disabled.

Before syncing or building:

1. Select a JDK compatible with the committed Gradle/Android plugin combination.
2. Set your own Android SDK location in local configuration; the committed `local.properties` is machine-specific.
3. Replace the absolute signing-keystore and ProGuard paths in `app/build.gradle` with your local configuration. Use your own signing material; do not reuse the committed key.
4. Review dependency resolution: the build references `jcenter()` and old dependencies, so environment restoration or a build-tool migration may be necessary.
5. Select a debug variant, such as `prodDebug`, and run on an emulator or test device.

Once those prerequisites are resolved, the corresponding command-line build is:

```bash
./gradlew assembleProdDebug
```

This is not a verified clean-build guarantee. The Android build and device behavior were not executed during this README update.

## Known maintenance work

- Signing material and signing configuration are committed. Treat them as unsuitable for future release signing and replace exposed credentials through the appropriate signing-key process.
- Test reminders across app closure, device restart, timezone changes, and battery restrictions.
- Review alarm behavior and permissions when updating the target SDK.
- Add focused tests for recurring schedules, deletion, and medication-history persistence; existing test scaffolding does not establish end-to-end reliability.

## License

See [LICENSE](LICENSE) for the existing Apache License 2.0 terms.
