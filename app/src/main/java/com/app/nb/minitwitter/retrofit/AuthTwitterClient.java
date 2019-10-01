package com.app.nb.minitwitter.retrofit;

import com.app.nb.minitwitter.common.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {

    // Instancia propia estatica
    private static AuthTwitterClient INSTANCE = null;

    private AuthTwitterService miniTwitterService;
    private Retrofit retrofit;

    public AuthTwitterClient() {
        //Incluye en la cabecera de la peticion el Token que autoriza al usuario
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AuthInterceptor());

        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWIITER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        miniTwitterService = retrofit.create(AuthTwitterService.class);
    }

    /**
     * Patron Singleton
     *
     * @return
     */
    public static AuthTwitterClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthTwitterClient();
        }
        return INSTANCE;
    }

    public AuthTwitterService getAuthTwitterService() {
        return miniTwitterService;
    }
}
