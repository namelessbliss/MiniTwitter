package com.app.nb.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.nb.minitwitter.retrofit.response.Tweet;

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
}
