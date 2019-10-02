package com.app.nb.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.common.MyApp;
import com.app.nb.minitwitter.common.SharedPreferencesManager;
import com.app.nb.minitwitter.retrofit.AuthTwitterClient;
import com.app.nb.minitwitter.retrofit.AuthTwitterService;
import com.app.nb.minitwitter.retrofit.request.RequestCreateTweet;
import com.app.nb.minitwitter.retrofit.response.Like;
import com.app.nb.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthTwitterService authTwitterService;

    private AuthTwitterClient authTwitterClient;

    private MutableLiveData<List<Tweet>> allTweets;

    private MutableLiveData<List<Tweet>> favTweets;

    private String username;

    TweetRepository() {
        retrofitInit();
    }

    private void retrofitInit() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
        username = SharedPreferencesManager.getStringValue(Constants.PREF_USERNAME);
    }

    public MutableLiveData<List<Tweet>> getAllTweets() {

        if (allTweets == null) {
            allTweets = new MutableLiveData<>();
        }

        //Llama a la carga de los datos de tweets
        Call<List<Tweet>> listCall = authTwitterService.getAllTweets();
        listCall.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    allTweets.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "No se pudo cargar los tweets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        });

        return allTweets;
    }

    public MutableLiveData<List<Tweet>> getFavTweets() {
        if (favTweets == null) {
            favTweets = new MutableLiveData<>();
        }

        List<Tweet> newFavList = new ArrayList<>();
        allTweets.getValue().size();
        Iterator itTweets = allTweets.getValue().iterator();

        while (itTweets.hasNext()) {
            Tweet currentTweet = (Tweet) itTweets.next();
            Iterator itLike = currentTweet.getLikes().iterator();

            boolean encontrado = false;

            while (itLike.hasNext() && !encontrado) {
                Like like = (Like) itLike.next();
                if (like.getUsername().equals(username)) { //Tweet donde el usuario ha hecho like
                    encontrado = true;
                    newFavList.add(currentTweet);
                }
            }
        }

        favTweets.setValue(newFavList);

        return favTweets;
    }

    public void createTweet(String mensaje) {
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(mensaje);
        Call<Tweet> tweetCall = authTwitterService.createTweet(requestCreateTweet);
        tweetCall.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()) {
                    List<Tweet> listaClonada = new ArrayList<>();
                    //Añadimos en primer lugar el nuevo tweet que llega del servidor
                    listaClonada.add(response.body());

                    /**
                     * Iteracion para actualizar la lista de tweets a la lista anterior de crear
                     * el nuevo tweet
                     */
                    for (int i = 0; i < allTweets.getValue().size(); i++) {
                        listaClonada.add(new Tweet(allTweets.getValue().get(i)));
                    }

                    allTweets.setValue(listaClonada);

                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void likeTweet(final int idTweet) {

        Call<Tweet> tweetCall = authTwitterService.likeTweet(idTweet);

        tweetCall.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()) {
                    List<Tweet> listaClonada = new ArrayList<>();

                    /**
                     * Iteracion para actualizar la lista de tweets a la lista anterior de crear
                     * el nuevo tweet
                     */
                    for (int i = 0; i < allTweets.getValue().size(); i++) {
                        if (allTweets.getValue().get(i).getId() == idTweet) {
                            // Si encuentra en la lista original el elmento al que se ha hecho like
                            //Introduce el elemento que llega del servidor
                            listaClonada.add(response.body());
                        } else {
                            listaClonada.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }

                    allTweets.setValue(listaClonada);

                    //Refrescar lista de favoritos
                    getFavTweets();

                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
