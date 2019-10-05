package com.app.nb.minitwitter.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.data.ProfileViewModel;
import com.app.nb.minitwitter.retrofit.response.ResponseUserProfile;
import com.bumptech.glide.Glide;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private ImageView ivAvatar;
    private EditText etUser, etPass, etEmail, etWeb, etDesc;
    private Button btnSave, btnChangePass;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        etEmail = view.findViewById(R.id.editTextEmail);
        etPass = view.findViewById(R.id.editTextCurrentPassword);
        etUser = view.findViewById(R.id.editTextUsername);
        etWeb = view.findViewById(R.id.editTextWebsite);
        etDesc = view.findViewById(R.id.editTextDescripcion);
        btnSave = view.findViewById(R.id.buttonSave);
        btnChangePass = view.findViewById(R.id.buttonChangePass);

        //Eventos
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        //ViewModel
        profileViewModel.userProfileLiveData.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(ResponseUserProfile responseUserProfile) {
                etUser.setText(responseUserProfile.getUsername());
                etEmail.setText(responseUserProfile.getEmail());
                etDesc.setText(responseUserProfile.getDescripcion());
                etWeb.setText(responseUserProfile.getWebsite());
                if (!responseUserProfile.getPhotoUrl().isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constants.API_MINITWIITER_FILES_URL + responseUserProfile.getPhotoUrl())
                            .into(ivAvatar);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
