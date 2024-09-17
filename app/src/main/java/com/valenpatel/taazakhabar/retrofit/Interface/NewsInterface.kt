package com.valenpatel.taazakhabar.retrofit.Interface

import com.valenpatel.taazakhabar.retrofit.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {
    @GET("top-headlines")
    suspend fun getAllNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ) : NewsResponse
}