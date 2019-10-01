package com.app.nb.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.nb.minitwitter.common.MyApp;
import com.app.nb.minitwitter.retrofit.AuthTwitterClient;
import com.app.nb.minitwitter.retrofit.AuthTwitterService;
import com.app.nb.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthTwitterService authTwitterService;

    private AuthTwitterClient authTwitterClient;

    private LiveData<List<Tweet>> allTweets;

    TweetRepository() {
        retrofitInit();
    }

    private void retrofitInit() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
    }

    public LiveData<List<Tweet>> getAllTweets() {
        final MutableLiveData<List<Tweet>> liveData = new MutableLiveData<>();

        //Llama a la carga de los datos de tweets
        Call<List<Tweet>> listCall = authTwitterService.getAllTweets();
        listCall.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "No se pudo cargar los tweets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        });

        return liveData;
    }
}
