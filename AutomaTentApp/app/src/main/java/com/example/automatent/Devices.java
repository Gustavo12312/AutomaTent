package com.example.automatent;

        import android.graphics.Color;
        import android.os.Bundle;
        import android.util.TypedValue;
        import android.view.View;
        import android.widget.Button;
        import android.widget.GridLayout;
        import android.widget.Toast;

        import androidx.activity.EdgeToEdge;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.AppCompatButton;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;

        import java.util.List;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class Devices extends AppCompatActivity {


    private ApiService apiService;
    private GridLayout devicesLayout; // Change the type to GridLayout

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

        devicesLayout = findViewById(R.id.devicesLayout); // Change the type to GridLayout

        listDevices();
    }

    private void listDevices() {
        Call<List<DevicesResult>> call = apiService.getDevices();

        call.enqueue(new Callback<List<DevicesResult>>() {
            @Override
            public void onResponse(Call<List<DevicesResult>> call, Response<List<DevicesResult>> response) {
                if (response.isSuccessful()) {
                    List<DevicesResult> results = response.body();
                    for (DevicesResult result : results) {
                        String deviceName = result.getName();
                        addButton(deviceName);
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

    private void addButton(String deviceName) {
        Button button = new AppCompatButton(this);
        button.setText(deviceName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, e.g., open device details
                Toast.makeText(Devices.this, "Clicked on " + deviceName, Toast.LENGTH_SHORT).show();
            }
        });
        GridLayout.Spec spec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        // Set layout parameters for GridLayout
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = 0;
        layoutParams.height = 250;
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.columnSpec = spec;

        button.setLayoutParams(layoutParams);

        // Set background tint

        button.setBackgroundResource(R.drawable.buttonformat);





        // Set text size
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        button.setTextColor(Color.WHITE);

        devicesLayout.addView(button);
    }

}
