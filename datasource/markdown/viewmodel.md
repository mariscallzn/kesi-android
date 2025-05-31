# Surviving Configuration Changes: ViewModel & Jetpack Compose to the Rescue!

Hey Android Devs! 👋

We've all been there. You've crafted a beautiful UI, your app is running smoothly, and then... BAM! The user rotates their screen, and your app's state is wiped clean. Inputs lost, data re-fetched, user experience? Frustratingly disrupted. This, my friends, is the classic pain point of **configuration changes** in Android.

But fear not! In the world of modern Android development, especially with the declarative magic of Jetpack Compose, we have a trusty hero: the **ViewModel**. In this post, we're going to dive deep into how ViewModels elegantly solve the data retention puzzle during configuration changes.

## What Exactly Are These "Configuration Changes"?

Before we hail our hero, let's understand the villain. Configuration changes are events that alter the device's state, forcing Android to recreate your Activity (and thus, your Composable UI if you're using Compose). Common culprits include:

- **Screen Rotation:** The most frequent offender.

- **Language Change:** When the user switches the device locale.

- **Keyboard Availability:** Attaching or detaching a physical keyboard.

- **Window Size Changes:** In multi-window mode.

- **Dark Mode/Light Mode Toggling:** Changing the system theme.


When these occur, Android destroys your current Activity instance and creates a new one. If you're not careful, any data stored in that Activity is lost. Imagine a user filling out a long form, rotating their screen, and having to start all over. Ouch!

## The Dark Ages: Life Before ViewModel

In the olden days (and sometimes still, for specific use cases), developers had to manually save and restore instance state using `onSaveInstanceState()` and `onRestoreInstanceState()` (or by retrieving it in `onCreate()`). This involved:

1. Bundling up data into a `Bundle`.

2. Android saving this `Bundle` across the configuration change.

3. Unbundling the data in the new Activity instance.


While functional, this approach can become cumbersome for complex data, leads to boilerplate code, and mixes UI state logic directly within the Activity/Fragment, muddying the separation of concerns.

## Enter the ViewModel: A Beacon of Stability

The ViewModel, part of the Android Jetpack Architecture Components, was introduced to address these challenges head-on.

**What is a ViewModel?**

A ViewModel is designed to store and manage UI-related data in a lifecycle-conscious way. The key takeaway is that a **ViewModel instance survives configuration changes**. When an Activity or Fragment is recreated due to a configuration change, it receives the _same_ ViewModel instance that was previously associated with it.

**How Does It Work Its Magic?**

The magic lies in how ViewModels are scoped and managed:

1. **ViewModelStore:** When you request a ViewModel, it's not directly tied to the Activity/Fragment _instance_ but to a `ViewModelStoreOwner` (which Activities and Fragments implement). This owner has a `ViewModelStore` associated with it.

2. **ViewModelProvider:** You obtain a ViewModel using a `ViewModelProvider`. This provider, given a `ViewModelStoreOwner`, either creates a new ViewModel instance (if one doesn't exist for that scope) or returns the existing one.

3. **Surviving Destruction:** When an Activity is destroyed due to a configuration change, its `ViewModelStore` (and thus its ViewModels) are _not_ destroyed if the `ViewModelStoreOwner` is still active (e.g., the Activity is being recreated, not finished permanently). The new Activity instance then connects to this existing `ViewModelStore`.


**The ViewModel Lifecycle:**

A ViewModel's lifecycle is tied to the scope it's obtained from (e.g., an Activity or a Fragment).

- It's created when first requested by the `ViewModelProvider`.

- It remains in memory as long as its scope is active.

- The `onCleared()` method of the ViewModel is called when the associated `ViewModelStoreOwner` is permanently destroyed (e.g., the Activity `finish()`es), allowing you to clean up any resources.


This means your UI data, safely tucked away in the ViewModel, remains untouched and readily available after the UI is recreated.

## ViewModels in the World of Jetpack Compose

Jetpack Compose, with its declarative paradigm, integrates seamlessly with ViewModels. In fact, using ViewModels is the recommended way to manage state that needs to survive configuration changes or that represents business logic for your UI.

**Getting a ViewModel in a Composable:**

You typically obtain a ViewModel instance within a Composable function using the `viewModel()` composable function from the `androidx.lifecycle:lifecycle-viewmodel-compose` library:

```
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

// A simple ViewModel
class MyViewModel : ViewModel() {
    // State to be managed (e.g., using StateFlow or LiveData)
    // ... business logic methods ...
}

@Composable
fun MyScreen(myViewModel: MyViewModel = viewModel()) {
    // Use myViewModel to drive your UI and handle events
}
```

The `viewModel()` function is smart. It will provide the correct ViewModel instance scoped to the nearest `ViewModelStoreOwner` (like the hosting Activity or Fragment, or even a Navigation graph entry).

**Managing State for Compose:**

ViewModels typically expose data to the Compose UI using observable state holders like:

- **`StateFlow` (from Kotlin Coroutines):** Often used with `collectAsStateWithLifecycle()` (from `androidx.lifecycle:lifecycle-runtime-compose`) or `collectAsState()` for Compose to react to changes.

- **`LiveData` (from Lifecycle library):** Can be observed in Compose using `observeAsState()`.


Let's look at a simple counter example:

**1. The ViewModel:**

```
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {
    // Private MutableStateFlow that can be modified within the ViewModel
    private val _count = MutableStateFlow(0)

    // Public immutable StateFlow that the UI can observe
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() {
        _count.update { it + 1 }
    }

    fun decrement() {
        _count.update { if (it > 0) it - 1 else 0 } // Prevent negative count
    }

    // onCleared() would be called if the ViewModel is being destroyed permanently
    override fun onCleared() {
        super.onCleared()
        // Clean up resources if any, e.g., cancel coroutines
        println("CounterViewModel onCleared")
    }
}
```

**2. The Composable UI:**

```
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recommended

@Composable
fun CounterScreen(counterViewModel: CounterViewModel = viewModel()) {
    // Collect the state from the ViewModel
    // collectAsStateWithLifecycle is lifecycle-aware and recommended
    val count by counterViewModel.count.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { counterViewModel.decrement() }) {
                Text("Decrement")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { counterViewModel.increment() }) {
                Text("Increment")
            }
        }
    }
}
```

Now, if you run this app and rotate the screen, the `count` value will be preserved because it lives in the `CounterViewModel`, which survives the Activity recreation. The new `CounterScreen` instance will simply reconnect to the existing `CounterViewModel` and display the current count. No data loss!

## Why This is a Game Changer: Key Benefits

Using ViewModels, especially with Compose, offers significant advantages:

1. **Data Persistence:** The most obvious win – your UI state survives configuration changes.

2. **Separation of Concerns:** ViewModels separate UI data and business logic from your UI rendering code (Composables or Activities/Fragments). This leads to cleaner, more organized code.

3. **Testability:** ViewModels are plain Kotlin/Java classes. They don't (and shouldn't) have direct dependencies on the Android Framework's UI components. This makes them much easier to unit test.

4. **Lifecycle Awareness:** They are designed to work with the Android lifecycle, preventing memory leaks and ensuring data is handled correctly.

5. **Sharable Logic:** A single ViewModel can be shared among multiple Composables or even Fragments within the same scope (e.g., an Activity-scoped ViewModel used by several screens in that Activity).


## Best Practices for ViewModel Ninjas

To make the most out of ViewModels:

- **Keep Them Lean:** ViewModels should not hold references to Activities, Fragments, Composable functions, or Contexts directly. This can lead to memory leaks if not handled with extreme care (e.g., using `AndroidViewModel` for application context only when absolutely necessary). Their job is to prepare data for the UI, not to know _how_ it's displayed.

- **Use Dependency Injection (DI):** For more complex ViewModels that have dependencies (like repositories, use cases), use a DI framework like Hilt or Koin to provide these dependencies. This makes them easier to manage and test.

- **Scope Appropriately:**

    - **Activity-scoped:** `viewModel()` in a Composable hosted by an Activity. The ViewModel lives as long as the Activity.

    - **Fragment-scoped:** `viewModel()` in a Composable hosted by a Fragment. The ViewModel lives as long as the Fragment.

    - **Navigation Graph-scoped:** You can scope a ViewModel to a navigation graph, making it easy to share data between destinations within that graph. `hiltViewModel(navBackStackEntry)` or `viewModel(navBackStackEntry)` are your friends here.

- **Expose State, Handle Events:** ViewModels typically expose state (data) to the UI and provide functions to handle UI events (like button clicks) which in turn might update the state.


## The Future is Composed, and ViewModel is Key

As Android development continues its evolution with Jetpack Compose, the ViewModel remains a cornerstone of robust application architecture. It elegantly handles the age-old problem of configuration changes, allowing developers to focus on building great user experiences without worrying about data loss.

By understanding how ViewModels work and integrating them correctly with your Composables, you're not just writing code; you're crafting resilient, maintainable, and professional Android applications.

So, the next time you're building a screen, remember your trusty ViewModel. It's there to keep your data safe and sound, no matter how many times the user decides to flip their phone!

Happy Composing!