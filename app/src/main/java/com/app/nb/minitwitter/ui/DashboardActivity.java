package com.app.nb.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.common.SharedPreferencesManager;
import com.app.nb.minitwitter.data.ProfileViewModel;
import com.app.nb.minitwitter.ui.profile.ProfileFragment;
import com.app.nb.minitwitter.ui.tweets.NuevoTweetDialogFragment;
import com.app.nb.minitwitter.ui.tweets.TweetListFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private FloatingActionButton fab;
    private ImageView ivAvatar;
    private ProfileViewModel profileViewModel;

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


        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constants.SELECT_PHOTO_GALLERY) {
                if (data != null) {

                    Uri imagenSeleccionada = data.getData(); //content://gallery/photos/...
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imagenSeleccionada, filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        int imagenIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String photoPath = cursor.getString(imagenIndex);
                        profileViewModel.uploadPhoto(photoPath);
                        cursor.close();
                    }
                }
            }

        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        //Invoca la seleccion de fotos de la galeria
        Intent seleccionarFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(seleccionarFoto, Constants.SELECT_PHOTO_GALLERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Toast.makeText(this, "No se puede tener acceso a las fotos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }
}
