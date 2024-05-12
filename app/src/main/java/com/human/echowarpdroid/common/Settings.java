package com.human.echowarpdroid.common;

import android.media.AudioDeviceInfo;


public class Settings extends PreSettings {
    private final AudioDeviceInfo audioDeviceInfo;
    private final String serverAddress;
    private Integer heartbeatAttempt;
    private Boolean isSSL;
    private Boolean isIntegrityControl;
    private final Integer serverWorkersCount;
    private final CryptoManager cryptoManager;

    public Settings(boolean isServer, boolean isInputAudioDevice, int udpPort, String password,
                    AudioDeviceInfo audioDeviceInfo, String serverAddress) {
        super(isServer, isInputAudioDevice, udpPort, password);
        this.audioDeviceInfo = audioDeviceInfo;
        this.serverAddress = serverAddress;
        serverWorkersCount = null;
        cryptoManager = new CryptoManager(isServer, null, null);
    }

    public Settings(boolean isServer, boolean isInputAudioDevice, int udpPort, String password,
                    AudioDeviceInfo audioDeviceInfo, Integer heartbeatAttempt, Boolean isSSL,
                    Boolean isIntegrityControl, Integer serverWorkersCount) {
        super(isServer, isInputAudioDevice, udpPort, password);
        this.audioDeviceInfo = audioDeviceInfo;
        this.heartbeatAttempt = heartbeatAttempt;
        this.isSSL = isSSL;
        this.isIntegrityControl = isIntegrityControl;
        this.serverWorkersCount = serverWorkersCount;
        serverAddress = null;
        cryptoManager = new CryptoManager(isServer, isIntegrityControl, isSSL);
    }

    public AudioDeviceInfo getAudioDeviceInfo() {
        return audioDeviceInfo;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public Integer getHeartbeatAttempt() {
        return heartbeatAttempt;
    }

    public Boolean getSSL() {
        return isSSL;
    }

    public Boolean getIntegrityControl() {
        return isIntegrityControl;
    }

    public Integer getServerWorkersCount() {
        return serverWorkersCount;
    }

    public CryptoManager getCryptoManager() {
        return cryptoManager;
    }
}
