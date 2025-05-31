# Jetpack Compose: Mastering CompositionLocals - When to Use Static vs. Dynamic (and Why!)

Hey Compose Coder! 👋

So, you're diving deep into Jetpack Compose, crafting beautiful, reactive UIs. You've probably encountered situations where you need to pass data down your Composable tree, but "prop drilling" (passing parameters through multiple layers of Composables) starts to feel clunky and verbose. Enter `CompositionLocal` – Compose's elegant solution for sharing data implicitly.

But then you see `staticCompositionLocalOf` and `compositionLocalOf` (which effectively replaces the older `dynamicLocalProvider`), and you might wonder: "What's the difference, and which one should I use?"

You're in the right place! In this post, we'll demystify these providers, explore their reasons for existence, and lay down the conventions for using them in modern Android development with Jetpack Compose. Let's unlock the full potential of `CompositionLocal`!

## What are CompositionLocals Anyway? The "Why"

At its core, a `CompositionLocal` allows you to define data that can be accessed by any Composable in a subtree without explicitly passing it as a parameter through each intermediate Composable. Think of it as "ambient" data that's just "there" for any descendant that needs it.

**Why is this so cool?**

1. **Cleaner Code:** Avoids cluttering your Composable functions with parameters that are only passed down for use by distant children.

2. **Decoupling:** Components don't need to know about data they don't directly use.

3. **Theming and System Data:** Perfect for providing things like theme attributes (colors, typography, shapes), system configurations (font scaling, locale), or even scoped dependencies.


`MaterialTheme` in Compose is a prime example – it uses `CompositionLocal` extensively to provide colors, typography, and shapes to all Material components within its scope.

```
// How MaterialTheme provides colors (simplified concept)
val LocalColors = staticCompositionLocalOf { lightColors() }

@Composable
fun MyScreen() {
    // Accessing the color provided by MaterialTheme
    val colors = MaterialTheme.colors // Internally uses LocalColors.current
    Text("Hello", color = colors.primary)
}
```

## Meet the Flavors: `staticCompositionLocalOf` vs. `compositionLocalOf`

The key difference between these two ways of creating a `CompositionLocal` lies in **how Jetpack Compose handles recomposition when the provided value changes.** This is crucial for performance!

### 1. `staticCompositionLocalOf`

As the name suggests, `staticCompositionLocalOf` is designed for values that are **truly static or change very, very infrequently.**

- **Creation:**

    ```
    val LocalExampleStatic = staticCompositionLocalOf<MyType> {
        // Provide a default value, this will be thrown if no value is provided
        // higher up in the tree.
        error("No LocalExampleStatic provided")
    }
    ```

- **Behavior on Value Change:** If the value provided to a `CompositionLocalProvider` for a `staticCompositionLocalOf` _changes_, **the entire content lambda of that `CompositionLocalProvider` will recompose.** This means all Composables defined within that provider's scope are recomposed, whether they directly consume the `CompositionLocal` or not.

- **Performance:** This sounds like a lot of recomposition, right? But if the value _rarely or never changes after the initial composition_, it's actually more performant. Compose can make optimizations because it assumes the value is stable.

- **When to Use:**

    - Values that are set once at a high level and don't change during the lifecycle of that scope (e.g., API clients, repository instances provided at the application level).

    - System-wide configurations that are unlikely to change at runtime (e.g., fundamental app settings, device characteristics).

    - Theming attributes that are stable for a given theme (like the default `MaterialTheme` colors, typography).


```
// Example: Providing a static API service
interface ApiService { fun fetchData(): String }
class ProdApiService : ApiService { override fun fetchData() = "Data from Prod" }

val LocalApiService = staticCompositionLocalOf<ApiService> { error("No ApiService provided") }

@Composable
fun AppRoot() {
    val apiService = remember { ProdApiService() } // Created once
    CompositionLocalProvider(LocalApiService provides apiService) {
        // If LocalApiService's provided value were to change (which it shouldn't here),
        // HomeScreen and everything inside it would recompose.
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    val service = LocalApiService.current // Consuming the service
    Text(service.fetchData())
}
```

### 2. `compositionLocalOf`

This is your go-to for values that **might change during the lifecycle of the provider's scope, and you want more granular control over recomposition.**

- **Creation:**

    ```
    val LocalExampleDynamic = compositionLocalOf<MyType> {
        error("No LocalExampleDynamic provided")
    }
    ```

- **Behavior on Value Change:** If the value provided to a `CompositionLocalProvider` for a `compositionLocalOf` changes, **only the Composables that actually read `.current` on this specific `CompositionLocal` will recompose.** Composables within the same provider scope that _don't_ read this particular local won't be affected.

- **Performance:** This targeted recomposition is generally better for values that are expected to change, as it minimizes unnecessary UI updates.

- **When to Use:**

    - Values that can change due to user interaction or other events (e.g., user preferences that can be updated in-app, UI state specific to a large section of the UI).

    - Data that is scoped to a certain part of the UI and might be updated within that scope.

    - When providing `State<T>` objects, as changes to the state's value will then trigger this fine-grained recomposition for consumers.


```
// Example: Providing a dynamic user preference
val LocalDarkMode = compositionLocalOf { mutableStateOf(false) } // Providing a MutableState

@Composable
fun SettingsScreen() {
    val isDarkMode = LocalDarkMode.current // Reading the MutableState<Boolean>
    Switch(
        checked = isDarkMode.value,
        onCheckedChange = { isDarkMode.value = it } // Changing the value
    )
    // ... other settings ...
}

@Composable
fun AppWithDynamicTheme() {
    val darkModeState = remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalDarkMode provides darkModeState) {
        // When darkModeState.value changes, only Composables reading LocalDarkMode.current
        // (like parts of ThemedContent) will recompose due to this specific change.
        ThemedContent()
    }
}

@Composable
fun ThemedContent() {
    val isDarkMode = LocalDarkMode.current.value // Reading the boolean value
    val backgroundColor = if (isDarkMode) Color.DarkGray else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    Column(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        Text("Hello World!", color = textColor)
        // This Composable will recompose if isDarkMode changes.
        SomeOtherComposableThatDoesNotUseDarkMode()
    }
}

@Composable
fun SomeOtherComposableThatDoesNotUseDarkMode() {
    // This Composable will NOT recompose if ONLY LocalDarkMode changes,
    // because it doesn't read LocalDarkMode.current.
    Text("I don't care about dark mode directly.")
}
```

In the example above, if `darkModeState.value` changes, `ThemedContent` will recompose because it reads `LocalDarkMode.current.value`. However, `SomeOtherComposableThatDoesNotUseDarkMode` (if it were a separate composable called from `ThemedContent`'s `Column`) would _not_ recompose _due to the dark mode change itself_, because it doesn't access `LocalDarkMode.current`. This is the power of `compositionLocalOf` for dynamic values!

## Conventions and Best Practices

So, how do you choose?

1. **Default to `compositionLocalOf` for most cases:** If there's any chance the value might change, or if you're providing a `State<T>`, `compositionLocalOf` is generally safer and offers better recomposition granularity. This is often the more common choice.

2. **Use `staticCompositionLocalOf` for truly static, application-wide constants:**

    - Think about things like providing a singleton `ImageLoader`, a `CoroutineDispatchers` wrapper, or fundamental theme properties that are defined once and never altered.

    - `MaterialTheme` uses `staticCompositionLocalOf` for its colors, typography, etc., because once a theme is applied, those specific values are generally not expected to change instance-by-instance within that theme's scope. If the _entire theme_ changes (e.g., user switches from light to dark theme), the top-level provider might change, causing a broader recomposition, which is acceptable for such a significant change.

3. **Naming Convention:** It's a common convention to prefix `CompositionLocal` names with `Local`, like `LocalContext`, `LocalDensity`, `LocalTextStyle`.

4. **Provide Sensible Defaults (or Don't):**

    - When creating a `CompositionLocal`, you must provide a lambda that returns a default value.

    - If there's no sensible universal default, it's common practice to have this lambda throw an error:

        ```
        val LocalRequiredService = staticCompositionLocalOf<MyService> {
            error("No MyService provided. Please use CompositionLocalProvider to set one.")
        }
        ```

      This makes it explicit that a provider is required higher up in the tree.

5. **Scope Wisely:** Provide locals at the lowest possible common ancestor in the Composable tree where they are needed. Don't just pollute the global scope if only a small section of your UI needs the data.

6. **Don't Overuse:** While powerful, `CompositionLocal` can make data flow harder to trace if overused. For simple data passing between direct parent/child, explicit parameters are often clearer. Reserve `CompositionLocal` for data that truly needs to be "ambient" or would cause excessive prop drilling.


## Performance Recap: The Nitty-Gritty

- **`staticCompositionLocalOf`:**

    - **Pros:** Can be more performant if the value _never or very rarely changes_ after initial composition. Compose can make more aggressive optimizations.

    - **Cons:** If the value _does_ change, the entire content of the `CompositionLocalProvider` recomposes.

- **`compositionLocalOf`:**

    - **Pros:** If the value changes, only Composables that _read_ that specific `CompositionLocal` instance will recompose. This is great for dynamic data.

    - **Cons:** Slightly more overhead than `staticCompositionLocalOf` if the value is truly static, as Compose needs to track individual consumers.


In modern Compose, you'll often find yourself using `compositionLocalOf` more frequently, especially when dealing with UI state or preferences that can change. `staticCompositionLocalOf` remains valuable for foundational, unchanging configurations.

## Conclusion: Choose with Intent!

`CompositionLocal` is a cornerstone of robust and maintainable Jetpack Compose applications. Understanding the distinction between `staticCompositionLocalOf` and `compositionLocalOf` allows you to make informed decisions that balance convenience with performance.

- **Use `staticCompositionLocalOf` for values that are stable and rarely, if ever, change within their provided scope.**

- **Prefer `compositionLocalOf` for values that can change, ensuring granular recomposition and optimal performance for dynamic data.**


By mastering these tools, you'll write cleaner, more efficient, and more scalable Compose UI.

Happy Composing! 🚀

**What are your experiences with `CompositionLocal`?**