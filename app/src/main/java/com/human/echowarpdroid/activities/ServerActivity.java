package com.human.echowarpdroid.activities;

import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.human.echowarpdroid.activitiesvars.ActivitiesVars;
import com.human.echowarpdroid.adapters.AudioDeviceInfoAdapter;
import com.human.echowarpdroid.common.PreSettings;
import com.human.echowarpdroid.common.Settings;
import com.human.echowarpdroid.interfaces.FillIntentInterface;
import com.human.echowarpdroid.R;

import java.util.Objects;

public class ServerActivity extends AppCompatActivity implements FillIntentInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_server);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        PreSettings preSettings = (PreSettings) getIntent().getSerializableExtra(ActivitiesVars.PRE_SETTINGS_OBJECT);
        Spinner audioDeviceSpinner = findViewById(R.id.audioDeviceSpinner);

        ArrayAdapter<AudioDeviceInfo> audioDeviceInfoAdapter = new AudioDeviceInfoAdapter(
                this,
                R.layout.activity_server,
                Objects.requireNonNull(preSettings).getAudioDevices()
        );

        audioDeviceSpinner.setAdapter(audioDeviceInfoAdapter);

        Button startStreamingButton = findViewById(R.id.startStreamingButton);
        startStreamingButton.setOnClickListener(v -> {
            Intent streamingIntent = new Intent(ServerActivity.this, StreamingActivity.class);

            fillIntentWithSettings(streamingIntent, preSettings);
            startActivity(streamingIntent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fillIntentWithSettings(Intent intent, PreSettings preSettings) {
        Spinner audioDeviceSpinner = findViewById(R.id.audioDeviceSpinner);
        AudioDeviceInfo audioDeviceInfo = (AudioDeviceInfo) audioDeviceSpinner.getSelectedItem();

        EditText heartbeatField = findViewById(R.id.heartbeatField);
        int heartbeatAttempts = Integer.parseInt(heartbeatField.getText().toString());

        Switch sslSwitch = findViewById(R.id.sslSwitch);
        boolean isSSL = sslSwitch.isChecked();

        Switch IntegrityControlSwitch = findViewById(R.id.IntegrityControlSwitch);
        boolean isIntegrityControl = IntegrityControlSwitch.isChecked();

        EditText workersCountField = findViewById(R.id.workersCountField);
        int serverWorkersCount = Integer.parseInt(workersCountField.getText().toString());

        Settings settings = new Settings(
                preSettings.isServer(),
                preSettings.isInputAudioDevice(),
                preSettings.getUdpPort(),
                preSettings.getPassword(),
                preSettings.getAudioDevices(),
                audioDeviceInfo,
                heartbeatAttempts,
                isSSL,
                isIntegrityControl,
                serverWorkersCount
        );

        intent.putExtra(ActivitiesVars.SETTINGS_OBJECT, settings);
    }

    @Override
    public void fillIntentWithPreSettings(Intent intent, boolean isServer) {
    }
}