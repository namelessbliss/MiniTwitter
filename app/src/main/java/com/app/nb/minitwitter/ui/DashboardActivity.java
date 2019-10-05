package com.app.nb.minitwitter.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.common.SharedPreferencesManager;
import com.app.nb.minitwitter.ui.profile.ProfileFragment;
import com.app.nb.minitwitter.ui.tweets.NuevoTweetDialogFragment;
import com.app.nb.minitwitter.ui.tweets.TweetListFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ImageView ivAvatar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_ALL);
                    fab.show();
                    break;
                case R.id.navigation_tweets_like:
                    fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_FAVS);
                    fab.hide();
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    fab.hide();
                    break;
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fab = findViewById(R.id.fab);
        ivAvatar = findViewById(R.id.imageViewUser);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevoTweetDialogFragment dialogFragment = new NuevoTweetDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "NuevoTweetDialogFragment");
            }
        });

        getSupportActionBar().hide();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, TweetListFragment.newInstance(Constants.TWEET_LIST_ALL))
                .commit();

        //Set imagen del usuario
        String photoUrl = SharedPreferencesManager.getStringValue(Constants.PREF_PHOTO_URL);

        if (!photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(Constants.API_MINITWIITER_FILES_URL + photoUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(ivAvatar);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_account_circle_grey_24dp)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(ivAvatar);
        }
    }

}
