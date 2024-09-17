package com.valenpatel.taazakhabar.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.valenpatel.taazakhabar.constants.Constants
import com.valenpatel.taazakhabar.retrofit.Interface.NewsInterface
import com.valenpatel.taazakhabar.retrofit.response.Article
import retrofit2.HttpException
import java.io.IOException

const val STARTING_PAGE_INDEX = 1

class NewsPagingSource(private val newsInterface: NewsInterface) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // Try to return the closest page to the anchorPosition, which is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            // Fetch data from the API, providing parameters like country and category
            val data = newsInterface.getAllNews("in", "business", Constants.API_KEY)

            // Handle successful data load
            LoadResult.Page(
                data = data.articles,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (data.articles.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            // Handle network issues
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // Handle API error responses
            LoadResult.Error(e)
        }
    }
}
