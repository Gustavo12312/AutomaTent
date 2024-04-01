package com.example.automatent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {
    private Retrofit retrofit;
    private ApiService apiService;
    private String BASE_URL = "http://10.72.46.159:8080";





    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });


        TextView textView = findViewById(R.id.noacc);
        final int clickedColor = Color.YELLOW;
        final int defaultColor = textView.getCurrentTextColor();;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setTextColor(clickedColor);
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.setTextColor(defaultColor);
                return false;
            }
        });
    }

    private void handleLogin() {


        Button loginBtn = findViewById(R.id.loginButton);

        EditText username = findViewById(R.id.usernameEditText);

        EditText pass = findViewById(R.id.passwordEditText);

        String usernameText = username.getText().toString();
        String passText = pass.getText().toString();

        if (usernameText.isEmpty() || passText.isEmpty()) {
            Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                map.put("name", username.getText().toString());
                map.put("pass", pass.getText().toString());

                Call<LoginResult> call = apiService.loginUser(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.code() == 200) {
                            Toast.makeText(Login.this, "Login Successfully",
                                    Toast.LENGTH_SHORT).show();

                            LoginResult result = response.body();
                            Intent intent = new Intent(Login.this, Devices.class);
                            startActivity(intent);


                        }
                        else if (response.code() == 401) {
                            Toast.makeText(Login.this, "Wrong Credentials",
                                    Toast.LENGTH_SHORT).show();
                        }}

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(Login.this, t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }




}