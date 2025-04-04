package com.kesicollection.data.retrofit.service

import com.kesicollection.data.retrofit.model.kesiandroid.NetworkArticle
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkIndexArticle
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkPodcast
import com.kesicollection.data.retrofit.model.kesiandroid.lorem
import com.kesicollection.data.retrofit.model.kesiandroid.sections
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface KesiAndroidService {
    @GET("articles/index.json")
    suspend fun fetchAllArticles(): List<NetworkIndexArticle>

    @GET("articles/{id}.json")
    suspend fun getArticleById(
        @Path("id")
        id: String
    ): NetworkArticle
}

//
//val fakeData = object : KesiAndroidService {
//    override suspend fun fetchAllArticles(): Result<List<NetworkIndexArticle>> {
//        val json = NetworkArticle(
//            id = "android_compose_comprehensive_testing_research",
//            title = "Comprehensive Research on Android Testing for Jetpack Compose Applications",
//            description = "The landscape of modern Android application development is characterized by increasing complexity, driven by richer user experiences and more intricate functionalities. In this environment, the role of robust testing has become paramount in ensuring software quality and reliability.",
//            thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
//            content = listOf(
//                SubHeader("Introduction to Android Testing in Jetpack Compose"),
//                Paragraph("The landscape of modern Android application development is characterized by increasing complexity, driven by richer user experiences and more intricate functionalities. In this environment, the role of robust testing has become paramount in ensuring software quality and reliability. This is particularly true with the advent of declarative UI frameworks like Jetpack Compose, which, while streamlining UI development, introduces novel paradigms that necessitate specific and well-thought-out testing strategies . "),
//                Paragraph("Jetpack Compose, a contemporary and reactive UI framework for Android, empowers developers to construct visually appealing, responsive, and customizable user interfaces with significantly reduced boilerplate code . This shift towards declarative UI, where developers describe the desired UI state rather than directly manipulating UI elements, requires a fundamental re-evaluation of traditional testing methodologies. While Compose simplifies the process of building user interfaces, the unique aspects of managing state, handling UI recompositions, and testing UI elements defined as composable functions present distinct challenges for developers . "),
//                Code("""
//<WebView
//    android:id="@+id/webview"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent" />
//                """.trimIndent()),
//                BulletList("Release Build Testing", listOf(
//                    "Testing on Real Devices: Verify app compatibility and performance across a range of real devices with different screen sizes, resolutions, and Android versions.",
//                    "Security Verification: Ensure that user data is protected, and the app is free from common security vulnerabilities .",
//                    "Performance Evaluation: Assess the app's responsiveness, speed, and resource utilization, ensuring it meets performance requirements .",
//                    "Handling Interruptions: Test how the app handles interruptions such as incoming calls, messages, and network disruptions without crashing or losing data ."
//                ))
//            ),
//            podcast = NetworkPodcast(
//                id = "todo",
//                title = "Building Rock solid apps: The art of using composeUiTest",
//                url = "",
//                image = ""
//            ),
//        )
//        println(Json.encodeToString(json))
//        return Result.success(
//            testData.map { NetworkIndexArticle(it.id, it.title, it.description, it.thumbnail) }
//        )
//    }
//
//    override suspend fun getArticleById(id: String): Result<NetworkArticle> {
//        return Result.runCatching {
//            testData.find { it.id == id } ?: throw IllegalStateException()
//        }
//    }
//}

val testData = listOf(
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/images/android_compose_comprehensive_testing_research_img.jpg",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
    NetworkArticle(
        id = UUID.randomUUID().toString(),
        title = lorem.take(54),
        description = lorem.take(120),
        thumbnail = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg27sYPdusU5NPkS_XdrPPwOlhQPNa8jHzVaivFqJtGzT3g87dV914Toto-lnTrxK3n8G7mJJX7MszRXnUeuK6wK5EI_ePZAK1pHdaZcxXVZ0feXvCXAIlJQJz2WnzrZlehhDxU31VjvOo/s0/3+things+to+know+for+Modern+Android+Development-Social.png",
        content = sections,
        podcast = NetworkPodcast(
            id = "TODO()",
            title = lorem.take(60),
            url = "TODO()",
            image = "TODO()"
        )
    ),
)