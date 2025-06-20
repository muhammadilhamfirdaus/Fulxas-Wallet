package com.kelompok3.fulxas.models;

import com.kelompok3.fulxas.models.Article;

import java.util.List;

public class NewsResponse {
    private String status;
    private int totalResults;
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }
}
