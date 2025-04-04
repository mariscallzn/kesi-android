package com.kesicollection.data.retrofit.service

import com.kesicollection.data.retrofit.model.kesiandroid.NetworkArticle
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkPodcast
import com.kesicollection.data.retrofit.model.kesiandroid.lorem
import com.kesicollection.data.retrofit.model.kesiandroid.sections
import java.util.UUID

interface KesiAndroidService {
    suspend fun fetchAllArticles(): Result<List<NetworkArticle>>
    suspend fun getArticleById(id: String): Result<NetworkArticle>
}

val fakeData = object : KesiAndroidService {
    override suspend fun fetchAllArticles(): Result<List<NetworkArticle>> = Result.success(
        testData
    )

    override suspend fun getArticleById(id: String): Result<NetworkArticle> {
        return Result.runCatching {
            testData.find { it.id == id } ?: throw IllegalStateException()
        }
    }
}

val testData = listOf(
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