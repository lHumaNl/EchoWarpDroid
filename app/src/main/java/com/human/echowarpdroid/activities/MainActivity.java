package com.human.echowarpdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.human.echowarpdroid.DefaultValues;
import com.human.echowarpdroid.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EditText numberInput = findViewById(R.id.editTextNumber);
        numberInput.setText(String.valueOf(DefaultValues.defaultUDPPort));

        Button buttonClientMode = findViewById(R.id.buttonClientMode);
        buttonClientMode.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClientActivity.class);
            startActivity(intent);
        });

        Button buttonServerMode = findViewById(R.id.buttonServerMode);
        buttonServerMode.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ServerActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}