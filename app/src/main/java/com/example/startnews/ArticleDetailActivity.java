package com.example.startnews;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class ArticleDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleText, authorText, contentText, dateText, sourceText;
    private Button openInWebBtn;

    private String url;  // سيتم تخزين رابط المقالة الأصلي هنا

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_details);

        imageView = findViewById(R.id.detail_image);
        titleText = findViewById(R.id.detail_title);
        authorText = findViewById(R.id.detail_author);
        contentText = findViewById(R.id.detail_content);
        dateText = findViewById(R.id.detail_date);
        sourceText = findViewById(R.id.detail_source);
        openInWebBtn = findViewById(R.id.open_in_web_button);
        Toolbar toolbar = findViewById(R.id.article_details_toolbar);
        toolbar.setTitle("Article Details");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);

        // جلب البيانات من Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String imageUrl = intent.getStringExtra("image");
        String content = intent.getStringExtra("content");
        String date = intent.getStringExtra("date");
        String source = intent.getStringExtra("source");
        url = intent.getStringExtra("url");

        // تعبئة البيانات في الواجهة
        titleText.setText(title);
        authorText.setText(author != null ? author : "Unknown Author");
        contentText.setText(content != null ? content : "No content available.");
        dateText.setText(date);
        sourceText.setText(source);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        openInWebBtn.setOnClickListener(v -> {
            Intent intentWeb = new Intent(ArticleDetailActivity.this, ArticleWebView.class);
            intentWeb.putExtra("url", url); // استخدم المتغير اللي جهزناه من Intent
            startActivity(intentWeb);
        });

    }
}
