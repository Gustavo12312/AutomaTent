package com.example.automatent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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

public class Heater extends AppCompatActivity {
    private ApiService apiService;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DevActivity", "onCreate");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_heater);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);


        ImageView imageview = findViewById(R.id.backButton);
        final int clickedColor = ContextCompat.getColor(this, R.color.brown);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageview.setColorFilter(clickedColor);
                Intent intent = new Intent(Heater.this, Devices.class);
                startActivity(intent);
            }
        });
        Switch switchButton = findViewById(R.id.switchButton);

        sharedPreferences = getSharedPreferences("switch_state_heater", MODE_PRIVATE);
        switchButton.setChecked(sharedPreferences.getBoolean("isChecked", false));

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked", switchButton.isChecked());
                editor.apply();
            }
        });


        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle switch state change here
                if (isChecked) {
                    updateDeviceValue(1);
                } else {
                    updateDeviceValue(0);
                }
            }
        });
    }


    private void updateDeviceValue(Integer newValue) {
        // Retrieve device ID from intent extras
        int deviceId = getIntent().getIntExtra("device_id", -1);
        Log.d("DevActivity", "Updating device value for ID: " + deviceId);
        if (deviceId != -1) {
            // Call Retrofit service method to update device value
            UpdateDevRequest requestData = new UpdateDevRequest(newValue);

            // Log the request body
            Log.d("DevActivity", "Request Body: " + requestData);

            // Call Retrofit service method to update device value
            Call<Void> call = apiService.updateDev(deviceId, requestData);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Handle successful response
                        Log.d("DevActivity", "Device value updated successfully.");
                    } else {
                        // Handle unsuccessful response
                        Log.d("DevActivity", "Failed to update device value. Status code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Handle failure
                    Log.e("DevActivity", "Failed to update device value: " + t.getMessage(), t);
                }
            });
        } else {
            // Handle invalid device ID
            Toast.makeText(this, "Invalid device ID", Toast.LENGTH_SHORT).show();
            Log.d("DevActivity", "Invalid device ID");
        }
    }
}
