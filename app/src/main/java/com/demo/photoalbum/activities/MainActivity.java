package com.demo.photoalbum.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.demo.photoalbum.utils.MyApplication;
import com.demo.photoalbum.R;
import com.demo.photoalbum.adapter.PhotosAdapter;
import com.demo.photoalbum.model.Photos_Vm;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String getPhotosUrl="https://core-graphql.dev.waldo.photos/gql?query=query {\n" +
            "  album(id: \"dTRydwXhGQgthi1r2cKFmg\") {\n" +
            "    id,\n" +
            "    name,\n" +
            "    photos(slice: { limit: 100, offset: 0 }) {\n" +
            "      count\n" +
            "      records {\n" +
            "        urls {\n" +
            "          size_code\n" +
            "          url\n" +
            "          width\n" +
            "          height\n" +
            "          quality\n" +
            "          mime\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    RecyclerView recycler_Photo;
    ArrayList<Photos_Vm> listOfPhotos;
    PhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        listOfPhotos=new ArrayList<>();

        if (isNetworkAvailable()) {
            login();
        }else {
            Toast.makeText(MainActivity.this, "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    //Initialization View elements
    public void init(){
        recycler_Photo=(RecyclerView)findViewById(R.id.recycler_photots);
        recycler_Photo.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        recycler_Photo.setNestedScrollingEnabled(false);
    }

    /**
     * Authentication API Call*/

    @SuppressLint("LongLogTag")
    public void login() {
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> call = MyApplication.getApiService().login("https://auth.dev.waldo.photos","waldo-android","1234");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                progressDialog.cancel();
                getAllPhotos();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                t.printStackTrace();
                progressDialog.cancel();
                getAllPhotos();
            }
        });
    }



    /***
     * Get Photos API Call**/

    public void getAllPhotos(){
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> call = MyApplication.getApiService().getPhotos(getPhotosUrl);
        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                progressDialog.dismiss();
                listOfPhotos.clear();
                /**
                 * JSON Parsing*/
                try {


                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject jsonObjectdata = jsonObject.get("data").getAsJsonObject();
                    JsonObject jsonObjectalbum = jsonObjectdata.get("album").getAsJsonObject();
                    JsonObject jsonObjectPhotos = jsonObjectalbum.get("photos").getAsJsonObject();
                    JsonArray jsonArrayrecords = jsonObjectPhotos.get("records").getAsJsonArray();
                    for (JsonElement jsonElement : jsonArrayrecords) {
                        JsonObject jsonObject_Inner = jsonElement.getAsJsonObject();
                        JsonArray jsonArray_url = jsonObject_Inner.get("urls").getAsJsonArray();
                        for (JsonElement element : jsonArray_url) {
                            JsonObject jsonObject_urlInner = element.getAsJsonObject();
                            String size_code = jsonObject_urlInner.get("size_code").getAsString();
                            String url = jsonObject_urlInner.get("url").getAsString();
                            int width = jsonObject_urlInner.get("width").getAsInt();
                            int height = jsonObject_urlInner.get("height").getAsInt();
                            float quality = jsonObject_urlInner.get("quality").getAsFloat();
                            String mime = jsonObject_urlInner.get("mime").getAsString();

                            Photos_Vm photos_vm = new Photos_Vm(size_code, url, width, height, quality, mime);
                            listOfPhotos.add(photos_vm);
                        }
                    }
                }catch (NullPointerException e){

                }
                /**
                 * Passing data to adapter*/
                photosAdapter=new PhotosAdapter(MainActivity.this,listOfPhotos);
                //adding adapter to recyclerview
                recycler_Photo.setAdapter(photosAdapter);
                photosAdapter.notifyDataSetChanged();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
