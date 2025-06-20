package com.kelompok3.fulxas.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok3.fulxas.R;
import com.kelompok3.fulxas.adapters.NewsAdapter;
import com.kelompok3.fulxas.models.Article;
import com.kelompok3.fulxas.models.NewsResponse;
import com.kelompok3.fulxas.network.NewsApi;
import com.kelompok3.fulxas.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView rvNews;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        rvNews = findViewById(R.id.rvNews);
        rvNews.setLayoutManager(new LinearLayoutManager(this));

        fetchNews();
    }

    private void fetchNews() {
        NewsApi api = RetrofitClient.getRetrofitInstance().create(NewsApi.class);
        Call<NewsResponse> call = api.getGlobalNews("economy", "publishedAt", "644698c934354473b93cbeb5c1172083");


        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body().getArticles();
                    newsAdapter = new NewsAdapter(articles);
                    rvNews.setAdapter(newsAdapter);
                } else {
                    Toast.makeText(NewsActivity.this, "Gagal mengambil berita", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("NewsActivity", "onFailure: ", t);
                Toast.makeText(NewsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
