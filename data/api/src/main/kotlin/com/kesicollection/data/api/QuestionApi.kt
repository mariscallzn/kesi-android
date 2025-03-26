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
    val topics = listOf(
        Topic(name = "Kotlin Basics"),
        Topic(name = "Android UI"),
        Topic(name = "Data Structures"),
        Topic(name = "Algorithms"),
        Topic(name = "Networking")
    )

    val tags = listOf(
        Tag(name = "Variables"),
        Tag(name = "Layouts"),
        Tag(name = "Lists"),
        Tag(name = "Sorting"),
        Tag(name = "HTTP")
    )

    return listOf(
        Question(
            questionType = QuestionType.Text,
            content = "What keyword is used to declare a constant in Kotlin?",
            options = listOf("var", "val", "const", "let"),
            topic = topics[0],
            correctAnswerIndex = 1,
            difficulty = Difficulty.Easy,
            explanation = "val is used for read-only properties.",
            tags = listOf(tags[0])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which layout is used to arrange views in a linear direction?",
            options = listOf("RelativeLayout", "LinearLayout", "ConstraintLayout", "FrameLayout"),
            topic = topics[1],
            correctAnswerIndex = 1,
            difficulty = Difficulty.Easy,
            explanation = "LinearLayout arranges views in a single column or row.",
            tags = listOf(tags[1])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which data structure follows the LIFO principle?",
            options = listOf("Queue", "Stack", "List", "Set"),
            topic = topics[2],
            correctAnswerIndex = 1,
            difficulty = Difficulty.Medium,
            explanation = "Stack uses Last-In-First-Out.",
            tags = listOf(tags[2])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is the time complexity of the bubble sort algorithm?",
            options = listOf("O(n)", "O(log n)", "O(n log n)", "O(n^2)"),
            topic = topics[3],
            correctAnswerIndex = 3,
            difficulty = Difficulty.Medium,
            explanation = "Bubble sort has a quadratic time complexity.",
            tags = listOf(tags[3])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which HTTP method is used to retrieve data?",
            options = listOf("POST", "PUT", "GET", "DELETE"),
            topic = topics[4],
            correctAnswerIndex = 2,
            difficulty = Difficulty.Easy,
            explanation = "GET is used for retrieving data.",
            tags = listOf(tags[4])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Kotlin is statically typed.",
            options = listOf("True", "False"),
            topic = topics[0],
            correctAnswerIndex = 0,
            difficulty = Difficulty.Easy,
            explanation = "Kotlin's type system is statically checked at compile time.",
            tags = listOf(tags[0])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is the purpose of RecyclerView in Android?",
            options = listOf(
                "Displaying a single view",
                "Displaying a scrollable list of views",
                "Handling user input",
                "Playing audio"
            ),
            topic = topics[1],
            correctAnswerIndex = 1,
            difficulty = Difficulty.Medium,
            explanation = "RecyclerView efficiently displays large datasets.",
            tags = listOf(tags[1], tags[2])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which data structure uses key-value pairs?",
            options = listOf("List", "Set", "Map", "Queue"),
            topic = topics[2],
            correctAnswerIndex = 2,
            difficulty = Difficulty.Medium,
            explanation = "Maps store data in key-value pairs.",
            tags = listOf(tags[2])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is the time complexity of binary search?",
            options = listOf("O(n)", "O(log n)", "O(n log n)", "O(n^2)"),
            topic = topics[3],
            correctAnswerIndex = 1,
            difficulty = Difficulty.Medium,
            explanation = "Binary search has a logarithmic time complexity.",
            tags = listOf(tags[3])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is the purpose of the Retrofit library in Android?",
            options = listOf(
                "Image processing",
                "Database management",
                "Networking",
                "Playing videos"
            ),
            topic = topics[4],
            correctAnswerIndex = 2,
            difficulty = Difficulty.Medium,
            explanation = "Retrofit simplifies making network requests.",
            tags = listOf(tags[4])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What does the '!!' operator do in Kotlin?",
            options = listOf("Safe call", "Elvis operator", "Not-null assertion", "Type checking"),
            topic = topics[0],
            correctAnswerIndex = 2,
            difficulty = Difficulty.Medium,
            explanation = "It throws a NullPointerException if the value is null.",
            tags = listOf(tags[0])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "Which Android component is used for background operations?",
            options = listOf("Activity", "Fragment", "Service", "View"),
            topic = topics[1],
            correctAnswerIndex = 2,
            difficulty = Difficulty.Medium,
            explanation = "Services run in the background without a UI.",
            tags = listOf(tags[1])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is a linked list?",
            options = listOf(
                "A linear data structure with contiguous memory allocation",
                "A non-linear data structure",
                "A linear data structure with nodes pointing to the next node",
                "A data structure with key-value pairs"
            ),
            topic = topics[2],
            correctAnswerIndex = 3,
            difficulty = Difficulty.Medium,
            explanation = "Linked lists use nodes with pointers.",
            tags = listOf(tags[2])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is Dijkstra's algorithm used for?",
            options = listOf("Sorting", "Searching", "Shortest path finding", "Data compression"),
            topic = topics[3],
            correctAnswerIndex = 2,
            difficulty = Difficulty.Hard,
            explanation = "Dijkstra's algorithm finds the shortest path.",
            tags = listOf(tags[3])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What does REST stand for?",
            options = listOf(
                "Representational State Transfer",
                "Resource Exchange Transfer",
                "Remote State Transmission",
                "Request Encoding Technique"
            ),
            topic = topics[4],
            correctAnswerIndex = 0,
            difficulty = Difficulty.Medium,
            explanation = "REST is an architectural style for distributed systems.",
            tags = listOf(tags[4])
        ),
        Question(
            questionType = QuestionType.Text,
            content = "What is a sealed class in Kotlin?",
            options = listOf(
                "A class that cannot be inherited",
                "A class that can only be inherited within the same file",
                "A class that restricts the class hierarchy",
                "A class that can be inherited from any file"
            ),
            topic = topics[0],
            correctAnswerIndex = 2,
            difficulty = Difficulty.Medium,
            explanation = "Sealed classes restrict class hierarchies.",
            tags = listOf(tags[0])
        )
    )
}