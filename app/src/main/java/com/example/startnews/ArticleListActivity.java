package com.example.startnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.startnews.adapters.ArticleListAdapter;
import com.example.startnews.models.Article;
import com.example.startnews.models.NewsResponse;
import com.example.startnews.network.NewsServiceApi;
import com.example.startnews.network.NewsServiceProvider;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ArticleListActivity extends AppCompatActivity {

    private RecyclerView articlesRecyclerView;
    private ProgressBar loadingIndicator;
    private TextView errorTextView;
    private ArticleListAdapter articleListAdapter;
    private String selectedCountryCode = "us";
    private ArrayList<Article> articlesData = new ArrayList<>();
    private TabLayout categoryTabLayout;

    private final List<String> newsCategories = Arrays.asList(
            "All", "business", "entertainment", "health", "science", "sports", "technology"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_list);

        Spinner categoryDropdown = findViewById(R.id.category_spinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, newsCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryDropdown.setAdapter(categoryAdapter);

        categoryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = newsCategories.get(position);
                fetchArticles(Objects.equals(selectedCategory, "All") ? "" : selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        Toolbar topToolbar = findViewById(R.id.news_list_toolbar);
        topToolbar.setTitle("Latest News");
        topToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(topToolbar);

        articlesRecyclerView = findViewById(R.id.articles_recycler);
        loadingIndicator = findViewById(R.id.progress_bar);
        errorTextView = findViewById(R.id.error_message);

        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        articleListAdapter = new ArticleListAdapter(this, articlesData, article -> {
            if (article != null) {
                try {
                    Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
                    detailIntent.putExtra("title", article.getTitle());
                    detailIntent.putExtra("author", article.getAuthor());
                    detailIntent.putExtra("image", article.getUrlToImage());
                    detailIntent.putExtra("content", article.getContent());
                    detailIntent.putExtra("date", article.getPublishedAt());
                    detailIntent.putExtra("source", article.getSource().getName());
                    detailIntent.putExtra("url", article.getUrl());
                    startActivity(detailIntent);
                } catch (Exception e) {
                    Log.e("ArticleListActivity", "Failed to open article: " + e.getMessage());
                }
            }
        });

        articlesRecyclerView.setAdapter(articleListAdapter);
        fetchArticles("");
    }

    private void fetchArticles(String category) {
        loadingIndicator.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);

        NewsServiceApi api = NewsServiceProvider.getNewsApiService();
        Call<NewsResponse> apiCall = api.getTopHeadlines(selectedCountryCode, category, "b629261e3a214b22bc137255b45ab9be");

        apiCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, retrofit2.Response<NewsResponse> response) {
                loadingIndicator.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Article> fetchedArticles = response.body().getArticles();
                    if (fetchedArticles.isEmpty()) {
                        displayError("No articles found");
                    }
                    articleListAdapter.setArticles(fetchedArticles);
                } else {
                    displayError("Failed to fetch news");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                displayError("Network error: " + t.getMessage());
            }
        });
    }

    private void displayError(String message) {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(message);
    }
}
