package com.kesicollection.data.retrofit

import com.kesicollection.core.model.Article
import com.kesicollection.core.model.ArticleContent
import com.kesicollection.data.api.ArticleApi
import javax.inject.Inject

class RetrofitArticleApi @Inject constructor(
//    private val kesiAndroidService: KesiAndroidService,
) : ArticleApi {
    override suspend fun getAll(): Result<List<Article>> {
        return Result.success(
            listOf(
                Article(
                    "1",
                    "title1",
                    "description1",
                    "thumbnail1"
                ),
                Article(
                    "2",
                    "title2",
                    "description2",
                    "thumbnail2"
                ),
                Article(
                    "3",
                    "title3",
                    "description3",
                    "thumbnail3"
                ),
                Article(
                    "4",
                    "title4",
                    "description4",
                    "thumbnail4"
                ),
                Article(
                    "5",
                    "title5",
                    "description5",
                    "thumbnail5"
                ),
                Article(
                    "6",
                    "title6",
                    "description6",
                    "thumbnail6"
                ),
                Article(
                    "7", "title7", "description7", "thumbnail7"
                )
            )
        )
    }

    override suspend fun getContentById(id: String): Result<ArticleContent> {
        TODO("Not yet implemented")
    }
}