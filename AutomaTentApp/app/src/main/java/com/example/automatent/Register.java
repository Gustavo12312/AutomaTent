package com.example.automatent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private ApiService apiService;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Retrofit retrofit = RetrofitClient.getRetrofitInstance();

        apiService = retrofit.create(ApiService.class);

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegister();
            }
        });


        TextView textView = findViewById(R.id.acc);
        ImageView imageview = findViewById(R.id.backButton);
        final int clickedColor = ContextCompat.getColor(this, R.color.brown);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageview.setColorFilter(clickedColor);
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setTextColor(clickedColor);
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void handleRegister() {



        EditText username = findViewById(R.id.usernameEditText);

        EditText email = findViewById(R.id.emailEditText);

        EditText pass = findViewById(R.id.passwordEditText);

        String usernameText = username.getText().toString();
        String passText = pass.getText().toString();

        if (usernameText.isEmpty() || passText.isEmpty()) {
            Toast.makeText(Register.this, "Please enter username, email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> map = new HashMap<>();

        map.put("name", username.getText().toString());
        map.put("email", email.getText().toString());
        map.put("pass", pass.getText().toString());

        Call<RegisterResult> call = apiService.registerUser(map);

        call.enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                if (response.code() == 200) {
                    Toast.makeText(Register.this, "Register Successfully",
                            Toast.LENGTH_SHORT).show();

                    RegisterResult result = response.body();
                    Intent intent = new Intent(Register.this, Devices.class);
                    startActivity(intent);


                }
                else if (response.code() == 401) {
                    Toast.makeText(Register .this, "Wrong Credentials",
                            Toast.LENGTH_SHORT).show();
                }}

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                Toast.makeText(Register.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

}