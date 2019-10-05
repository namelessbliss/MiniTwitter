package com.app.nb.minitwitter.retrofit;

import com.app.nb.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.nb.minitwitter.retrofit.request.RequestUserProfile;
import com.app.nb.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.app.nb.minitwitter.retrofit.response.ResponseUserProfile;
import com.app.nb.minitwitter.retrofit.response.Tweet;
import com.app.nb.minitwitter.retrofit.response.TweetDeleted;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthTwitterService {

    // Tweets

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

    /**
     * Peticio para dar like a un tweet filtrando por su id
     *
     * @param idTweet
     * @return
     */
    @POST("tweets/like/{idTweet}")
    Call<Tweet> likeTweet(@Path("idTweet") int idTweet);

    @DELETE("tweets/{idTweet}")
    Call<TweetDeleted> deleteTweet(@Path("idTweet") int idTweet);

    // Users

    /**
     * Obtiene informacion del usuario
     *
     * @return
     */
    @GET("users/profile")
    Call<ResponseUserProfile> getProfile();

    /**
     * Actualiza informacion del usuario
     *
     * @param requestUserProfile
     * @return
     */
    @PUT("users/profile")
    Call<ResponseUserProfile> updateProfile(@Body RequestUserProfile requestUserProfile);

    @Multipart
    @POST("users/uploadprofilephoto")
    Call<ResponseUploadPhoto> uploadProfilePhoto(@Part("file\"; filename=\"photo.jpg\" ") RequestBody file);

}
