package com.kesicollection.data.api

import com.kesicollection.core.model.Question

interface QuestionApi {
    suspend fun fetchQuestions(): Result<List<Question>>
}

fun mockedQuestions(): List<Question> {
    return listOf(
        Question(
            question = "When using `rememberSaveable`, which data types are automatically saved across configuration changes?",
            options = listOf(
                "Custom data classes without Parcelize/Serializable.",
                "MutableState of primitive types and String.",
                "Lambdas and functions.",
                "All objects that implement the Serializable interface."
            ),
            topic = "State Management",
            correctAnswerIndex = 1,
            explanation = "Only primitive types and String within MutableState are automatically saved by rememberSaveable. Custom objects require Parcelize or Serializable.",
            tags = listOf("rememberSaveable", "State", "ConfigurationChanges")
        ),
        Question(
            question = "How can you effectively optimize recompositions when dealing with deeply nested Composables and large data structures?",
            options = listOf(
                "By using `Modifier.memoize` on all Composables.",
                "By passing immutable data structures and using `derivedStateOf` for derived states.",
                "By forcing recompositions using `invalidate()`.",
                "By using global variables to share state."
            ),
            topic = "Performance",
            correctAnswerIndex = 1,
            explanation = "Immutable data and derivedStateOf prevent unnecessary recompositions by ensuring changes are only triggered when necessary.",
            tags = listOf("Recomposition", "Optimization", "derivedStateOf")
        ),
        Question(
            question = "When implementing custom layout modifiers, what is the primary difference between `MeasureScope.measure` and `IntrinsicMeasureScope.measure`?",
            options = listOf(
                "`IntrinsicMeasureScope.measure` is used for measuring content size, while `MeasureScope.measure` calculates layout position.",
                "`MeasureScope.measure` is used for intrinsic measurements, and `IntrinsicMeasureScope.measure` is for final layout measurements.",
                "`MeasureScope.measure` performs a single measurement pass, while `IntrinsicMeasureScope.measure` performs multiple passes.",
                "`IntrinsicMeasureScope.measure` allows measuring content without considering constraints, while `MeasureScope.measure` considers constraints."
            ),
            topic = "Custom Layout",
            correctAnswerIndex = 3,
            explanation = "Intrinsic measurements determine the minimum/maximum size of content without constraints, while regular measurements apply layout constraints.",
            tags = listOf("CustomLayout", "MeasureScope", "IntrinsicMeasureScope")
        ),
        Question(
            question = "In Jetpack Compose, how do you correctly handle side effects that require coroutine cancellation when a Composable leaves the composition?",
            options = listOf(
                "By using `LaunchedEffect` without any keys.",
                "By using `DisposableEffect` with a `onDispose` block that cancels the coroutine.",
                "By using `rememberCoroutineScope` and manually cancelling the scope in `onDispose` of a `DisposableEffect`.",
                "By using `rememberUpdatedState` and manually cancelling the coroutine in the composable lambda."
            ),
            topic = "Side Effects",
            correctAnswerIndex = 2,
            explanation = "Using `rememberCoroutineScope` in conjunction with `DisposableEffect`'s `onDispose` block ensures that the coroutine is cancelled when the Composable is removed.",
            tags = listOf("SideEffects", "Coroutines", "DisposableEffect")
        ),
        Question(
            question = "When implementing custom drawing using `Canvas` in Compose, how do you handle transformations (e.g., rotation, scaling) while maintaining optimal performance?",
            options = listOf(
                "By applying transformations within each draw call without using `save()` and `restore()`.",
                "By using `Modifier.graphicsLayer` for all transformations.",
                "By using `drawIntoCanvas` with `Canvas.save()` and `Canvas.restore()` to limit transformation scope.",
                "By applying transformations using `Canvas.translate` and `Canvas.scale` directly without any save/restore."
            ),
            topic = "Custom Drawing",
            correctAnswerIndex = 2,
            explanation = "Using `save()` and `restore()` limits the scope of transformations, preventing unnecessary recalculations and improving performance.",
            tags = listOf("CustomDrawing", "Canvas", "Transformations")
        ),
        Question(
            question = "How do you correctly implement a custom `Modifier` that influences the layout behavior of its children, specifically changing the arrangement of items in a `Row`?",
            options = listOf(
                "By directly modifying the `LayoutCoordinates` of the children within the `Modifier`'s measure block.",
                "By creating a new `Layout` Composable and wrapping the content with it.",
                "By using `Modifier.layout` and manipulating the `Placeable` instances based on custom logic.",
                "By using `Modifier.drawBehind` to visually rearrange the items."
            ),
            topic = "Custom Modifiers",
            correctAnswerIndex = 2,
            explanation = "`Modifier.layout` allows you to intercept and modify the placement of child `Placeable` instances, enabling custom layout logic.",
            tags = listOf("CustomModifiers", "Layout", "Placeable")
        ),
        Question(
            question = "When implementing complex animations with `animate*AsState` and `AnimatedVisibility`, what is the most efficient way to handle interruptible animations that can be reversed or changed mid-animation?",
            options = listOf(
                "By using `withFrameNanos` to manually control the animation progress.",
                "By using `Animatable` and managing its state transitions explicitly with coroutines.",
                "By using `updateTransition` and defining multiple states with `animate*` functions.",
                "By using a global `MutableState` to track the animation progress."
            ),
            topic = "Animations",
            correctAnswerIndex = 2,
            explanation = "`Animatable` provides fine-grained control over animation states, allowing for pausing, reversing, and interrupting animations smoothly.",
            tags = listOf("Animations", "Animatable", "Transitions")
        ),
        Question(
            question = "How can you effectively manage and propagate a complex, mutable application state across a deep Composable hierarchy without triggering unnecessary recompositions?",
            options = listOf(
                "By using `CompositionLocal` with a `MutableStateFlow` and collecting it in each Composable.",
                "By using a global `MutableState` and accessing it directly from Composables.",
                "By passing the `MutableState` down the hierarchy as a parameter in each Composable.",
                "By using `rememberUpdatedState` with a global variable."
            ),
            topic = "State Management",
            correctAnswerIndex = 0,
            explanation = "`CompositionLocal` with a `MutableStateFlow` allows for efficient state propagation and selective recompositions based on changes in the flow.",
            tags = listOf("StateManagement", "CompositionLocal", "StateFlow")
        ),
        Question(
            question = "In Jetpack Compose, how do you handle accessibility for a custom interactive component that doesn't fit the standard Material Design components?",
            options = listOf(
                "By using `Modifier.semantics` and defining custom actions and states without considering talkback.",
                "By using `Modifier.clickable` and relying on default accessibility behavior.",
                "By using `Modifier.semantics` with custom actions, states, and properties, and testing thoroughly with accessibility tools.",
                "By ignoring accessibility for custom components."
            ),
            topic = "Accessibility",
            correctAnswerIndex = 3,
            explanation = "Using `Modifier.semantics` with custom actions, states, and properties, and thoroughly testing with accessibility tools is the proper way to handle custom components accessibility.",
            tags = listOf("Accessibility", "Semantics", "CustomComponents")
        ),
        Question(
            question = "When integrating Jetpack Compose with existing Android Views, what is the most efficient way to handle bidirectional communication between Compose and View layers, especially for complex interactions?",
            options = listOf(
                "By using `AndroidView` and directly accessing the View's properties from Compose.",
                "By using `ComposeView` and directly manipulating Compose state from the View layer.",
                "By using callbacks and state hoisting to propagate events and state changes between the layers.",
                "By using global variables to share state between Compose and View."
            ),
            topic = "Interoperability",
            correctAnswerIndex = 2,
            explanation = "Callbacks and state hoisting provide a clean and maintainable way to handle communication between Compose and View layers, preventing tight coupling.",
            tags = listOf("Interoperability", "AndroidView", "ComposeView")
        ),
        Question(
            question = "When implementing a custom text layout with complex line breaking and hyphenation rules in Compose, what is the most effective approach?",
            options = listOf(
                "Using `AnnotatedString` with multiple spans and manually calculating line breaks.",
                "Using `Canvas` and drawing text character by character with custom layout logic.",
                "Integrating a native text shaping library via `AndroidView` and using its output in Compose.",
                "Using `BasicText` and relying on default line breaking."
            ),
            topic = "Text Layout",
            correctAnswerIndex = 2,
            explanation = "Integrating a native text shaping library allows for complex text layout features beyond what Compose's default text components provide.",
            tags = listOf("TextLayout", "CustomText", "AndroidView")
        ),
        Question(
            question = "How do you efficiently manage and visualize a large dataset (e.g., thousands of items) in a scrollable list in Compose, while maintaining smooth performance and minimizing memory usage?",
            options = listOf(
                "Using `Column` with `rememberLazyListState` and loading all items into memory.",
                "Using `LazyColumn` with a custom `items` lambda that loads data on demand and reuses Composables.",
                "Using `VerticalScrollbar` with `Column` and manually managing item visibility.",
                "Using `RecyclerView` inside an `AndroidView`."
            ),
            topic = "Lists and Performance",
            correctAnswerIndex = 1,
            explanation = "`LazyColumn` with on-demand loading and Composable reuse is the most efficient way to handle large datasets in scrollable lists.",
            tags = listOf("LazyColumn", "Performance", "LargeData")
        ),
        Question(
            question = "When designing a custom layout that adapts to different screen sizes and aspect ratios, how do you correctly apply constraint-based layouts in Compose without using `ConstraintLayout`?",
            options = listOf(
                "By using `Modifier.fillMaxSize()` and relying on implicit layout constraints.",
                "By using `Layout` and manually calculating constraints based on available space and aspect ratio.",
                "By using `BoxWithConstraints` and dynamically adjusting layout parameters based on available constraints.",
                "By using `Modifier.aspectRatio()` and relying on implicit aspect ratio constraints."
            ),
            topic = "Responsive Layout",
            correctAnswerIndex = 2,
            explanation = "`BoxWithConstraints` allows you to access and react to available constraints, enabling dynamic layout adjustments based on screen size and aspect ratio.",
            tags = listOf("ResponsiveLayout", "BoxWithConstraints", "Constraints")
        ),
        Question(
            question = "How can you implement a custom gesture detector that recognizes complex multi-touch gestures (e.g., rotation, pinch-to-zoom) while integrating smoothly with Compose's animation system?",
            options = listOf(
                "By using `Modifier.pointerInput` and manually calculating gesture transformations without animation.",
                "By using `Modifier.draggable` and relying on default gesture detection.",
                "By using `Modifier.pointerInput` with `detectTransformGestures` and combining it with `Animatable` for smooth animations.",
                "By using `AndroidView` with a custom `GestureDetector` and updating Compose state based on callbacks."
            ),
            topic = "Gestures and Animations",
            correctAnswerIndex = 3,
            explanation = "`Modifier.pointerInput` with `detectTransformGestures` and `Animatable` provides a powerful way to implement complex gestures with smooth animations.",
            tags = listOf("Gestures", "Animations", "PointerInput")
        ),
        Question(
            question = "When implementing a custom `CompositionLocal` that depends on external resources (e.g., network data, file system), how do you ensure that changes to these resources trigger recompositions only when necessary and avoid memory leaks?",
            options = listOf(
                "By using a global `MutableState` and updating it directly from external resource callbacks.",
                "By using `rememberUpdatedState` with a global variable that monitors external resources.",
                "By using `CompositionLocal` with a `StateFlow` that emits updates only when resources change, and managing the flow's lifecycle with `DisposableEffect`.",
                "By ignoring resource changes and relying on manual recompositions."
            ),
            topic = "CompositionLocal and Resources",
            correctAnswerIndex = 3,
            explanation = "Using `CompositionLocal` with a `StateFlow` and managing its lifecycle with `DisposableEffect` ensures efficient recompositions and prevents memory leaks.",
            tags = listOf("CompositionLocal", "Resources", "StateFlow")
        ),
        Question(
            question = "When using @EntryPoint, what is the most critical difference between defining an entry point interface in a library module versus an application module?",
            options = listOf(
                "Library modules require @InstallIn(SingletonComponent::class), application modules require @InstallIn(ActivityComponent::class).",
                "Library modules cannot access application-scoped bindings, application modules can access all bindings.",
                "Library modules can only inject bindings from their own module, application modules can inject bindings from all modules.",
                "Library modules require explicit dependencies declared in the application's build.gradle, application modules automatically resolve dependencies."
            ),
            topic = "Hilt",
            correctAnswerIndex = 1,
            explanation = "Library modules are designed to be decoupled. Therefore, they cannot directly access bindings provided by the application module. Application modules, being the root, have access to all bindings.",
            tags = listOf("EntryPoint", "Library", "Application")
        ),
        Question(
            question = "Given a complex dependency graph involving multiple modules and components, how can you most effectively diagnose cyclic dependencies in Hilt/Dagger at compile time?",
            options = listOf(
                "Using runtime reflection to inspect the object graph.",
                "Enabling verbose logging in the Hilt Gradle plugin.",
                "Analyzing the generated Dagger component classes for dependency cycles.",
                "Utilizing Dagger's graph validation features by enabling the '-Adagger.validateTransitiveComponentDependencies=ENABLED' compiler argument."
            ),
            topic = "Hilt",
            correctAnswerIndex = 3,
            explanation = "Dagger's graph validation, when enabled, performs a thorough analysis of the dependency graph at compile time, accurately identifying and reporting cyclic dependencies. This provides the earliest possible feedback.",
            tags = listOf("DependencyGraph", "Dagger", "Cycles", "CompileTime")
        ),
        Question(
            question = "Consider a scenario where you need to provide different implementations of an interface based on the build variant (e.g., debug vs. release). Which Hilt/Dagger feature allows for the most efficient and maintainable solution?",
            options = listOf(
                "Using @BindsOptionalOf with conditional logic.",
                "Implementing a custom @Qualifier with build-specific logic.",
                "Leveraging @InstallIn with different components based on build variants.",
                "Employing @Provides methods with build-specific flags and conditional returns."
            ),
            topic = "Hilt",
            correctAnswerIndex = 2,
            explanation = "By installing modules into different components based on build variants, you can effectively provide variant-specific bindings. This approach keeps the logic within Hilt's module system, improving maintainability.",
            tags = listOf("BuildVariants", "InstallIn", "Components")
        ),
        Question(
            question = "In a multi-module Android project using Hilt, how can you ensure that a specific @Provides method is only invoked once, even if multiple components depend on its provided type?",
            options = listOf(
                "Using @Singleton on the provided type.",
                "Applying @Reusable to the @Provides method.",
                "Ensuring the @Provides method is in a module installed in a component with the narrowest scope.",
                "Wrapping the @Provides method's logic in a synchronized block."
            ),
            topic = "Hilt",
            correctAnswerIndex = 2,
            explanation = "The scope of the component in which the module is installed determines the lifecycle of the provided bindings. By installing the module in the narrowest possible scope, you ensure that the @Provides method is invoked only once within that component's lifecycle.",
            tags = listOf("MultiModule", "Provides", "Scope", "Singleton")
        ),
        Question(
            question = "Given a complex scenario where you have to inject a dependency that is conditionally created based on the state of a fragment's arguments. How can you most effectively manage this using Hilt?",
            options = listOf(
                "Using a @Provides method with conditional logic based on fragment arguments.",
                "Using @AssistedInject with a custom factory to create the dependency.",
                "Using @EntryPoint with a function that takes fragment arguments as parameters.",
                "Using a custom scope with a factory that checks fragment arguments."
            ),
            topic = "Hilt",
            correctAnswerIndex = 1,
            explanation = "@AssistedInject combined with a custom factory provides the best way to handle dependencies that are conditionally created based on runtime parameters like fragment arguments, allowing for clean and testable code.",
            tags = listOf("AssistedInject", "Fragment", "Conditional", "Runtime")
        ),
        Question(
            question = "How can you effectively test a ViewModel that uses @AssistedInject with Hilt, especially when dealing with complex fragment argument dependencies?",
            options = listOf(
                "Using Robolectric and mocking all fragment arguments.",
                "Creating a custom test component and manually providing assisted factories.",
                "Relying solely on Espresso UI tests to verify ViewModel behavior.",
                "Using Dagger's test utilities to replace assisted factories with test doubles."
            ),
            topic = "Hilt",
            correctAnswerIndex = 1,
            explanation = "Robolectric, combined with mocking fragment arguments, allows for isolated and efficient testing of ViewModels using @AssistedInject. This approach provides fine-grained control over test inputs.",
            tags = listOf("ViewModel", "AssistedInject", "Testing", "Robolectric")
        ),
        Question(
            question = "In a scenario where you need to inject a dependency into a non-Hilt managed class (e.g., a legacy class), which Hilt mechanism is best suited for this purpose?",
            options = listOf(
                "Using @Inject on the non-Hilt class directly.",
                "Creating a custom Hilt component and manually injecting the dependency.",
                "Using @EntryPoint to provide an interface that exposes the dependency.",
                "Leveraging @InstallIn with a global scope and retrieving the dependency via Hilt.entryPointFrom()."
            ),
            topic = "Hilt",
            correctAnswerIndex = 3,
            explanation = "@EntryPoint allows you to define an interface that provides access to Hilt bindings from non-Hilt managed classes. This is the recommended way to bridge the gap between Hilt-managed and legacy code.",
            tags = listOf("EntryPoint", "Legacy", "NonHilt")
        ),
        Question(
            question = "When dealing with dynamic features in an Android app using Hilt, how can you ensure that dependencies specific to a dynamic feature module are correctly scoped and isolated?",
            options = listOf(
                "Using @InstallIn(SingletonComponent::class) for all dynamic feature dependencies.",
                "Defining custom components within the dynamic feature module and using @InstallIn with those components.",
                "Relying on the application's main component for all dynamic feature dependencies.",
                "Using @BindsOptionalOf for dynamic feature dependencies."
            ),
            topic = "Hilt",
            correctAnswerIndex = 1,
            explanation = "By defining custom components within the dynamic feature module and installing modules into them, you can isolate and scope dependencies specific to that feature. This ensures that dynamic feature dependencies are not leaked into other modules.",
            tags = listOf("DynamicFeatures", "Components", "Scope")
        ),
        Question(
            question = "Given a complex dependency graph where a dependency needs to be lazily initialized only when it's first used, which Hilt/Dagger feature should be used?",
            options = listOf(
                "Using @Singleton with a lazy initialization block.",
                "Using @Provides with a lazy delegate.",
                "Injecting a Provider<T> and invoking .get() when needed.",
                "Applying @Reusable to the @Provides method."
            ),
            topic = "Hilt",
            correctAnswerIndex = 2,
            explanation = "Injecting a Provider<T> allows you to defer the creation of the dependency until it's actually needed. Calling .get() on the Provider triggers the creation and returns the instance, ensuring lazy initialization.",
            tags = listOf("LazyInitialization", "Provider", "DependencyGraph")
        ),
        Question(
            question = "How can you effectively manage and inject configuration parameters (e.g., API keys, feature flags) that are fetched from a remote source or a configuration file at runtime, using Hilt?",
            options = listOf(
                "Using @Provides methods with hardcoded configuration values.",
                "Using @EntryPoint to retrieve configuration parameters directly from the configuration source.",
                "Creating a custom scope to hold configuration parameters and injecting them via @Inject.",
                "Injecting a Provider<Config> and fetching the config within the provider when requested."
            ),
            topic = "Hilt",
            correctAnswerIndex = 3,
            explanation = "By creating a custom scope that holds configuration parameters, you can ensure that these parameters are fetched and made available throughout the application lifecycle. Injecting a Provider allows lazy loading of the configuration.",
            tags = listOf("Configuration", "Runtime", "CustomScope", "Provider")
        ),
        Question(
            question = "When implementing nested navigation graphs in Compose Navigation, how can you most effectively manage and pass complex state objects between parent and child destinations while maintaining type safety?",
            options = listOf(
                "Using a global ViewModel shared between all nested destinations.",
                "Serializing the state object to a string and passing it as a navigation argument.",
                "Creating a custom navigation argument type that encapsulates the state object.",
                "Utilizing a shared Flow or StateFlow between the parent and child composables, managed by a scoped ViewModel."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 3,
            explanation = "Using a shared Flow or StateFlow managed by a scoped ViewModel allows for reactive and type-safe state sharing between nested destinations. This approach ensures that updates are propagated efficiently and consistently.",
            tags = listOf("NestedNavigation", "StateManagement", "ViewModel", "Flow")
        ),
        Question(
            question = "How can you implement deep linking with complex, dynamic parameters in Compose Navigation, while ensuring robustness and handling potential parsing errors?",
            options = listOf(
                "Using simple string arguments and manual parsing within the destination composable.",
                "Defining custom deep link schemes with regex-based argument matching and type conversion.",
                "Relying on implicit deep linking behavior based on navigation route patterns.",
                "Using a separate library for deep link parsing and then passing the parsed result to Compose Navigation."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 1,
            explanation = "Defining custom deep link schemes with regex-based argument matching and type conversion allows for precise control over deep link parsing. This approach ensures robustness and handles potential parsing errors by providing explicit validation rules.",
            tags = listOf("DeepLinking", "DynamicParameters", "Regex", "Parsing")
        ),
        Question(
            question = "In a scenario where you need to implement conditional navigation based on asynchronous data fetching or user authentication status, how can you most effectively integrate Compose Navigation with asynchronous operations?",
            options = listOf(
                "Using LaunchedEffect with a suspend function to perform the asynchronous operation and then navigate.",
                "Blocking the navigation thread until the asynchronous operation completes.",
                "Using a global coroutine scope to perform the asynchronous operation and then trigger navigation.",
                "Relying on side effects within composables to perform asynchronous operations and navigate."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 0,
            explanation = "LaunchedEffect with a suspend function allows for safe and controlled integration of asynchronous operations with Compose Navigation. This approach ensures that navigation is triggered only after the asynchronous operation completes, without blocking the main thread.",
            tags = listOf("ConditionalNavigation", "Asynchronous", "LaunchedEffect", "SuspendFunction")
        ),
        Question(
            question = "How can you implement seamless transitions between destinations in Compose Navigation, particularly when dealing with shared element transitions or complex animations?",
            options = listOf(
                "Relying on default navigation transitions provided by Compose Navigation.",
                "Using a separate animation library and manually triggering animations during navigation.",
                "Utilizing AnimatedContent with TransitionSpec to define custom transitions based on navigation events.",
                "Using Android View based transition framework with Compose Interop."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 2,
            explanation = "AnimatedContent with TransitionSpec allows for highly customizable and seamless transitions between destinations. This approach provides fine-grained control over animation behavior based on navigation events, enabling shared element transitions and complex animations.",
            tags = listOf("Transitions", "Animations", "AnimatedContent", "TransitionSpec")
        ),
        Question(
            question = "Given a complex navigation scenario with multiple back stacks and conditional navigation based on user roles or feature flags, how can you effectively manage and test the navigation logic in Compose Navigation?",
            options = listOf(
                "Using a single navigation controller and relying on manual testing to verify navigation behavior.",
                "Creating a custom navigation testing framework that simulates user interactions and verifies back stack behavior.",
                "Using a ViewModel to manage navigation state and writing unit tests for the ViewModel's navigation logic.",
                "Relying on Espresso UI tests to cover all possible navigation scenarios."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 2,
            explanation = "Using a ViewModel to manage navigation state and writing unit tests for the ViewModel's navigation logic allows for isolated and efficient testing of navigation behavior. This approach ensures that navigation logic is decoupled from UI components and can be tested independently.",
            tags = listOf("Testing", "NavigationLogic", "ViewModel", "BackStack")
        ),
        Question(
            question = "How can you effectively handle navigation events triggered from UI components that are not part of the composable hierarchy managed by the `NavController` (e.g., from a custom View or a system UI event)?",
            options = listOf(
                "Using a global event bus to communicate navigation events.",
                "Accessing the `NavController` directly from the non-composable component.",
                "Employing a callback mechanism or a shared `Flow` to propagate navigation requests to a composable that can then use the `NavController`.",
                "Relying on side effects within composables to observe external events and trigger navigation."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 2,
            explanation = "Using a callback mechanism or a shared `Flow` provides a clean and decoupled way to communicate navigation requests from non-composable components to a composable that has access to the `NavController`. This approach avoids direct access to the `NavController` from outside the composable hierarchy, maintaining better separation of concerns.",
            tags = listOf("NavigationEvents", "NavController", "NonComposable", "Flow")
        ),
        Question(
            question = "In a complex Compose Navigation setup with multiple nested graphs and dynamic navigation scenarios, how can you best visualize and debug the current navigation state, including the back stack and the active destinations?",
            options = listOf(
                "Relying solely on Log statements within composables to track navigation.",
                "Using the Android Studio Layout Inspector to inspect the composable hierarchy and infer navigation state.",
                "Creating a custom debugging tool or composable that observes the `NavController`'s state and displays a visual representation of the navigation graph.",
                "Using the `NavController.getBackStack()` method and printing the back stack in Logcat."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 2,
            explanation = "Creating a custom debugging tool or composable that observes the `NavController`'s state allows for a visual and interactive way to inspect the navigation graph, the back stack, and the active destinations. This approach provides a much clearer understanding of the navigation state compared to relying solely on logs.",
            tags = listOf("Debugging", "NavigationState", "NavController", "BackStack")
        ),
        Question(
            question = "When integrating Compose Navigation with the traditional Android navigation components (e.g., Fragments), what are the key considerations and strategies for ensuring seamless interoperability and a consistent user experience?",
            options = listOf(
                "Using a single `NavController` to manage both Compose and Fragment navigation.",
                "Treating Fragments as destinations within the Compose Navigation graph.",
                "Using a separate `NavController` for Compose and the FragmentManager for Fragments, and carefully orchestrating transitions between them.",
                "Migrating all Fragments to Compose before adopting Compose Navigation."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 2,
            explanation = "Using separate `NavController` for Compose and the FragmentManager for Fragments, and carefully orchestrating transitions between them, is the recommended approach for interoperability. This allows for better management of the back stack and navigation behavior for each system, while enabling a consistent user experience through coordinated transitions.",
            tags = listOf("Interoperability", "Fragments", "NavController", "AndroidNavigation")
        ),
        Question(
            question = "How can you implement a navigation pattern where a destination can be accessed from multiple points in the app, but navigating 'up' from that destination should return to the specific point of origin (e.g., a settings screen accessible from different parts of the app)?",
            options = listOf(
                "Using a single navigation route for the destination and relying on the back stack to manage the return path.",
                "Creating multiple copies of the destination composable for each point of origin.",
                "Using a combination of navigation actions and custom back stack manipulation to define specific 'up' navigation behavior.",
                "Relying on a global state management solution to track the point of origin and conditionally navigate 'up'."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 2,
            explanation = "Using a combination of navigation actions and custom back stack manipulation provides the most flexible and precise way to define specific 'up' navigation behavior. This allows you to control the return path based on the origin of the navigation, even if the destination is accessed from multiple points.",
            tags = listOf("NavigationPattern", "BackStack", "NavigationActions", "UpNavigation")
        ),
        Question(
            question = "Given a scenario where you want to implement a complex UI flow with multiple steps and conditional branching, such as a multi-step form or a guided tour, how can you effectively manage the navigation flow and state using Compose Navigation?",
            options = listOf(
                "Using a single navigation route and relying on composable state to manage the flow.",
                "Creating a separate navigation graph for each step of the flow.",
                "Employing a state machine or a dedicated flow controller in conjunction with Compose Navigation to manage the flow state and navigation decisions.",
                "Using nested navigation graphs with a large number of nested levels."
            ),
            topic = "Compose Navigation",
            correctAnswerIndex = 2,
            explanation = "Employing a state machine or a dedicated flow controller in conjunction with Compose Navigation allows for robust and maintainable management of complex UI flows. This approach separates the flow logic from the UI composables, making it easier to test, debug, and modify the flow.",
            tags = listOf("UIFlow", "StateManagement", "StateMachine", "FlowController")
        ),
        Question(
            question = "In Clean Architecture, how does the Dependency Rule enforce the isolation of inner layers from outer layers, and why is this crucial for maintainability?",
            options = listOf(
                "Outer layers depend on inner layers; it allows for direct access to data models.",
                "Inner layers depend on outer layers; it facilitates UI-driven development.",
                "Both inner and outer layers can depend on each other, as long as interfaces are used.",
                "Inner layers must not depend on outer layers; it allows changes in outer layers without affecting core business logic."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 3,
            explanation = "The Dependency Rule ensures that inner layers, containing core business logic, are insulated from changes in outer layers like UI or databases. This unidirectional dependency flow is vital for maintainability, as it minimizes the ripple effect of changes.",
            tags = listOf("DependencyRule", "Layers", "Maintainability", "BusinessLogic")
        ),
        Question(
            question = "When implementing use cases in Clean Architecture, what is the most effective strategy for handling complex business logic that involves interactions with multiple entities and external services?",
            options = listOf(
                "Placing all business logic directly within the use case classes.",
                "Creating a single, monolithic use case class that handles all interactions.",
                "Decomposing the logic into smaller, focused use case classes and coordinating them using an orchestrator or a domain service.",
                "Using a global state manager to coordinate interactions between entities and services."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 3,
            explanation = "Decomposing complex logic into smaller, focused use cases and coordinating them using an orchestrator or a domain service promotes modularity and testability. This approach prevents use cases from becoming overly complex and adheres to the Single Responsibility Principle.",
            tags = listOf("UseCases", "BusinessLogic", "DomainService", "Modularity")
        ),
        Question(
            question = "In Clean Architecture, how should data transfer objects (DTOs) be used to facilitate communication between different layers, and what are the key benefits of using DTOs?",
            options = listOf(
                "DTOs should be shared directly between all layers to minimize data duplication.",
                "DTOs should be used only for communication between the UI and the domain layer.",
                "DTOs should be defined within the domain layer and used by all outer layers.",
                "DTOs should be defined in the outer layers and mapped to domain entities, providing a layer of abstraction and preventing domain leakage."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 3,
            explanation = "DTOs defined in the outer layers and mapped to domain entities provide a layer of abstraction, preventing domain leakage into the presentation or data layers. This allows for changes in the outer layers without affecting the domain logic.",
            tags = listOf("DTOs", "Layers", "Abstraction", "DomainLeakage")
        ),
        Question(
            question = "When implementing repositories in Clean Architecture, how can you effectively manage the transition between different data sources (e.g., local database, network API) while maintaining a consistent interface for the use cases?",
            options = listOf(
                "Creating separate repository interfaces for each data source.",
                "Implementing conditional logic within the repository to select the appropriate data source.",
                "Using a single repository interface and implementing data source selection through dependency injection or a strategy pattern.",
                "Relying on a global data source manager to handle data source selection."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 2,
            explanation = "Using a single repository interface and implementing data source selection through dependency injection or a strategy pattern allows for a consistent interface for use cases, while providing flexibility in choosing data sources. This approach promotes testability and maintainability.",
            tags = listOf("Repositories", "DataSources", "DependencyInjection", "StrategyPattern")
        ),
        Question(
            question = "In a multi-module Android project using Clean Architecture, how can you effectively enforce the Dependency Rule and prevent accidental dependencies between modules that violate the architecture?",
            options = listOf(
                "Relying on code reviews to identify and prevent dependency violations.",
                "Using Gradle dependency constraints and lint rules to enforce dependency boundaries and detect violations at compile time.",
                "Implementing a custom build script that analyzes module dependencies and reports violations.",
                "Using a global dependency manager to control module dependencies."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 2,
            explanation = "Using Gradle dependency constraints and lint rules provides automated enforcement of dependency boundaries and detects violations at compile time, preventing accidental dependencies and ensuring adherence to the architecture's principles.",
            tags = listOf("MultiModule", "DependencyRule", "Gradle", "Lint")
        ),
        Question(
            question = "How do you handle cross-cutting concerns like logging, analytics, and error handling in Clean Architecture without violating the Dependency Rule and maintaining a clean separation of concerns?",
            options = listOf(
                "Injecting global logging and analytics services into all layers.",
                "Using aspect-oriented programming (AOP) or interceptors to apply cross-cutting concerns without direct dependencies.",
                "Placing all cross-cutting concerns in the outermost layer.",
                "Relying on a global exception handler to manage all errors."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 1,
            explanation = "AOP or interceptors allow you to apply cross-cutting concerns without introducing direct dependencies into the core layers. This approach maintains a clean separation of concerns and adheres to the Dependency Rule.",
            tags = listOf("CrossCuttingConcerns", "AOP", "Interceptors", "DependencyRule")
        ),
        Question(
            question = "When designing entities in the domain layer of Clean Architecture, what are the key principles for ensuring that they encapsulate business logic and data effectively, and how do you prevent anemic domain models?",
            options = listOf(
                "Creating entities with only data properties and placing all business logic in use cases.",
                "Adding behavior methods to entities that directly interact with external services.",
                "Encapsulating business logic within entities and ensuring that they are responsible for their own state and behavior.",
                "Using a global state manager to manage entity behavior."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 2,
            explanation = "Encapsulating business logic within entities and making them responsible for their own state and behavior prevents anemic domain models. This approach ensures that entities are rich with behavior and adhere to the principles of object-oriented design.",
            tags = listOf("Entities", "DomainLayer", "BusinessLogic", "AnemicDomainModel")
        ),
        Question(
            question = "In Clean Architecture, how can you effectively manage asynchronous operations and reactive streams (e.g., using Kotlin Coroutines or RxJava) while maintaining the architecture's principles and ensuring testability?",
            options = listOf(
                "Performing asynchronous operations directly within the use cases without any abstraction.",
                "Exposing reactive streams directly from the domain layer to the presentation layer.",
                "Defining interfaces for asynchronous operations or reactive streams within the domain layer and implementing them in the outer layers.",
                "Using global coroutine scopes or schedulers to manage asynchronous operations."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 3,
            explanation = "Defining interfaces for asynchronous operations or reactive streams within the domain layer and implementing them in the outer layers allows for a clean separation of concerns and ensures testability. This approach maintains the architecture's principles while providing flexibility in handling asynchronous operations.",
            tags = listOf("Asynchronous", "ReactiveStreams", "Coroutines", "Testability")
        ),
        Question(
            question = "How do you implement and test interactors (use cases) in Clean Architecture when they involve complex interactions with multiple repositories and external services, ensuring that the tests are isolated and maintainable?",
            options = listOf(
                "Using integration tests that interact with real repositories and services.",
                "Mocking all dependencies of the interactor and writing unit tests that verify interactions and behavior.",
                "Relying on end-to-end tests to cover interactor behavior.",
                "Using a global test environment to manage interactor dependencies."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 1,
            explanation = "Mocking all dependencies of the interactor and writing unit tests allows for isolated and maintainable tests. This approach ensures that tests are focused on the interactor's behavior and do not rely on external dependencies, making them faster and more reliable.",
            tags = listOf("Interactors", "UseCases", "Testing", "Mocking")
        ),
        Question(
            question = "When adapting Clean Architecture for a microservices architecture, how do you ensure that the principles of the architecture are maintained across service boundaries, and what are the key considerations for inter-service communication?",
            options = listOf(
                "Treating each microservice as a separate application with its own Clean Architecture implementation and using well-defined APIs for communication.",
                "Sharing domain models and use cases across all microservices to minimize code duplication.",
                "Using a global data store to facilitate communication between microservices.",
                "Relying on a centralized orchestration service to manage inter-service communication."
            ),
            topic = "Clean Architecture",
            correctAnswerIndex = 0,
            explanation = "Treating each microservice as a separate application with its own Clean Architecture implementation and using well-defined APIs for communication ensures that the architecture's principles are maintained across service boundaries. This approach promotes autonomy and flexibility, while enabling clear communication between services.",
            tags = listOf("Microservices", "ServiceBoundaries", "APIs", "Communication")
        ),
        Question(
            question = "How can you effectively manage complex background tasks that require precise timing and guaranteed execution, even when the app is in the background or the device is in Doze mode?",
            options = listOf(
                "Using a simple Thread or AsyncTask for background tasks.",
                "Relying solely on WorkManager with periodic work requests and no constraints.",
                "Implementing a combination of WorkManager with specific constraints (e.g., network, charging) and using foreground services for critical tasks.",
                "Using AlarmManager with inexact alarms for all background processes."
            ),
            topic = "Core Android Components",
            correctAnswerIndex = 2,
            explanation = "WorkManager with constraints handles deferrable background tasks, and foreground services ensure critical tasks run even with background restrictions. Combining these ensures reliability and respects system limitations.",
            tags = listOf("WorkManager", "ForegroundServices", "BackgroundTasks", "DozeMode")
        ),
        Question(
            question = "In a scenario where you need to implement inter-process communication (IPC) between different applications on an Android device, what is the most robust and secure approach?",
            options = listOf(
                "Using shared preferences or files for data exchange.",
                "Employing implicit intents for data transfer between apps.",
                "Implementing a custom content provider or using AIDL (Android Interface Definition Language) with bound services.",
                "Using broadcast receivers for direct data transmission."
            ),
            topic = "Core Android Components",
            correctAnswerIndex = 2,
            explanation = "Content providers and AIDL with bound services provide structured and secure IPC, allowing controlled data access and defined interfaces for inter-application communication.",
            tags = listOf("IPC", "ContentProvider", "AIDL", "BoundServices")
        ),
        Question(
            question = "How can you efficiently manage and persist large datasets in an Android application while ensuring data integrity and performance, especially when dealing with complex relational data?",
            options = listOf(
                "Storing data as JSON files in the app's internal storage.",
                "Using shared preferences for all data persistence.",
                "Implementing a custom SQLite database with raw queries and manual data management.",
                "Utilizing Room Persistence Library with defined entities, DAOs, and database migrations."
            ),
            topic = "Core Android Components",
            correctAnswerIndex = 3,
            explanation = "Room provides an abstraction layer over SQLite, offering compile-time checks, structured data management, and simplified database migrations, ensuring data integrity and performance.",
            tags = listOf("Room", "Persistence", "SQLite", "DataIntegrity")
        ),
        Question(
            question = "Given an Android application that needs to respond to various system events (e.g., network connectivity changes, battery status), how can you effectively implement and manage broadcast receivers while minimizing battery drain?",
            options = listOf(
                "Registering a single, global broadcast receiver for all system events.",
                "Using static broadcast receivers for all events, even when the app is not running.",
                "Registering dynamic broadcast receivers only when needed and unregistering them promptly, and using JobScheduler or WorkManager for background tasks triggered by events.",
                "Relying on polling system services for event updates."
            ),
            topic = "Core Android Components",
            correctAnswerIndex = 3,
            explanation = "Dynamic receivers and background task schedulers prevent unnecessary background processing, reducing battery drain and improving app performance.",
            tags = listOf("BroadcastReceivers", "SystemEvents", "JobScheduler", "BatteryDrain")
        ),
        Question(
            question = "How can you effectively manage the lifecycle of a Service in Android, particularly when dealing with long-running tasks that need to survive configuration changes and app restarts?",
            options = listOf(
                "Using a started service without any lifecycle management.",
                "Relying solely on bound services for all long-running tasks.",
                "Implementing a combination of started services with sticky intents for critical tasks and using foreground services with notifications.",
                "Using only IntentService for all service related tasks."
            ),
            topic = "Core Android Components",
            correctAnswerIndex = 3,
            explanation = "Started services with sticky intents and foreground services ensure reliable execution of long-running tasks, even during configuration changes and app restarts, by properly managing the service's lifecycle.",
            tags = listOf("Services", "Lifecycle", "StartedServices", "ForegroundServices")
        ),
        Question(
            question = "How can you effectively handle backpressure in a complex Kotlin Flow pipeline involving multiple operators and asynchronous data sources, ensuring that the consumer is not overwhelmed by the producer?",
            options = listOf(
                "Ignoring backpressure and relying on the consumer to handle it.",
                "Using `buffer()` with an unlimited capacity to buffer all emitted values.",
                "Employing `conflate()` or `collectLatest()` to process only the latest emitted value, or using `buffer()` with a fixed capacity and appropriate onBufferOverflow strategy.",
                "Blocking the producer thread until the consumer is ready to receive more data."
            ),
            topic = "Kotlin Flows",
            correctAnswerIndex = 2,
            explanation = "`conflate()` and `collectLatest()` discard older values to prevent overwhelming the consumer, while `buffer()` with a fixed capacity and overflow strategies provides more granular control over backpressure.",
            tags = listOf("Backpressure", "FlowOperators", "Concurrency", "Asynchronous")
        ),
        Question(
            question = "Given a scenario where you need to implement a complex state machine using Kotlin Flows, how can you effectively manage the state transitions and handle side effects while ensuring testability and maintainability?",
            options = listOf(
                "Using a single, mutable state variable and updating it directly within the Flow's collect block.",
                "Employing `scan()` or `transformLatest()` to manage state transitions and using side effects within the Flow's operators.",
                "Relying on a global state manager to manage state transitions and side effects.",
                "Using `SharedFlow` with a large buffer to store all state changes."
            ),
            topic = "Kotlin Flows",
            correctAnswerIndex = 1,
            explanation = "`scan()` and `transformLatest()` allow for state transitions within the Flow pipeline, while encapsulating side effects within the Flow's operators ensures testability and maintainability.",
            tags = listOf("StateManagement", "StateMachine", "FlowOperators", "SideEffects")
        ),
        Question(
            question = "How can you effectively combine multiple hot flows (e.g., `SharedFlow` or `StateFlow`) with different emission rates and update frequencies into a single, unified Flow while ensuring that the latest values from all flows are always reflected?",
            options = listOf(
                "Using `combine()` or `zip()` with all hot flows, regardless of their emission rates.",
                "Employing `merge()` to combine all hot flows into a single Flow.",
                "Using `combine()` with `distinctUntilChanged()` on each hot flow to prevent unnecessary emissions, and handling potential race conditions with appropriate synchronization.",
                "Relying on a global event bus to combine all flow emissions."
            ),
            topic = "Kotlin Flows",
            correctAnswerIndex = 3,
            explanation = "`combine()` with `distinctUntilChanged()` ensures that only the latest, distinct values from each hot flow are combined, preventing unnecessary emissions and handling potential race conditions.",
            tags = listOf("HotFlows", "Combine", "DistinctUntilChanged", "Concurrency")
        ),
        Question(
            question = "In a scenario where you need to implement a Flow that performs resource-intensive operations (e.g., network requests, database queries) and handle potential errors or exceptions gracefully, how can you effectively manage cancellation and error handling?",
            options = listOf(
                "Ignoring cancellation and error handling within the Flow's operators.",
                "Using `catch()` and `retry()` operators with simple error handling logic and relying on external cancellation mechanisms.",
                "Employing `catch()` and `retry()` operators with granular error handling logic, using `onCompletion()` to handle cancellation and resource cleanup, and leveraging structured concurrency for cancellation.",
                "Relying on a global exception handler to manage all errors and cancellations."
            ),
            topic = "Kotlin Flows",
            correctAnswerIndex = 2,
            explanation = "`catch()` and `retry()` provide granular error handling, `onCompletion()` handles cancellation and resource cleanup, and structured concurrency ensures proper cancellation propagation.",
            tags = listOf("ErrorHandling", "Cancellation", "FlowOperators", "StructuredConcurrency")
        ),
        Question(
            question = "How can you effectively test a complex Kotlin Flow pipeline that involves multiple operators, asynchronous data sources, and side effects, ensuring that the tests are isolated, deterministic, and maintainable?",
            options = listOf(
                "Using integration tests that interact with real data sources and external services.",
                "Relying on end-to-end tests to cover all Flow pipeline scenarios.",
                "Employing `runTest` and `turbine` to write unit tests that simulate asynchronous data sources, verify emissions, and handle cancellation and error scenarios deterministically.",
                "Using a global test environment to manage Flow pipeline dependencies."
            ),
            topic = "Kotlin Flows",
            correctAnswerIndex = 3,
            explanation = "`runTest` and `turbine` allow for isolated, deterministic unit tests of Flow pipelines, simulating asynchronous data sources and verifying emissions and behavior.",
            tags = listOf("Testing", "FlowOperators", "RunTest", "Turbine")
        )
    )
}