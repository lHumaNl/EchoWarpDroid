package com.human.echowarpdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.human.echowarpdroid.DefaultValues;
import com.human.echowarpdroid.common.PreSettings;
import com.human.echowarpdroid.R;
import com.human.echowarpdroid.activitiesvars.ActivitiesVars;
import com.human.echowarpdroid.interfaces.FillIntentInterface;


public class MainActivity extends AppCompatActivity implements FillIntentInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EditText numberInput = findViewById(R.id.udpPortNumber);
        numberInput.setText(String.valueOf(DefaultValues.defaultUDPPort));

        Button buttonClientMode = findViewById(R.id.buttonClientMode);
        buttonClientMode.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClientActivity.class);

            fillIntentWithPreSettings(intent, false);
            startActivity(intent);
        });

        Button buttonServerMode = findViewById(R.id.buttonServerMode);
        buttonServerMode.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ServerActivity.class);

            fillIntentWithPreSettings(intent, true);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void fillIntentWithSettings(Intent intent, PreSettings preSettings) {
    }

    public void fillIntentWithPreSettings(Intent intent, boolean isServer) {
        Switch switchAudioDevice = findViewById(R.id.switchAudioDevice);
        boolean isInputDevice = switchAudioDevice.isChecked();

        EditText udpPortNumber = findViewById(R.id.udpPortNumber);
        int udpPort = Integer.parseInt(udpPortNumber.getText().toString());

        EditText editTextPassword = findViewById(R.id.editTextPassword);
        String password = editTextPassword.getText().toString();

        if (password.trim().isEmpty()) password = null;

        intent.putExtra(
                ActivitiesVars.PRE_SETTINGS_OBJECT,
                new PreSettings(isServer, isInputDevice, udpPort, password));
    }
}