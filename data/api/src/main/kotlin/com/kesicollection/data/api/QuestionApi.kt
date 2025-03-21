package com.kesicollection.data.api

import com.kesicollection.core.model.Question

interface QuestionApi {
    suspend fun fetchQuestions(): Result<List<Question>>
}

fun mockedQuestions(): List<Question> {
    return listOf(
        Question(
            question = "Consider a complex custom layout in Compose that requires precise measurement and placement of children based on dynamic content. How would you optimize its performance to minimize recompositions and layout passes, especially when dealing with large datasets?",
            options = listOf(
                "Using `Modifier.layout` with direct manipulation of `Placeable` instances and extensive caching.",
                "Relying solely on built-in Compose layouts and modifiers, hoping for automatic optimization.",
                "Creating a custom `Layout` composable with minimal logic and relying on recomposition for updates.",
                "Using `SubcomposeLayout` for all child elements, regardless of complexity."
            ),
            topic = "Jetpack Compose",
            correctAnswerIndex = 0,
            difficulty = "Hard",
            explanation = "Direct manipulation of `Placeable` instances in `Modifier.layout` and strategic caching allows for fine-grained control and performance optimization. It minimizes unnecessary recompositions and layout calculations.",
            tags = listOf("performance", "custom layout", "optimization")
        ),
        Question(
            question = "In a scenario where you need to implement a complex, interactive animation that involves multiple synchronized composables responding to user input and state changes, which approach would provide the most robust and performant solution?",
            options = listOf(
                "Using `animate*AsState` modifiers with `LaunchedEffect` for complex sequences.",
                "Combining `Animatable` with custom `AnimationSpec` and `derivedStateOf` for state-driven animations.",
                "Relying on XML-based animations wrapped in `AndroidView` for performance.",
                "Using `transition` and `updateTransition` with simple `AnimationSpec` for all animation needs."
            ),
            topic = "Jetpack Compose",
            correctAnswerIndex = 1,
            difficulty = "Hard",
            explanation = "`Animatable` with custom `AnimationSpec` and `derivedStateOf` provides precise control over animation state and allows for complex, state-driven animations with optimized performance.",
            tags = listOf("animation", "performance", "state management")
        ),
        Question(
            question = "You have a composable that displays a large, paginated list of items fetched from a remote source. How would you implement efficient lazy loading and state management to ensure smooth scrolling and minimal memory usage, even with thousands of items?",
            options = listOf(
                "Using `LazyColumn` with `itemsIndexed` and loading all data upfront.",
                "Implementing a custom `LazyListLayout` with manual pagination and caching.",
                "Using `rememberLazyListState` with `derivedStateOf` to detect scroll position and trigger data fetching, employing a paging library.",
                "Relying on `Column` with `forEach` and loading data as needed during scrolling."
            ),
            topic = "Jetpack Compose",
            correctAnswerIndex = 2,
            difficulty = "Hard",
            explanation = "`rememberLazyListState` with `derivedStateOf` allows you to monitor scroll position and trigger data fetching efficiently. Using a paging library is essential for managing large datasets and optimizing memory usage.",
            tags = listOf("lazy loading", "performance", "state management", "paging")
        ),
        Question(
            question = "When designing a custom layout that involves complex constraints and dynamic sizing, how would you ensure that your composable adapts correctly to different screen sizes and orientations, while maintaining optimal performance?",
            options = listOf(
                "Using absolute pixel values for all dimensions and relying on `Modifier.fillMaxSize()`.",
                "Employing `Modifier.weight` and `Modifier.aspectRatio` with fixed ratios for all elements.",
                "Using `ConstraintLayout` with dynamic constraints based on screen size and orientation, and leveraging `Modifier.layoutId` for flexible placement.",
                "Relying on `Box` and `Column` with nested `Modifier.fillMaxWidth()` and `Modifier.fillMaxHeight()`."
            ),
            topic = "Jetpack Compose",
            correctAnswerIndex = 3,
            difficulty = "Hard",
            explanation = "ConstraintLayout with dynamic constraints and layoutId allows for adaptable layouts that respond to screen size and orientation changes. This approach provides flexibility and maintains performance.",
            tags = listOf("responsive layout", "constraint layout", "performance")
        ),
        Question(
            question = "In a multi-module Android project using Jetpack Compose, how would you implement a theming system that allows for dynamic theme changes across all modules while ensuring consistent look and feel and minimizing recompositions?",
            options = listOf(
                "Using a global `MutableState` for theme values and passing it to all composables.",
                "Creating a custom `CompositionLocal` with a shared `Theme` object and providing it at the root of the app.",
                "Storing theme values in SharedPreferences and reading them in each composable.",
                "Relying on Android's built-in theme system and overriding resources in each module."
            ),
            topic = "Jetpack Compose",
            correctAnswerIndex = 1,
            difficulty = "Hard",
            explanation = "Using a custom `CompositionLocal` with a shared `Theme` object provides a centralized and efficient way to manage themes across modules. Changes to the theme trigger recompositions only in the relevant composables.",
            tags = listOf("theming", "composition local", "multi-module")
        ),
        Question(
            question = "In a complex multi-module Android project, you need to inject a singleton instance of a class that depends on a scoped dependency from another module. How would you configure Hilt to ensure proper dependency lifecycle and avoid memory leaks?",
            options = listOf(
                "Using `@Singleton` for both the singleton and scoped dependencies.",
                "Creating a custom `@Scope` annotation and applying it to both dependencies.",
                "Using `@BindsInstance` to provide the scoped dependency from the parent module.",
                "Creating a custom component hierarchy with `Subcomponent` and `Subcomponent.Factory` for the scoped dependency."
            ),
            topic = "Hilt",
            correctAnswerIndex = 3,
            difficulty = "Hard",
            explanation = "Using `Subcomponent` and `Subcomponent.Factory` allows for a hierarchical component structure that manages the lifecycle of scoped dependencies within the appropriate scope, preventing memory leaks.",
            tags = listOf("dependency injection", "subcomponent", "scope", "multi-module")
        ),
        Question(
            question = "You have a library module that provides a set of UI components and requires access to application-scoped dependencies. How would you configure Hilt to allow the library to access these dependencies without tightly coupling it to the application module?",
            options = listOf(
                "Using `@InstallIn(SingletonComponent::class)` in the library module and providing the dependencies directly.",
                "Creating a separate Hilt component in the library module and installing it in the application component.",
                "Using `@EntryPoint` to define an interface for accessing the dependencies and implementing it in the application module.",
                "Relying on reflection to access the application-scoped dependencies from the library module."
            ),
            topic = "Hilt",
            correctAnswerIndex = 2,
            difficulty = "Hard",
            explanation = "`@EntryPoint` allows you to define an interface for accessing dependencies from a component, providing a clean separation between the library and application modules.",
            tags = listOf("dependency injection", "entry point", "library module", "abstraction")
        ),
        Question(
            question = "When dealing with asynchronous operations that require access to scoped dependencies, how would you ensure that the dependencies are available when needed and properly cleaned up afterward, especially in long-running operations?",
            options = listOf(
                "Using `CoroutineScope` injected with `@ViewModelScoped` dependencies and relying on its lifecycle.",
                "Injecting dependencies directly into the coroutine's lambda and relying on garbage collection.",
                "Creating a custom `CoroutineScope` and manually managing the dependency lifecycle.",
                "Using `withContext` to access the dependencies within a specific scope."
            ),
            topic = "Hilt",
            correctAnswerIndex = 0,
            difficulty = "Hard",
            explanation = "Injecting `@ViewModelScoped` dependencies into a `CoroutineScope` ensures that the dependencies are available within the ViewModel's lifecycle and cleaned up when the ViewModel is cleared.",
            tags = listOf("dependency injection", "coroutine", "scope", "lifecycle")
        ),

    )
}