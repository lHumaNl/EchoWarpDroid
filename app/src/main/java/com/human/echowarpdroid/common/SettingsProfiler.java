package com.human.echowarpdroid.common;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SettingsProfiler {
    public static void saveSettings(Context context, Settings settings, String filename) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(settings);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Settings loadSettings(Context context, String filename) {
        Settings settings = null;
        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            settings = (Settings) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return settings;
    }

    public String[] getListOfFiles(Context context) {
        return context.fileList();
    }

    public boolean deleteSettingsFile(Context context, String filename) {
        return context.deleteFile(filename);
    }
}
