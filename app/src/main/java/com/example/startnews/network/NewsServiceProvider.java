
package com.example.startnews.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsServiceProvider {

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static NewsServiceApi newsApiService;

    public static NewsServiceApi getNewsApiService() {
        if (newsApiService == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            newsApiService = retrofit.create(NewsServiceApi.class);
        }

        return newsApiService;
    }
}
