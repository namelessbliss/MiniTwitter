package com.app.nb.minitwitter.ui.tweets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.data.TweetViewModel;
import com.app.nb.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetListFragment extends Fragment {

    private int tweetListType = 1;

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TweetRecyclerViewAdapter adapter;

    private List<Tweet> tweetList;

    private TweetViewModel tweetViewModel;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = ViewModelProviders.of(getActivity()).get(TweetViewModel.class);

        if (getArguments() != null) {
            tweetListType = getArguments().getInt(Constants.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        //Establece color
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAzul));

        //Metodo para actualizar lista
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (tweetListType == Constants.TWEET_LIST_ALL)
                    loadNewData();
                else if (tweetListType == Constants.TWEET_LIST_FAVS)
                    loadNewFavData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new TweetRecyclerViewAdapter(getActivity(), tweetList);
        recyclerView.setAdapter(adapter);

        if (tweetListType == Constants.TWEET_LIST_ALL)
            loadTweetData();
        else if (tweetListType == Constants.TWEET_LIST_FAVS)
            loadFavTweetData();
        return view;
    }

    private void loadNewFavData() {
        tweetViewModel.getNewFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewFavTweets().removeObserver(this); // Eliminar el observador sobre la lista de tweets
            }
        });
    }

    private void loadFavTweetData() {
        tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setData(tweetList);
            }
        });
    }


    private void loadTweetData() {
        tweetViewModel.getTweets().observe(this, new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) { //Cuando ocurre un cambio a traves del viewModel
                tweetList = tweets;
                adapter.setData(tweetList);
            }
        });
    }

    private void loadNewData() {
        tweetViewModel.getNewTweets().observe(this, new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) { //Cuando ocurre un cambio a traves del viewModel
                tweetList = tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                //Elimina observado, no se vuelve a lanzar a menos que se realice el gesto de swipe
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });
    }
}
