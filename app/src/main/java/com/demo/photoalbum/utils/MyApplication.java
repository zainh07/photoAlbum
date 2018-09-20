package com.demo.photoalbum.utils;

import android.app.Application;
import android.content.Context;

import com.demo.photoalbum.interfaces.MyApis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MyApplication extends Application {

    private static MyApis apiService;
    public static Context APPLICATION_CONTEXT;
    public static final String TAG = MyApplication.class.getName();
    private static MyApplication mInstance;
    public static synchronized MyApis getApiService() {
       return apiService;
   }

    @Override
    public void onCreate() {
        super.onCreate();
        initApiService();
        APPLICATION_CONTEXT = getApplicationContext();
        mInstance = this;
        initApiService();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void initApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // init cookie manager
        CookieHandler cookieHandler = new CookieManager();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String url = "";

        Retrofit retrofit = new Retrofit.Builder()
               .baseUrl("https://auth.dev.waldo.photos")  //main server
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         apiService = retrofit.create(MyApis.class);

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }







}
