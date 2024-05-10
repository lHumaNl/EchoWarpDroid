package com.human.echowarpdroid.interfaces;

import android.content.Intent;

import com.human.echowarpdroid.common.PreSettings;

public interface FillIntentInterface {
    void fillIntentWithSettings(Intent intent, PreSettings preSettings);

    void fillIntentWithPreSettings(Intent intent, boolean isServer);
}
