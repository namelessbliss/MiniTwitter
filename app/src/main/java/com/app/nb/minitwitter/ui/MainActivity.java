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
import com.app.nb.minitwitter.retrofit.MiniTwitterClient;
import com.app.nb.minitwitter.retrofit.MiniTwitterService;
import com.app.nb.minitwitter.retrofit.request.RequestLogin;
import com.app.nb.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private TextView tvGoSignUp;
    private EditText etEmail;
    private EditText etPassword;

    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ocultar barra de toolbar
        getSupportActionBar().hide();

        retrofitInit();

        bindView();

        events();
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void bindView() {
        btnLogin = findViewById(R.id.buttonLogin);
        tvGoSignUp = findViewById(R.id.textViewSignUp);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events() {
        btnLogin.setOnClickListener(this);
        tvGoSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewSignUp:
                goToSignUp();
                break;
        }
    }

    private void goToLogin() {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        if (email.isEmpty())
            etEmail.setError("El email es incorrecto");
        else if (pass.isEmpty())
            etPassword.setError("Contraseña incorrecta");
        else {
            RequestLogin requestLogin = new RequestLogin(email, pass);
            Call<ResponseAuth> responseAuthCall = miniTwitterService.doLogin(requestLogin);

            responseAuthCall.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()) { // si el codigo de respuesta esta entr el 200 y 300
                        Toast.makeText(MainActivity.this, "Inicio de sesion completado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);

                        //Destruir activity
                        finish();
                    } else
                        Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Problemas de conexion", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void goToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
