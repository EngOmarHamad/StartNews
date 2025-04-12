package com.example.startnews;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class ArticleWebView  extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_web);

        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progressBar);
        Toolbar toolbar = findViewById(R.id.articles_web_view_toolbar);
        toolbar.setTitle("Article Web View");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);


        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // عند الانتهاء من التحميل
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        // استلام الرابط
        String url = getIntent().getStringExtra("url");
        Log.e("url", url);
        if (url != null) {
            try {
                webView.loadUrl(url);
            } catch (Exception e) {
                Log.e("Exception url ", e.getMessage());            }

        }
    }
}
