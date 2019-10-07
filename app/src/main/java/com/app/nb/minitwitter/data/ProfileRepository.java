package com.app.nb.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.common.MyApp;
import com.app.nb.minitwitter.common.SharedPreferencesManager;
import com.app.nb.minitwitter.retrofit.AuthTwitterClient;
import com.app.nb.minitwitter.retrofit.AuthTwitterService;
import com.app.nb.minitwitter.retrofit.request.RequestUserProfile;
import com.app.nb.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.app.nb.minitwitter.retrofit.response.ResponseUserProfile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private AuthTwitterService authTwitterService;

    private AuthTwitterClient authTwitterClient;

    private MutableLiveData<ResponseUserProfile> userProfile;

    private MutableLiveData<String> photoProfile;

    ProfileRepository() {
        retrofitInit();
    }

    private void retrofitInit() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        userProfile = getProfile();
        if (photoProfile == null)
            photoProfile = new MutableLiveData<>();
    }

    public MutableLiveData<ResponseUserProfile> getProfile() {

        if (userProfile == null) {
            userProfile = new MutableLiveData<>();
        }

        //Llama a la carga de los datos de tweets
        Call<ResponseUserProfile> call = authTwitterService.getProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        });

        return userProfile;
    }

    public void updateProfile(RequestUserProfile requestUserProfile) {

        Call<ResponseUserProfile> call = authTwitterService.updateProfile(requestUserProfile);

        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadPhoto(String photoPath) {
        File file = new File(photoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<ResponseUploadPhoto> call = authTwitterService.uploadProfilePhoto(requestBody);

        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if (response.isSuccessful()) {
                    SharedPreferencesManager.setStringValue(Constants.PREF_PHOTO_URL, response.body().getFilename());
                    //TODO Revisar Runtime exc (You cannot start a load on a not yet attached View or a Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).)
                    photoProfile.setValue(response.body().getFilename());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<String> getPhotoProfile() {
        return photoProfile;
    }

}
