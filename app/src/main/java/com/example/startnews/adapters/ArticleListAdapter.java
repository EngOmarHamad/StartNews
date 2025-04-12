package com.example.startnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.startnews.ArticleDetailActivity;
import com.example.startnews.R;
import com.example.startnews.models.Article;

import java.util.ArrayList;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.NewsViewHolder> {

    private Context context;
    private ArrayList<Article> articleList;
    private OnViewMoreClickListener onViewMoreClickListener;
    public interface OnViewMoreClickListener {
        void onArticleClick(Article article);
    }
    public void setArticles(ArrayList<Article> articles) {
        this.articleList = articles;
        notifyDataSetChanged();
    }
    public ArticleListAdapter(Context context, ArrayList<Article> articleList, OnViewMoreClickListener onViewMoreClickListener) {
        this.context = context;
        this.articleList = articleList;
        this.onViewMoreClickListener = onViewMoreClickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("author", article.getAuthor());
            intent.putExtra("image", article.getUrlToImage());
            intent.putExtra("content", article.getContent());
            intent.putExtra("date", article.getPublishedAt());
            intent.putExtra("source", article.getSource().getName());
            intent.putExtra("url", article.getUrl());
            context.startActivity(intent);
        });
        holder.bind(article, onViewMoreClickListener);

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, sourceText, dateText;
        ImageView imageView;
        Button viewArticleDetailsButton;

        public NewsViewHolder(@NonNull View itemView) {

            super(itemView);
            titleText = itemView.findViewById(R.id.article_title);
            sourceText = itemView.findViewById(R.id.article_source);
            dateText = itemView.findViewById(R.id.article_date);
            imageView = itemView.findViewById(R.id.article_image);
            viewArticleDetailsButton = itemView.findViewById(R.id.viewArticleDetailsButton);
        }
        public void bind(Article article, OnViewMoreClickListener listener) {
            this.titleText.setText(article.getTitle());
            this.sourceText.setText(article.getSource().getName());
            this.dateText.setText(article.getPublishedAt());

            Glide.with(imageView.getContext())
                    .load(article.getUrlToImage())
                    .placeholder(R.drawable.placeholder)
                    .into(this.imageView);

            viewArticleDetailsButton.setOnClickListener(v -> listener.onArticleClick(article));
        }
    }
}
