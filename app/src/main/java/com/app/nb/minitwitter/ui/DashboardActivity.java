package com.app.nb.minitwitter.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.common.SharedPreferencesManager;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ImageView ivAvatar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_tweets_like:
                    return true;
                case R.id.navigation_profile:
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

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new TweetListFragment()).commit();

        //Set imagen del usuario
        String photoUrl = SharedPreferencesManager.getStringValue(Constants.PREF_PHOTO_URL);

        if (!photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(Constants.API_MINITWIITER_FILES_URL + photoUrl)
                    .into(ivAvatar);
        }
    }

}
