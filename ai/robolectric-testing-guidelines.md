Libraries that must be used for UI testing:

- Robolectric 
- Mockk DSL
- androidx.compose.ui.test.junit4.createComposeRule
- Google's truth library for assertions

Always use backsticks (``) to name the tests using the template: "when", "then"

If you detect that `coil3.*` libraries are used, make sure to use `coil3.test.FakeImageLoaderEngine` to build an instance as follow:
```kotlin
fakeImageLoaderEngine = FakeImageLoaderEngine.Builder()
            .default(ColorImage(Color.Blue.toArgb()))
            .build()
```

Define `@Config(sdk = [Build.VERSION_CODES.M, Build.VERSION_CODES.TIRAMISU])` at the testing class

Do not use lambda mockks, instead create an interface called `LambdaInvokers.kt` to avoid Robolectric and Mockk to duplicate class definitions on sdk version 23. Here is an example of wha I mean:
```kotlin
// For onNavigateUp: () -> Unit
fun interface OnNavigateUp {
    operator fun invoke()
}
```

Always access string resources with this approach:
```kotlin
//Global property
private val applicationContext: Application = ApplicationProvider.getApplicationContext()
fun someMethod() {
    applicationContext.getString(R.string.example)
}
```

Document all in the new file and make sure to add a list of test in the class kdoc