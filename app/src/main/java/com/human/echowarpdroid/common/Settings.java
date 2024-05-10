package com.human.echowarpdroid.common;

import android.media.AudioDeviceInfo;

import java.io.Serializable;
import java.util.List;

public class Settings extends PreSettings implements Serializable {
    private final AudioDeviceInfo audioDeviceInfo;
    private final String serverAddress;
    private Integer heartbeatAttempt;
    private Boolean isSSL;
    private Boolean isIntegrityControl;
    private final Integer serverWorkersCount;
    private final CryptoManager cryptoManager;

    public Settings(boolean isServer, boolean isInputAudioDevice, int udpPort, String password,
                    List<AudioDeviceInfo> audioDevices, AudioDeviceInfo audioDeviceInfo,
                    String serverAddress) {
        super(isServer, isInputAudioDevice, udpPort, password, audioDevices);
        this.audioDeviceInfo = audioDeviceInfo;
        this.serverAddress = serverAddress;
        serverWorkersCount = null;
        cryptoManager = new CryptoManager(isServer);
    }

    public Settings(boolean isServer, boolean isInputAudioDevice, int udpPort, String password,
                    List<AudioDeviceInfo> audioDevices, AudioDeviceInfo audioDeviceInfo,
                    Integer heartbeatAttempt, Boolean isSSL, Boolean isIntegrityControl,
                    Integer serverWorkersCount) {
        super(isServer, isInputAudioDevice, udpPort, password, audioDevices);
        this.audioDeviceInfo = audioDeviceInfo;
        this.heartbeatAttempt = heartbeatAttempt;
        this.isSSL = isSSL;
        this.isIntegrityControl = isIntegrityControl;
        this.serverWorkersCount = serverWorkersCount;
        serverAddress = null;
        cryptoManager = new CryptoManager(isServer);
    }


}
