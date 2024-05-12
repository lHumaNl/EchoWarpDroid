package com.human.echowarpdroid.interfaces;

import android.media.AudioDeviceInfo;
import android.widget.ArrayAdapter;

import com.human.echowarpdroid.common.PreSettings;

public interface AudioManagerInterface {
    ArrayAdapter<AudioDeviceInfo> getAudioDeviceAdapter(PreSettings preSettings);
}
