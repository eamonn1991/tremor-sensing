package com.example.chenz_000.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import android.bluetooth.BluetoothDevice;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.Manifest;
import android.content.pm.PackageManager;
import org.apache.commons.io.IOUtils;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;
import com.punchthrough.bean.sdk.message.SketchMetadata;
import com.punchthrough.bean.sdk.upload.SketchHex;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_BLUETOOTH = 0;

    private void showAccessPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.BLUETOOTH)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH},
                        MY_PERMISSIONS_REQUEST_BLUETOOTH);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_BLUETOOTH: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    String TAG = "Verkstad Bean";

    private Bean mBean;

    EditText et_email, et_subject, et_message;

    Button b_send;

    String message, time, duration, accel;
    int autosend = 1;
    int x = 0;

    /*
     code from: http://stackoverflow.com/questions/26854480/android-app-bluetooth-lightblue-bean
     */
    private List<Bean> beans = new ArrayList<>();


    BeanDiscoveryListener blistener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int i) {
            mBean = bean;
            mBean.connect(getApplicationContext(), myBeanListener);
        }



        @Override
        public void onDiscoveryComplete() {
        }
    };



    BeanListener myBeanListener = new BeanListener() {
        @Override
        public void onConnected() {

            Toast.makeText(getApplicationContext(), "CONNECTED TO BEAN", Toast.LENGTH_LONG).show();

            // reading things takes a callback:
            mBean.readSketchMetadata(new Callback<SketchMetadata>() {
                @Override
                public void onResult(SketchMetadata result) {
                    Log.i(TAG, "Running sketch " + result);
                }
            });

        }

        @Override
        public void onReadRemoteRssi(int i) {
        }
        @Override
        public void onError(BeanError b) {

        }

        @Override
        public void onConnectionFailed() {
            Toast.makeText(getApplicationContext(), "CONNECTION FAILED", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDisconnected() {
            Toast.makeText(getApplicationContext(), "BEAN DISCONNECTED", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSerialMessageReceived(byte[] bytes) {
            message = new String(bytes);
            x=1;
            autosend=1;

            if (message.length() > 1) {

                Log.d(TAG, String.valueOf(autosend));

            String acc_message = "102";
            TextView acc_output = (TextView) findViewById(R.id.acc_result);
            acc_output.setText(message);

                while (autosend == 1) {
                    if (x == 1) {
                        Intent autointent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:rosaliechan2017@u.northwestern.edu"));
                        autointent.putExtra(Intent.EXTRA_SUBJECT, "Tremor alert");
                        autointent.putExtra(Intent.EXTRA_TEXT, "Hello, this is to alert you that your patient had a tremor today. You can view the tremor data below." + "\n" + message);
//                        intent.setType("message/rfc822");
                        startActivity(autointent);

                        autosend = 0;
                        x = 0;
                    }
                }

            }



        }

        @Override
        public void onScratchValueChanged(ScratchBank i, byte[] bytes) {

        }
    };


    private void cancelBeanDiscovery(){
        BeanManager.getInstance().cancelDiscovery();
    }

    /*
     end of code
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showAccessPermission();
        BeanManager.getInstance().setScanTimeout(10);
        BeanManager.getInstance().startDiscovery(blistener);

        et_email = (EditText) findViewById(R.id.et_email);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_message = (EditText) findViewById(R.id.et_message);

        b_send = (Button) findViewById(R.id.b_send);



        b_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = et_email.getText().toString();
                String subject = et_subject.getText().toString();
                String customMessage = et_message.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, customMessage + "\n" + message);

                intent.setType("message/rfc822");


                startActivity(Intent.createChooser(intent, "Select Email app"));


            }
        });




    }



}
