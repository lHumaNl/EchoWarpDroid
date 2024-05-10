package com.human.echowarpdroid.common;

import android.media.AudioDeviceInfo;

import java.io.Serializable;
import java.util.List;

public class PreSettings implements Serializable {
    private final boolean isServer;
    private final boolean isInputAudioDevice;
    private final int udpPort;
    private final String password;
    private final List<AudioDeviceInfo> audioDevices;

    public PreSettings(boolean isServer, boolean isInputAudioDevice, int udpPort, String password,List<AudioDeviceInfo> audioDevices) {
        this.isServer = isServer;
        this.isInputAudioDevice = isInputAudioDevice;
        this.udpPort = udpPort;
        this.password = password;
        this.audioDevices = audioDevices;
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isInputAudioDevice() {
        return isInputAudioDevice;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public String getPassword() {
        return password;
    }

    public List<AudioDeviceInfo> getAudioDevices() {
        return audioDevices;
    }
}
