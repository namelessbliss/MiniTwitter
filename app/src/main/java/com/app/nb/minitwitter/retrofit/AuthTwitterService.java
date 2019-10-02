package com.app.nb.minitwitter.retrofit;

import com.app.nb.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.nb.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthTwitterService {

    /**
     * Peticion para obtener todos los tweets registrados
     *
     * @return
     */
    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    /**
     * Peticion para enviar un tweet
     *
     * @return
     */
    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTweet requestCreateTweet);


}
