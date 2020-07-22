package com.codemobiles.send_receivedatablutooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class Bluetoothconnectionservice {
    private static final String tag = "Bluetoothconnection";
    private static final String app = "MYAPP";
    private static final UUID my_uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mbluetoothadapter;
    Context mcontext;

    private ConnectThread mconnectthread;
    private AcceptThread macceptthread;
    private ConnectedThread connectedthread;

    private BluetoothDevice bluetoothDevice;
    private UUID uuid;
    ProgressDialog mprogressdialog;

    public Bluetoothconnectionservice(Context context) {
        mcontext = context;
        mbluetoothadapter = BluetoothAdapter.getDefaultAdapter();
        //start();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mserversocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mbluetoothadapter.listenUsingInsecureRfcommWithServiceRecord(app, my_uuid);
                Log.d(TAG, "AcceptThread: ");
            } catch (IOException e) {
                //   Log.e(TAG, "AcceptThread: ", );
            }
            mserversocket = tmp;
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            try {
                Log.d(TAG, "run: sever socket start");
                socket = mserversocket.accept();
                Log.d(TAG, "run: sever socket accept connection");
            } catch (IOException e) {
                Log.e(TAG, "run: IOException" + e.getMessage());
            }
            if (socket != null) {
                connected(socket, bluetoothDevice);
            }
            Log.i(TAG, "run: End acceptthread");
        }

        public void cancle() {
            try {
                Log.d(TAG, "cancle: cancle thread");
                mserversocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancle: cancle acceptthread" + e.getMessage());
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mybluetoothsk;

        public ConnectThread(BluetoothDevice mybluetoothdevice, UUID myuuid) {
            Log.d(TAG, "ConnectThread: connectthread start");
            bluetoothDevice = mybluetoothdevice;
            uuid = myuuid;
        }

        @Override
        public void run() {
            BluetoothSocket tmp = null;
            try {
                tmp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(TAG, "run: could not create socket" + e.getMessage());
            }
            mybluetoothsk = tmp;
            mbluetoothadapter.cancelDiscovery();

            try {
                mybluetoothsk.connect();
                Log.d(TAG, "run: connectthread connected");
            } catch (IOException e) {
                try {
                    mybluetoothsk.close();
                    Log.d(TAG, "run: close socket");
                } catch (IOException e1) {
                    Log.e(TAG, "run: unable to close socket" + e1.getMessage());
                }
                Log.d(TAG, "run: can't connect to uuid");
            }
            connected(mybluetoothsk, bluetoothDevice);
//I'm here at bluetooth tutorial part 2 ConnectThread
        }

        public void cancle() {
            try {
                Log.d(TAG, "cancle: cancle thread");
                mybluetoothsk.close();
            } catch (IOException e) {
                Log.e(TAG, "cancle: cancle acceptthread" + e.getMessage());
            }
        }
    }

    public synchronized void start() {
        Log.d(TAG, "start: Start");
        if (mconnectthread != null) {
            mconnectthread.cancle();
            mconnectthread = null;
        }
        if (macceptthread == null) {
            macceptthread = new AcceptThread();
            macceptthread.start();

        }
    }

    public void startClient(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startClient: already started");
        mprogressdialog = ProgressDialog.show(mcontext, "Bluetooth connecting", "please wait....", true);
        mconnectthread = new ConnectThread(device, uuid);
        mconnectthread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket btsocket;
        private final InputStream minputstream;
        private final OutputStream moutputstream;

        public ConnectedThread(BluetoothSocket socket) {
            btsocket = socket;
            InputStream tmppinput = null;
            OutputStream tmpoutput = null;

            try {
                mprogressdialog.dismiss();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            try {
                tmppinput = btsocket.getInputStream();
                tmpoutput = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            minputstream = tmppinput;
            moutputstream = tmpoutput;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = minputstream.read(buffer);
                    String incomingmessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "run: input:" + incomingmessage);
                } catch (IOException e) {
                    Log.e(TAG, "run: error readding input" + e.getMessage());
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: writing to output stream" + text);
            try {
                moutputstream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: error writing message" + e.getMessage());
            }
        }

        public void cancle() {
            try {
                btsocket.close();
            } catch (IOException e) {

            }
        }


    }

    private void connected(BluetoothSocket msocket, BluetoothDevice mdevice) {
        Log.d(TAG, "connected: starting");
        connectedthread = new ConnectedThread(msocket);
        connectedthread.start();

    }

    public void write(byte[] out) {
        ConnectedThread c;
        Log.d(TAG, "write: write's called");
        connectedthread.write(out);
    }

}
