package com.example.automatent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Dht extends AppCompatActivity {
    private ApiService apiService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DevActivity", "onCreate");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dht);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
        handler.post(runnableCode);

        ImageView imageview = findViewById(R.id.backButton);
        final int clickedColor = ContextCompat.getColor(this, R.color.brown);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageview.setColorFilter(clickedColor);
                Intent intent = new Intent(Dht.this, Devices.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.infoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dht.this, DhtInfo.class);
                startActivity(intent);
            }
        });
    }
    public void updateData(){
        Call<DevResult> call = apiService.getDev(6);
        call.enqueue(new Callback<DevResult>() {
            @Override
            public void onResponse(Call<DevResult> call, Response<DevResult> response) {
                if (response.isSuccessful()) {
                    String[] values = response.body().getValueString().split(":");
                    TextView temp = findViewById(R.id.dhtTemp);
                    temp.setText(values[0]);
                    TextView hum = findViewById(R.id.dhtHum);
                    hum.setText(values[1]);
                    Log.d("DevActivity", "Device value updated successfully.");
                } else {
                    // Handle unsuccessful response
                    Log.d("DevActivity", "Failed to update device value. Status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DevResult> call, Throwable t) {
                // Handle failure
                Log.e("DevActivity", "Failed to update device value: " + t.getMessage(), t);
            }
        });
    }
    private Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Call your Java function here
            updateData();

            // Run this runnable again after 5 seconds
            handler.postDelayed(this, 5000); // 5000 milliseconds = 5 seconds
        }
    };
}
