package com.human.echowarpdroid.adapters;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AudioDeviceInfoAdapter extends ArrayAdapter<AudioDeviceInfo> {
    private final List<AudioDeviceInfo> audioDevices;

    public AudioDeviceInfoAdapter(@NonNull Context context, int resource, List<AudioDeviceInfo> audioDevices) {
        super(context, resource, audioDevices);
        this.audioDevices = audioDevices;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text = (TextView) view;
        text.setText(audioDevices.get(position).getProductName().toString());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView text = (TextView) view;
        text.setText(audioDevices.get(position).getProductName().toString());
        return view;
    }

}
