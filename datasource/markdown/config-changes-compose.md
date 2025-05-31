# Stop Fearing Rotations: Mastering Configuration Changes in Jetpack Compose

Ah, configuration changes. The classic Android developer headache. For years, we've wrestled with `onSaveInstanceState`, `ViewModel` lifecycles, and the dreaded `Activity` recreation just because a user tilted their phone. It was a rite of passage, sure, but often a frustrating one.

Enter Jetpack Compose, the modern UI toolkit that's revolutionizing how we build Android apps. While Compose brings a declarative and more intuitive way to craft UIs, the underlying Android system still recreates your `Activity` (or `Fragment`) during configuration changes like screen rotations, theme switches, or language adjustments.

So, are we doomed to repeat the same state-saving dances? Not quite! Compose offers powerful, more streamlined tools to handle these scenarios gracefully. This post is your guide to mastering configuration changes in the Compose world, so you can spend less time fighting the framework and more time building beautiful, resilient UIs.

## The Core Challenge: UI State vs. Configuration Changes

When a configuration change occurs, your `Activity` is destroyed and recreated. This means any local state held directly within your Composable functions would normally be wiped clean. Imagine a counter that resets every time you rotate the screen – not ideal!

Jetpack Compose rebuilds the UI by calling your Composable functions again (a process called recomposition). The key is to ensure that the _state_ driving your UI survives this recreation.

## Meet Your State-Saving Allies in Compose

Compose provides two primary mechanisms for remembering state across recompositions and, crucially, across configuration changes:

### 1. `remember`: For State Across Recompositions

The `remember` function is your go-to for storing state that needs to persist across multiple recompositions of the _same_ Composable instance.

```
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SimpleCounter() {
    var count by remember { mutableStateOf(0) } // State remembered across recompositions

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

**When `remember` falls short:** If you rotate the screen with this `SimpleCounter`, the `count` will reset to 0. Why? Because `remember` only keeps state as long as the Composable remains in the composition. When the `Activity` is recreated, the previous Composable instance is gone, and so is its `remember`ed state.

### 2. `rememberSaveable`: Your Hero for Configuration Changes

This is where `rememberSaveable` shines. It behaves like `remember` but with a superpower: it automatically saves its state into the `Bundle` used by `onSaveInstanceState`. This means the state survives `Activity` recreation due to configuration changes and even process death (when the OS kills your app in the background).

```
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable // Import rememberSaveable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PersistentCounter() {
    var count by rememberSaveable { mutableStateOf(0) } // State survives configuration changes!

    Button(onClick = { count++ }) {
        Text("Count: $count (Persistent)")
    }
}
```

Now, if you rotate the screen with `PersistentCounter`, the `count` will retain its value!

**What can `rememberSaveable` save?** It can automatically save types that can be stored in a `Bundle`, such as:

- Primitive types (Int, String, Boolean, etc.)

- `Parcelable` (including `Bundle` itself)

- `Serializable`

- `Enum`

- `Saver` and `listSaver` for custom types.


For more complex types that aren't directly `Parcelable` or `Serializable`, you can provide a custom `Saver` object to `rememberSaveable` to define how it should be saved and restored.

```
// Example with a custom data class (if it weren't Parcelable)
data class UserProfile(val name: String, val age: Int)

val UserProfileSaver = Saver<UserProfile, Bundle>(
    save = { Bundle().apply { putString("name", it.name); putInt("age", it.age) } },
    restore = { UserProfile(it.getString("name")!!, it.getInt("age")!!) }
)

@Composable
fun UserProfileEditor() {
    var userProfile by rememberSaveable(stateSaver = UserProfileSaver) {
        mutableStateOf(UserProfile("Alex", 30))
    }
    // ... UI to edit userProfile ...
}
```

However, it's often simpler to make your data class `Parcelable` if possible:

```
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileParcelable(val name: String, val age: Int) : Parcelable

@Composable
fun UserProfileEditorParcelable() {
    var userProfile by rememberSaveable {
        mutableStateOf(UserProfileParcelable("Alex", 30))
    }
    // ... UI to edit userProfile ...
    Text("User: ${userProfile.name}, Age: ${userProfile.age}")
}
```

## `ViewModel`: Still the Backbone for Business Logic and Complex State

While `rememberSaveable` is fantastic for simple UI state, `ViewModel` remains an indispensable part of modern Android architecture, even with Compose.

**Why `ViewModel` is still crucial:**

1. **Survives Configuration Changes (and Process Death):** This is its core strength. ViewModels are designed to hold data related to the screen's UI, independent of the `Activity` or `Fragment` lifecycle.

2. **Holds Business Logic:** Keep your Composables focused on UI. Business logic, data fetching, and complex state transformations belong in the `ViewModel`.

3. **Shares State:** If multiple Composables on a screen need to access or modify the same state, or if you need to share data with the hosting `Activity`/`Fragment`, `ViewModel` is the way to go.

4. **Lifecycle Awareness:** It can be aware of its lifecycle and clean up resources when it's no longer needed (e.g., in `onCleared()`).


**Integrating `ViewModel` with Compose:**

You typically access a `ViewModel` in your Composables using the `viewModel()` delegate from the `androidx.lifecycle:lifecycle-viewmodel-compose` library.

```
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel // Import viewModel

// 1. Define your ViewModel
class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow("Loading...")
    val uiState: StateFlow<String> = _uiState

    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter

    fun fetchData() {
        viewModelScope.launch {
            // Simulate network request
            kotlinx.coroutines.delay(1000)
            _uiState.value = "Data Loaded!"
        }
    }

    fun incrementCounter() {
        _counter.value++
    }
}

// 2. Use it in your Composable
@Composable
fun MyScreenWithViewModel(myViewModel: MyViewModel = viewModel()) {
    // Collect StateFlow as Compose State
    val currentUiState by myViewModel.uiState.collectAsState()
    val currentCount by myViewModel.counter.collectAsState()

    Column {
        Text(text = currentUiState)
        Button(onClick = { myViewModel.fetchData() }) {
            Text("Fetch Data")
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Text(text = "Counter from ViewModel: $currentCount")
        Button(onClick = { myViewModel.incrementCounter() }) {
            Text("Increment VM Counter")
        }
    }
}
```

In this example, `currentUiState` and `currentCount` will survive configuration changes because they are held by the `ViewModel`. The `ViewModel` itself is retained by the system across `Activity` recreations.

## Handling Specific Configuration Scenarios

- **Orientation Changes:**

    - UI State: Use `rememberSaveable`.

    - Data & Business Logic: Use `ViewModel`.

    - Layout Adaptations: Compose's flexibility with modifiers, `BoxWithConstraints`, or custom layout logic allows you to adapt your UI to different orientations more easily than with XML.

- **Dark/Light Mode Theme Changes:**

    - Compose's `MaterialTheme` integrates well with system theme changes.

    - Use `isSystemInDarkTheme()` to detect the current theme if you need custom logic.

    - Ensure your `colors.kt` defines colors for both light and dark themes. Your UI will automatically recompose with the new theme colors.


    ```
    // In your Theme.kt
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = when {
        darkTheme -> DarkColorScheme // Your dark colors
        else -> LightColorScheme // Your light colors
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
    ```

- **Language Changes:**

    - Use `stringResource(R.string.your_string_id)` for all text displayed in your UI.

    - When the system language changes, your Composables will automatically recompose with the strings from the new locale's `strings.xml` file.

- **Window Size Changes (Foldables, Multi-Window):**

    - This is a more advanced topic, but Compose provides tools like `BoxWithConstraints` to get the available space and adapt your layout accordingly.

    - Consider creating different Composables or modifying layout parameters based on width/height constraints.


## Best Practices for Robust State Handling

1. **Hoist State:** Keep your low-level Composables as stateless as possible. Pass state down and events up. This makes them more reusable and easier to test.

2. **`rememberSaveable` for UI State:** For simple UI elements' state that needs to survive configuration changes (e.g., scroll position, text field input, collapsed/expanded state of a panel).

3. **`ViewModel` for Screen-Level State & Logic:** For complex state, business logic, data fetching, and state that needs to be shared or outlive individual Composables.

4. **Test Rigorously:** Manually test by rotating the device, changing themes, toggling languages, and (if possible) simulating process death using developer options ("Don't keep activities").

5. **Favor `Parcelable`:** For custom data classes you want to store in `rememberSaveable`, implementing `Parcelable` is often the most efficient and Android-idiomatic way.

6. **Understand the Scope:** `remember` is scoped to the Composable's presence in the composition. `rememberSaveable` is scoped to the `Activity`/`Fragment` lifecycle (survives recreation). `ViewModel` is scoped to the `Activity`/`Fragment` or a navigation graph, outliving UI recreation.


## The Compose Advantage: A Sigh of Relief

Handling configuration changes in Jetpack Compose feels significantly more natural and less error-prone than the traditional View system.

- **Less Boilerplate:** No more manually overriding `onSaveInstanceState` and `onRestoreInstanceState` for every piece of UI state.

- **Declarative State:** You declare what state your UI depends on, and Compose handles the recomposition.

- **Focus on "What," not "How":** You describe your UI based on the current state, and `rememberSaveable` and `ViewModel` take care of preserving that state.


By understanding and correctly applying `remember`, `rememberSaveable`, and `ViewModel`, you can build robust, professional Android applications that gracefully handle configuration changes, leading to a much smoother user experience.

So, go ahead, rotate that device with confidence! Compose has your back.