package com.human.echowarpdroid.common;


import java.io.Serializable;

public class PreSettings implements Serializable {
    private final boolean isServer;
    private final boolean isInputAudioDevice;
    private final int udpPort;
    private final String password;

    public PreSettings(boolean isServer, boolean isInputAudioDevice, int udpPort, String password) {
        this.isServer = isServer;
        this.isInputAudioDevice = isInputAudioDevice;
        this.udpPort = udpPort;
        this.password = password;
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
}
