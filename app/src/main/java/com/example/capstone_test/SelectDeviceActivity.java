package com.example.capstone_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        // Instantiate RecyclerView
        RecyclerView recyclerView = findViewById(R.id.deviceList);

        // Initialize Bluetooth Adapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Fetch the list from Android device's cache
        try {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            List<Object> deviceList = new ArrayList<>();
            for (BluetoothDevice device : pairedDevices){ // extract the name and hardware address for every device in cache using For statement
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                DeviceInfoModel deviceInfoModel = new DeviceInfoModel(deviceName, deviceHardwareAddress); // data model or data template that we will use to make the structured data object
                deviceList.add(deviceInfoModel);
            }
            // Setting Up RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Connecting to Data Source and Content Layout
            DeviceListAdapter listAdapter = new DeviceListAdapter(this, deviceList);
            recyclerView.setAdapter(listAdapter);

        } catch(SecurityException e){
            e.printStackTrace();
        }

    }
}