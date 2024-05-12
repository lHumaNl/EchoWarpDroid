package com.human.echowarpdroid.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.human.echowarpdroid.activitiesvars.ActivitiesVars;
import com.human.echowarpdroid.adapters.AudioDeviceInfoAdapter;
import com.human.echowarpdroid.common.PreSettings;
import com.human.echowarpdroid.common.Settings;
import com.human.echowarpdroid.interfaces.AudioManagerInterface;
import com.human.echowarpdroid.interfaces.FillIntentInterface;
import com.human.echowarpdroid.R;

import java.util.Arrays;
import java.util.Objects;

public class ClientActivity extends AppCompatActivity implements FillIntentInterface, AudioManagerInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        PreSettings preSettings = (PreSettings) intent.getSerializableExtra(ActivitiesVars.PRE_SETTINGS_OBJECT);

        Spinner audioDeviceSpinner = findViewById(R.id.audioDeviceSpinner);
        audioDeviceSpinner.setAdapter(getAudioDeviceAdapter(preSettings));

        Toolbar toolbar = findViewById(R.id.clientToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button startReceivingButton = findViewById(R.id.startStreaming);
        startReceivingButton.setOnClickListener(v -> {
            Intent streamingIntent = new Intent(ClientActivity.this, StreamingActivity.class);

            fillIntentWithSettings(streamingIntent, Objects.requireNonNull(preSettings));
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

        EditText serverAddrEditText = findViewById(R.id.serverAddrEditText);
        String serverAddress = serverAddrEditText.getText().toString();

        Settings settings = new Settings(
                preSettings.isServer(),
                preSettings.isInputAudioDevice(),
                preSettings.getUdpPort(),
                preSettings.getPassword(),
                audioDeviceInfo,
                serverAddress
        );

        intent.putExtra(ActivitiesVars.SETTINGS_OBJECT, settings);
    }

    @Override
    public void fillIntentWithPreSettings(Intent intent, boolean isServer) {
    }

    @Override
    public ArrayAdapter<AudioDeviceInfo> getAudioDeviceAdapter(PreSettings preSettings) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] devices = audioManager.getDevices(
                Objects.requireNonNull(preSettings).isInputAudioDevice() ?
                        AudioManager.GET_DEVICES_INPUTS :
                        AudioManager.GET_DEVICES_OUTPUTS
        );

        return new AudioDeviceInfoAdapter(
                this,
                R.layout.activity_client,
                Arrays.asList(devices)
        );
    }
}