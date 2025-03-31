package com.kesicollection.data.api

import com.kesicollection.core.model.Difficulty
import com.kesicollection.core.model.Question
import com.kesicollection.core.model.QuestionType
import com.kesicollection.core.model.Tag
import com.kesicollection.core.model.Topic

interface QuestionApi {
    suspend fun fetchQuestions(): Result<List<Question>>
}

fun mockedQuestions(): List<Question> {
    return listOf(
        Question(
            questionType = QuestionType.CodeSnippet,
            content = "Pattern with inherent challenges in managing complex side effects:",
            options = listOf("MVC", "MVP", "MVVM", "MVI"),
            topic = Topic(name = "Android Architecture"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "MVC. Due to the Controller's direct interaction with both Model and View, managing asynchronous operations and complex side effects becomes convoluted, often leading to tightly coupled code and increased difficulty in testing and maintenance.",
            tags = listOf(Tag(name = "MVC"), Tag(name = "Side Effects"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Architecture where the View's role is strictly limited to rendering UI, with minimal logic:",
            options = listOf("MVC", "MVP", "MVVM", "MVI"),
            topic = Topic(name = "Android Architecture"),
            correctAnswerIndex = 1,
            difficulty = Difficulty.Hard,
            explanation = "MVP. The View in MVP is designed to be 'dumb' or 'passive,' focusing solely on displaying data and forwarding user interactions to the Presenter. This separation allows for cleaner, more testable UI code, as the Presenter handles all UI logic and data manipulation.",
            tags = listOf(Tag(name = "MVP"), Tag(name = "View Role"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Pattern that leverages observable data streams for efficient UI updates and state management:",
            options = listOf("MVC", "MVP", "MVVM", "MVI"),
            topic = Topic(name = "Android Architecture"),
            correctAnswerIndex = 2,
            difficulty = Difficulty.Hard,
            explanation = "MVVM. By utilizing data binding and reactive programming principles, MVVM enables the View to observe changes in the ViewModel's data streams. This approach simplifies UI updates, reduces boilerplate code, and enhances the overall responsiveness and maintainability of the application.",
            tags = listOf(Tag(name = "MVVM"), Tag(name = "Reactive Streams"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Architecture that enforces a strict unidirectional data flow, simplifying state predictability:",
            options = listOf("MVC", "MVP", "MVVM", "MVI"),
            topic = Topic(name = "Android Architecture"),
            correctAnswerIndex = 3,
            difficulty = Difficulty.Hard,
            explanation = "MVI. The core principle of MVI is the unidirectional flow of data, where user Intents trigger state changes in the Model, which are then rendered by the View. This pattern eliminates the complexities of two-way data binding and facilitates easier debugging and testing due to the predictable nature of state transitions.",
            tags = listOf(Tag(name = "MVI"), Tag(name = "Unidirectional Flow"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Architecture that necessitates a deep understanding of reactive programming for effective implementation:",
            options = listOf("MVC", "MVP", "MVVM", "MVI"),
            topic = Topic(name = "Android Architecture"),
            correctAnswerIndex = 3,
            difficulty = Difficulty.Hard,
            explanation = "MVI. To fully leverage the benefits of MVI, developers must be proficient in reactive programming paradigms, such as RxJava or Kotlin Coroutines with Flow. The reactive nature of MVI enables efficient handling of asynchronous operations and complex state changes, but it also introduces a steeper learning curve compared to other architectural patterns.",
            tags = listOf(Tag(name = "MVI"), Tag(name = "Reactive Programming"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "In SRP, what is the ideal number of reasons a class should have to change?",
            options = listOf("One", "Multiple", "Zero", "Variable"),
            topic = Topic(name = "SOLID Principles"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "According to the Single Responsibility Principle (SRP), a class should have only 'One' reason to change. This ensures that each class has a single, well-defined responsibility, making it easier to maintain and modify. Multiple reasons for change indicate that the class is doing too much and should be refactored.",
            tags = listOf(Tag(name = "SRP"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "OCP is achieved through what mechanism that allows for extension without modification?",
            options = listOf("Abstraction", "Inheritance", "Composition", "Mutation"),
            topic = Topic(name = "SOLID Principles"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "The Open/Closed Principle (OCP) is best achieved through 'Abstraction.' Abstractions, like interfaces or abstract classes, provide a way to define contracts that can be implemented by different classes. This allows you to add new functionality by creating new implementations of the abstraction without modifying the existing code that depends on it.",
            tags = listOf(Tag(name = "OCP"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "LSP implies that a subclass should be what, in relation to its superclass?",
            options = listOf("Substitutable", "Independent", "Dependent", "Unique"),
            topic = Topic(name = "SOLID Principles"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "The Liskov Substitution Principle (LSP) emphasizes that a subclass must be 'Substitutable' for its superclass. This means that any place in the code where a superclass is used, a subclass can be used without causing errors or unexpected behavior. This ensures that inheritance is used correctly and maintains the integrity of the program.",
            tags = listOf(Tag(name = "LSP"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "ISP advocates for how many interfaces per client, ideally?",
            options = listOf("Specific", "General", "Variable", "Minimal"),
            topic = Topic(name = "SOLID Principles"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "The Interface Segregation Principle (ISP) advocates for 'Specific' interfaces per client. Instead of having large, general-purpose interfaces, ISP recommends creating smaller, more focused interfaces that are tailored to the needs of specific clients. This prevents clients from being forced to depend on methods they don't use, leading to cleaner and more maintainable code.",
            tags = listOf(Tag(name = "ISP"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "DIP relies on what programming paradigm to reduce coupling?",
            options = listOf("Abstraction", "Encapsulation", "Polymorphism", "Inheritance"),
            topic = Topic(name = "SOLID Principles"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "The Dependency Inversion Principle (DIP) relies on 'Abstraction' to reduce coupling between high-level and low-level modules. By depending on abstractions rather than concrete implementations, modules become less tightly coupled, making the code more flexible, testable, and maintainable. Abstractions provide a layer of indirection that allows modules to be swapped or replaced without affecting other parts of the system.",
            tags = listOf(Tag(name = "DIP"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "In Clean Architecture, which layer should be completely independent of external frameworks like Android SDK?",
            options = listOf("UI", "Domain", "Data", "Presentation"),
            topic = Topic(name = "Clean Architecture"),
            correctAnswerIndex = 1,
            difficulty = Difficulty.Hard,
            explanation = "The Domain layer contains core business logic and should be framework-agnostic to ensure testability and maintainability. It represents the 'inner' circle and should not depend on outer circles.",
            tags = listOf(Tag(name = "Layering"), Tag(name = "Framework Independence"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What design principle does Clean Architecture primarily leverage to achieve separation of concerns?",
            options = listOf("SOLID", "KISS", "DRY", "YAGNI"),
            topic = Topic(name = "Clean Architecture"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "SOLID principles, particularly the Single Responsibility Principle and Dependency Inversion Principle, are fundamental to Clean Architecture. These principles ensure that each layer has a single responsibility and that dependencies flow inwards.",
            tags = listOf(Tag(name = "Design Principles"), Tag(name = "SOLID"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "In Clean Architecture, what serves as the primary interface between the Domain and Data layers?",
            options = listOf("Interactor", "ViewModel", "Repository", "Entity"),
            topic = Topic(name = "Clean Architecture"),
            correctAnswerIndex = 2,
            difficulty = Difficulty.Hard,
            explanation = "The Repository pattern acts as an abstraction over the data layer, providing a clean interface for the Domain layer to access data without needing to know the specifics of data sources or storage mechanisms.",
            tags = listOf(Tag(name = "Repositories"), Tag(name = "Data Layer"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is the most significant challenge in implementing Clean Architecture in a small Android project?",
            options = listOf("Performance", "Complexity", "Testing", "UI/UX"),
            topic = Topic(name = "Clean Architecture"),
            correctAnswerIndex = 1,
            difficulty = Difficulty.Hard,
            explanation = "The initial complexity and perceived overhead of setting up Clean Architecture can be a significant challenge, especially in small projects where the benefits of maintainability and testability may not be immediately apparent.",
            tags = listOf(Tag(name = "Challenges"), Tag(name = "Project Size"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which pattern is frequently used within the presentation layer of Clean Architecture to manage UI state and logic?",
            options = listOf("MVC", "MVP", "MVVM", "MVI"),
            topic = Topic(name = "Clean Architecture"),
            correctAnswerIndex = 2,
            difficulty = Difficulty.Hard,
            explanation = "MVVM (Model-View-ViewModel) is commonly used in the presentation layer of Clean Architecture. It aligns well with the separation of concerns, providing a testable and maintainable way to manage UI state and logic.",
            tags = listOf(Tag(name = "Presentation Layer"), Tag(name = "MVVM"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What design principle is primarily used in composition to create 'has-a' relationships?",
            options = listOf("Aggregation", "Delegation", "Inheritance", "Polymorphism"),
            topic = Topic(name = "Composition vs Inheritance"),
            correctAnswerIndex = 1,
            difficulty = Difficulty.Hard,
            explanation = "Delegation is the core principle in composition. It involves one object forwarding or delegating tasks to another object, which is part of its composition. This creates a 'has-a' relationship, where one class contains another class as a member and uses its functionality."
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which problem is often associated with deep inheritance hierarchies?",
            options = listOf("Encapsulation", "Loose coupling", "Fragile base class", "Testability"),
            topic = Topic(name = "Composition vs Inheritance"),
            correctAnswerIndex = 2,
            difficulty = Difficulty.Hard,
            explanation = "The 'fragile base class' problem occurs when modifications to a base class unintentionally break derived classes due to tight coupling in deep inheritance hierarchies. Composition aims to avoid this by favoring loosely coupled components.",
        ),
        Question(
            questionType = QuestionType.Text,
            content = "In Kotlin, which keyword is used to implement delegation in composition?",
            options = listOf("extends", "implements", "by", "with"),
            topic = Topic(name = "Composition vs Inheritance"),
            correctAnswerIndex = 2,
            difficulty = Difficulty.Hard,
            explanation = "The 'by' keyword in Kotlin is specifically used for delegation, allowing a class to delegate the implementation of an interface to another object. This simplifies composition by reducing boilerplate code.",
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What type of relationship is established by inheritance?",
            options = listOf("Has-a", "Uses-a", "Is-a", "Like-a"),
            topic = Topic(name = "Composition vs Inheritance"),
            correctAnswerIndex = 2,
            difficulty = Difficulty.Hard,
            explanation = "Inheritance establishes an 'is-a' relationship, meaning a subclass is a specialized version of its superclass. This contrasts with composition, which establishes a 'has-a' relationship.",
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which paradigm promotes loose coupling and improved testability?",
            options = listOf("Inheritance", "Composition", "Polymorphism", "Abstraction"),
            topic = Topic(name = "Composition vs Inheritance"),
            correctAnswerIndex = 1,
            difficulty = Difficulty.Hard,
            explanation = "Composition promotes loose coupling and improved testability because it allows for the creation of independent, reusable components that can be easily swapped or mocked during testing. This is a key advantage over inheritance, which often leads to tighter coupling.",
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which architectural pattern is commonly used to separate UI logic from business logic in Android?",
            options = listOf("MVVM", "MVC", "MVP", "VIPER"),
            topic = Topic(name = "Architecture"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Medium,
            explanation = "MVVM (Model-View-ViewModel) separates the UI (View) from the data (Model) and introduces a ViewModel to handle UI logic.",
            tags = listOf(Tag(name = "Architecture"), Tag(name = "MVVM"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What Kotlin feature is preferred for handling continuous data streams in Android?",
            options = listOf("Flow", "LiveData", "Coroutine", "AsyncTask"),
            topic = Topic(name = "Data Flow"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Medium,
            explanation = "Flow is a Kotlin Coroutines feature designed for handling asynchronous streams of data.",
            tags = listOf(Tag(name = "Kotlin"), Tag(name = "Flow"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is the recommended approach for representing UI state in a modern Android application?",
            options = listOf("StateFlow", "MutableList", "Bundle", "HashMap"),
            topic = Topic(name = "State Management"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "StateFlow is a state-holder observable flow that emits current and new state updates. It's ideal for representing UI state because it ensures the UI always has the latest state.",
            tags = listOf(Tag(name = "State Management"), Tag(name = "StateFlow"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which utility class is used for efficient updates of RecyclerView items?",
            options = listOf("DiffUtil", "ArrayAdapter", "CursorAdapter", "SimpleAdapter"),
            topic = Topic(name = "UI Updates"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Medium,
            explanation = "DiffUtil calculates the minimal set of changes between two lists, allowing for efficient RecyclerView updates.",
            tags = listOf(Tag(name = "RecyclerView"), Tag(name = "DiffUtil"))
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What pattern should be used to encapsulate Business Logic?",
            options = listOf("UseCase", "Repository", "ViewModel", "DataClass"),
            topic = Topic(name = "Data Flow"),
            correctAnswerIndex = 0,
            difficulty = Difficulty.Hard,
            explanation = "The UseCase pattern is used to encapsulate business logic, allowing for cleaner separation of concerns and improved testability.",
            tags = listOf(Tag(name = "Data Flow"), Tag(name = "UseCase"))
        )
    )
}