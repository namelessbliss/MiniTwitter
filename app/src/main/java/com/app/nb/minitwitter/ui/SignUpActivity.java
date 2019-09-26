package com.app.nb.minitwitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.common.SharedPreferencesManager;
import com.app.nb.minitwitter.retrofit.MiniTwitterClient;
import com.app.nb.minitwitter.retrofit.MiniTwitterService;
import com.app.nb.minitwitter.retrofit.request.RequestSignUp;
import com.app.nb.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp;
    private TextView tvGoLogin;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;

    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Ocultar barra de toolbar
        getSupportActionBar().hide();

        minitwitterInit();

        bindView();

        events();

    }

    private void minitwitterInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void bindView() {
        btnSignUp = findViewById(R.id.buttonSignUp);
        tvGoLogin = findViewById(R.id.textViewGoLogin);
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events() {
        btnSignUp.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                goToSingUp();
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }
    }

    private void goToSingUp() {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();
        String username = etUsername.getText().toString();

        if (username.isEmpty()) {
            etUsername.setError("El nombre de ususario es requerido");
        } else if (pass.isEmpty() || pass.length() < 4) {
            etPassword.setError("La contraseña es requerida y debe tener al menos 4 caracteres");
        } else if (email.isEmpty()) {
            etEmail.setError("El email es requerido");
        } else {
            String code = "UDEMYANDROID";
            RequestSignUp requestSignUp = new RequestSignUp(username, email, pass, code);
            Call<ResponseAuth> responseAuthCall = miniTwitterService.doSignUp(requestSignUp);

            responseAuthCall.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Inicio de sesion completado", Toast.LENGTH_SHORT).show();

                        //Almacena preferences del registro
                        SharedPreferencesManager.setStringValue(Constants.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setStringValue(Constants.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setStringValue(Constants.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setStringValue(Constants.PREF_PHOTO_URL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setStringValue(Constants.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setBooleanValue(Constants.PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Error en la conexion", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
