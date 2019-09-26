package com.app.nb.minitwitter.retrofit;

import com.app.nb.minitwitter.common.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {

    // Instancia propia estatica
    private static MiniTwitterClient INSTANCE = null;

    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    public MiniTwitterClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWIITER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }

    /**
     * Patron Singleton
     *
     * @return
     */
    public static MiniTwitterClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MiniTwitterClient();
        }
        return INSTANCE;
    }

    public MiniTwitterService getMiniTwitterService() {
        return miniTwitterService;
    }
}
