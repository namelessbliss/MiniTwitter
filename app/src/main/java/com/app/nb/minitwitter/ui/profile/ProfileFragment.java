package com.app.nb.minitwitter.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.data.ProfileViewModel;
import com.app.nb.minitwitter.retrofit.request.RequestUserProfile;
import com.app.nb.minitwitter.retrofit.response.ResponseUserProfile;
import com.bumptech.glide.Glide;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private ImageView ivAvatar;
    private EditText etUser, etPass, etEmail, etWeb, etDesc;
    private Button btnSave, btnChangePass;
    private boolean firstLoadingData = true;

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
                String username = etUser.getText().toString();
                String email = etEmail.getText().toString();
                String descripcion = etDesc.getText().toString();
                String website = etWeb.getText().toString();
                String password = etPass.getText().toString();

                if (username.isEmpty())
                    etUser.setError("El nombre de usuario es requerido");
                else if (email.isEmpty())
                    etEmail.setError("El email es requerido");
                else if (password.isEmpty())
                    etPass.setError("La contraseña es requerida");
                else {
                    RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
                    profileViewModel.updateProfile(requestUserProfile);
                    Toast.makeText(getActivity(), "Enviando Informacion al servidor", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(false);
                }
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

                if (!firstLoadingData) {
                    btnSave.setEnabled(true);
                    Toast.makeText(getActivity(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

                }

                if (!responseUserProfile.getPhotoUrl().isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constants.API_MINITWIITER_FILES_URL + responseUserProfile.getPhotoUrl())
                            .into(ivAvatar);
                }
                firstLoadingData = false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
