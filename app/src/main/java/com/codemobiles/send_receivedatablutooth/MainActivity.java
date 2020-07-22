package com.codemobiles.send_receivedatablutooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    private Bluetoothconnectionservice bluetoothconnectionservice;
    private BluetoothDevice mbtdevice;
    private static final UUID muuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

    Button myDop;
    Button myDcl;
    Button myct;
    TextView mytext;
//00001101-0000-1000-8000-00805F9B34FB
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                message("TEsT");
                mbtdevice = device;
                mytext.setText(device.getName());


            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothconnectionservice = new Bluetoothconnectionservice(MainActivity.this);
        mytext = (TextView) findViewById(R.id.devicess);
        myDop = (Button) findViewById(R.id.openthedoor);
        myDcl = (Button) findViewById(R.id.closethedoor);
        myct = (Button) findViewById(R.id.connectbt);

        // Get a set of currently paired devices

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();



        // If there are paired devices, add each one to the ArrayAdapter

        if (pairedDevices.size() > 0) {



            for (BluetoothDevice device : pairedDevices) {
                mbtdevice = device;
                message(device.getName()+":"+device.getAddress());

            }

        } else {

            message("nodevice paired");

        }


        myDop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bytes = "1".getBytes(Charset.defaultCharset());
                bluetoothconnectionservice.write(bytes);
            }
        });
        myDcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bytes = "2".getBytes(Charset.defaultCharset());
                bluetoothconnectionservice.write(bytes);
            }
        });
        myct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(mbtdevice, muuid);
            }
        });
    }

    public void start(BluetoothDevice device, UUID uuid) {
        bluetoothconnectionservice.startClient(device, uuid);
    }

    public void message(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
    }

}
