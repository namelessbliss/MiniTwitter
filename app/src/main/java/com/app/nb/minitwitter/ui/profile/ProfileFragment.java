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
import androidx.lifecycle.ViewModelProviders;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.data.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private ImageView ivAvatar;
    private EditText etUser, etPass, etEmail;
    private Button btnSave, btnChangePass;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        etEmail = view.findViewById(R.id.editTextEmail);
        etPass = view.findViewById(R.id.editTextCurrentPassword);
        etUser = view.findViewById(R.id.editTextUsername);
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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

    }

}
