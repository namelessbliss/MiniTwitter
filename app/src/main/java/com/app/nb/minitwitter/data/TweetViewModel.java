package com.app.nb.minitwitter.data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.nb.minitwitter.retrofit.response.Tweet;
import com.app.nb.minitwitter.ui.BottomModalTweetFragment;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets() {
        return tweets;
    }

    /**
     * Metodo para mostrar el cuadro de dialogo
     *
     * @param context
     * @param idTweet
     */
    public void openDialogTweetMenu(Context context, int idTweet) {
        BottomModalTweetFragment dialogTweet = BottomModalTweetFragment.newInstance(idTweet);

        AppCompatActivity activity = (AppCompatActivity) context;

        dialogTweet.show(activity.getSupportFragmentManager(), "BottomModalTweetFragment");
    }

    public LiveData<List<Tweet>> getNewTweets() {
        tweets = tweetRepository.getAllTweets();
        return tweets;
    }

    public void createTweet(String mensaje) {
        tweetRepository.createTweet(mensaje);
    }

    public void likeTweet(int idTweet) {
        tweetRepository.likeTweet(idTweet);
    }

    public LiveData<List<Tweet>> getFavTweets() {
        favTweets = tweetRepository.getFavTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewFavTweets() {
        getNewTweets();
        return getFavTweets();
    }

    public void deleteTweet(int idTweet) {
        tweetRepository.deleteTweet(idTweet);
    }


}
