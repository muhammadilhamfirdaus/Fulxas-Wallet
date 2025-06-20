package com.kelompok3.fulxas.network;

import com.kelompok3.fulxas.models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("everything")
    Call<NewsResponse> getGlobalNews(
            @Query("q") String query,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );

}
