package com.example.automatent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Dev extends AppCompatActivity {
    private ApiService apiService;
    private LinearLayout devLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DevActivity", "onCreate");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dev);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        devLayout = findViewById(R.id.dev);

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        // Retrieve device ID from intent extras
        int deviceId = getIntent().getIntExtra("device_id", -1);
        Log.d("DevActivity", "Device ID: " + deviceId);
        if (deviceId != -1) {
            // If device ID is valid, fetch and display device name
            DataForDevice(deviceId);
        } else {
            // Handle invalid device ID
            Toast.makeText(this, "Invalid device ID", Toast.LENGTH_SHORT).show();
            Log.d("DevActivity", "Invalid device ID");
        }
    }

    private void DataForDevice(Integer deviceId) {
        Log.d("DevActivity", "Fetching data for device ID: " + deviceId);
        Call<DevResult> call = apiService.getDev(deviceId);

        call.enqueue(new Callback<DevResult>() {
            @Override
            public void onResponse(Call<DevResult> call, Response<DevResult> response) {
                // Inside onResponse method of DataForDevice
                Log.d("DevActivity", "Response: " + response.toString());
                if (response.isSuccessful()) {
                    DevResult result = response.body();
                    if (result != null) {
                        String deviceName = result.getName();
                        Log.d("DevActivity", "Device name: " + deviceName);
                        // Display device name on TextView
                        addTextView(deviceName);
                        addButton();
                    } else {
                        Log.d("DevActivity", "Response body is null");
                    }
                } else {
                    // Handle error
                    Log.d("DevActivity", "Failed to get device data. Status code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<DevResult> call, Throwable t) {
                // Handle failure
                Log.e("DevActivity", "Failed to get device data: " + t.getMessage(), t);
                Toast.makeText(Dev.this, "Failed to get device data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTextView(String deviceName) {
        Log.d("DevActivity", "Adding TextView for device: " + deviceName);
        TextView textView = new TextView(this);
        textView.setText(deviceName);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(layoutParams);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setTextColor(Color.WHITE);

        devLayout.addView(textView);
    }

    private void addButton() {
        Switch switchButton = new Switch(this);
        switchButton.setText("On/Off");
        switchButton.setChecked(false); // Set initial state
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

        // Set layout width and height
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Set background tint
        switchButton.setBackgroundResource(R.drawable.buttonformat);

        layoutParams.setMargins(0, 150, 0, 0); // left, top, right, bottom
        switchButton.setLayoutParams(layoutParams);
        int paddingInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        switchButton.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);


        // Set text size
        switchButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        switchButton.setGravity(Gravity.CENTER);
        switchButton.setTextColor(Color.WHITE);
        devLayout.addView(switchButton);
    }

    private void updateDeviceValue(Integer newValue) {
        // Retrieve device ID from intent extras
        int deviceId = getIntent().getIntExtra("device_id", -1);
        Log.d("DevActivity", "Updating device value for ID: " + deviceId);
        if (deviceId != -1) {
            // Call Retrofit service method to update device value
            UpdateDevRequest requestData = new UpdateDevRequest(newValue);

            // Log the request body
            Log.d("DevActivity", "Request Body: " + requestData.toString());

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
