package com.example.capstone_test;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private String deviceAddress = null;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static CreateConnectThread createConnectThread;

    private final static int CONNECTION_STATUS = 1;
    private final static int MESSAGE_READ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate UI
        TextView bluetoothStatus = findViewById(R.id.textBluetoothStatus);
        Button buttonConnect = findViewById(R.id.buttonConnect);
        Button buttonDisconnect = findViewById(R.id.buttonDisconnect);
        TextView ledStatus = findViewById(R.id.textLedStatus);
        Button buttonOn = findViewById(R.id.buttonOn);
        Button buttonOff = findViewById(R.id.buttonOff);
        Button buttonBlink = findViewById(R.id.buttonBlink);

        // Code for the "Connect" button
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectDeviceActivity.class);
                startActivity(intent);
            }
        });

        // Code for the "Disconnect" button
        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createConnectThread.cancel();
                bluetoothStatus.setText("Bluetooth is Disconnected");
            }
        });

        // Get Device Address information
        deviceAddress = getIntent().getStringExtra("deviceAddress");

        // If device Address is found
        if (deviceAddress != null){
            bluetoothStatus.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter, deviceAddress);
            createConnectThread.start();
        }

        // Handler Object
        handler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECTION_STATUS:
                        switch (msg.arg1) {
                            case 1:
                                bluetoothStatus.setText("Bluetooth Connected");
                                break;
                            case 2:
                                bluetoothStatus.setText("Connection Failed");
                                break;

                        }
                        break;

                    // If the message contains data from Arduino board
                    case MESSAGE_READ:
                        // Code will be completed in the next video
                        break;
                }
            }
        };

    }

    /* ======== Thread 1 ========*/
    public static class CreateConnectThread extends Thread {

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            try {
                UUID uuid = bluetoothDevice.getUuids()[0].getUuid();
                try {
                    tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                } catch (IOException e) {
                    Log.e("Eroor Message", e.toString());
                }
            } catch (SecurityException e){
                e.printStackTrace();
            }

            mmSocket = tmp;
        }

        public void run() {
            try {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter.cancelDiscovery();
                try {
                    mmSocket.connect();
                    handler.obtainMessage(CONNECTION_STATUS, 1, -1).sendToTarget();
                } catch (IOException connectException){
                    try {
                        mmSocket.close();
                        handler.obtainMessage(CONNECTION_STATUS, -1, -1).sendToTarget();
                    } catch (IOException closeException) {}
                    return;
                }
            } catch (SecurityException e){
                e.printStackTrace();
            }

        }
        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e){}
        }
    }
}