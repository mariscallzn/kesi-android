package com.kesicollection.feature.quiz

import com.kesicollection.core.model.Question

data class QuizUiState(
    val questions: List<Question> = emptyList()
)

fun generateQuestions(): List<Question> {
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
        )
    )
}

val initialState = QuizUiState().copy(questions = generateQuestions())
