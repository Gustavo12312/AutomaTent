package com.example.automatent;

        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.util.TypedValue;
        import android.view.View;
        import android.widget.Button;
        import android.widget.GridLayout;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import androidx.activity.EdgeToEdge;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.AppCompatButton;
        import androidx.core.content.ContextCompat;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;

        import java.util.List;
        import java.util.Collections;
        import java.util.Comparator;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class Devices extends AppCompatActivity {


    private ApiService apiService;
    private LinearLayout devicesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_devices);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Retrofit retrofit = RetrofitClient.getRetrofitInstance();

        apiService = retrofit.create(ApiService.class);

        devicesLayout = findViewById(R.id.devicesLayout);

        listDevices();

        ImageView imageview = findViewById(R.id.backButton);
        final int clickedColor = ContextCompat.getColor(this, R.color.brown);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageview.setColorFilter(clickedColor);
                Intent intent = new Intent(Devices.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void listDevices() {
        Call<List<DevicesResult>> call = apiService.getDevices();

        call.enqueue(new Callback<List<DevicesResult>>() {
            @Override
            public void onResponse(Call<List<DevicesResult>> call, Response<List<DevicesResult>> response) {
                if (response.isSuccessful()) {
                    List<DevicesResult> results = response.body();
                    // Sort the list of devices by ID
                    Collections.sort(results, new Comparator<DevicesResult>() {
                        @Override
                        public int compare(DevicesResult device1, DevicesResult device2) {
                            return Integer.compare(device1.getId(), device2.getId());
                        }
                    });

                    for (DevicesResult result : results) {
                        String deviceName = result.getName();
                        int deviceId = result.getId();
                        addButton(deviceName, deviceId);
                    }
                } else {
                    // Handle error
                    Toast.makeText(Devices.this, "Failed to get devices", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DevicesResult>> call, Throwable t) {
                // Handle failure
                Toast.makeText(Devices.this, "Failed to get devices: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addButton(String deviceName, int deviceId) {
        Button button = new Button(this);
        button.setText(deviceName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class<?> destinationClass = null;
                switch (deviceName.toLowerCase()) {
                    case "led":
                        destinationClass = Led.class;
                        break;
                    case "heater":
                        destinationClass = Heater.class;
                        break;
                    case "fan":
                        destinationClass = Fan.class;
                        break;
                    case "reagroup button":
                        destinationClass = Fan.class;
                        break;
                }
                Intent intent = new Intent(Devices.this, destinationClass);
                intent.putExtra("device_id", deviceId);
                startActivity(intent);
            }
        });
        // Set layout width and height
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Set background tint
        button.setBackgroundResource(R.drawable.buttonformat);

        layoutParams.setMargins(0, 0, 0, 30); // left, top, right, bottom
        button.setLayoutParams(layoutParams);

        // Set text size
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        button.setTextColor(Color.WHITE);
        devicesLayout.addView(button);
    }

}
