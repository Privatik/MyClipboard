# MyClipboard

<details>
  <summary>Kotlin</summary>

```kotlin
adb shell am start -n "io.my.myclipboard/io.my.myclipboard.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
adb shell am broadcast -a clipboard
adb shell am force-stop io.my.myclipboard
```
</details>
