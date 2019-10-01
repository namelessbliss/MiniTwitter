package com.app.nb.minitwitter.retrofit;

import com.app.nb.minitwitter.retrofit.request.RequestLogin;
import com.app.nb.minitwitter.retrofit.request.RequestSignUp;
import com.app.nb.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MiniTwitterService {


    /**
     * Pasa de parametro un objeto RequestLogin y obtiene como respuesta un ResponseAuth
     *
     * @param requestLogin
     * @return
     */
    @POST("auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    /**
     * Pasa de parametro un objeto RequestSignUp y obtiene como respuesta un ResponseAuth
     * @param requestSignUp
     * @return
     */
    @POST("auth/signup")
    Call<ResponseAuth> doSignUp(@Body RequestSignUp requestSignUp);
}
