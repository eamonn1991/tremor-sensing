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

    String message = et_message.getText().toString();


    /*
     code from: http://stackoverflow.com/questions/26854480/android-app-bluetooth-lightblue-bean
     */
    private List<Bean> beans = new ArrayList<>();


    BeanDiscoveryListener blistener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int i) {
            mBean = bean;
//            beans.add(bean);
//            mBean = beans.get(0);
            mBean.connect(getApplicationContext(), myBeanListener);
//            Log.d(TAG, beans);

//            mBean = bean;

//            BeanManager.getInstance().cancelDiscovery();
//            Toast.makeText(getApplicationContext(), "Bean discovered - "+this, Toast.LENGTH_LONG).show();
//
//            // some information for the log
//            Log.i(TAG,  "Bean discovered - name: "+ bean.getDevice().getName());
//            Log.i(TAG,  "Bean discovered - address: "+ bean.getDevice().getAddress());
//            Log.i(TAG,  "Bean discovered - BT class: "+ bean.getDevice().getBluetoothClass().toString());

            /*
             this is the connected fun
             */
//            mBean.connect(getApplicationContext(), myBeanListener);

//            Log.i(TAG, "Starting connection");
        }



        @Override
        public void onDiscoveryComplete() {
//            String listString = "";
//            for (Bean bean : beans) {
//                String beanname = bean.getDevice().getName();
////                Toast.makeText(getApplicationContext(), beanname, Toast.LENGTH_LONG).show();   // "Bean"              (example)
////                Log.d(TAG, bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)
////                Log.d(TAG, beanname);
//                mBean = bean;
//                listString += bean + "\t";
//                break;
//            }

            int numbre = BeanManager.getInstance().getBeans().size();
//            Collection<Bean> beans = BeanManager.getInstance().getBeans();
//            int numbre = beans.size();
//            Log.d(TAG, listString);

//            System.out.println(mBean.getDevice().getName());
//            System.out.println(mBean.getDevice().getAddress());

            Toast.makeText(getApplicationContext(), numbre+" Beans Found", Toast.LENGTH_LONG).show();
        }
    };



    BeanListener myBeanListener = new BeanListener() {
        @Override
        public void onConnected() {

            Toast.makeText(getApplicationContext(), "CONNECTED TO BEAN", Toast.LENGTH_LONG).show();


            mBean.readDeviceInfo(new Callback<DeviceInfo>() {
                @Override
                public void onResult(DeviceInfo deviceInfo) {
                    Log.d(TAG, deviceInfo.hardwareVersion());
                    Log.d(TAG, deviceInfo.firmwareVersion());
                    Log.d(TAG, deviceInfo.softwareVersion());
                }
            });

            // reading things takes a callback:
            mBean.readSketchMetadata(new Callback<SketchMetadata>() {
                @Override
                public void onResult(SketchMetadata result) {
                    Log.i(TAG, "Running sketch " + result);
                    if (result.toString().contains("Tremor!")) {
                        message = result.toString();
                    }
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
            Toast.makeText(getApplicationContext(), "CONNECTED FAILED", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDisconnected() {
            Toast.makeText(getApplicationContext(), "BEAN DISCONNECTED", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSerialMessageReceived(byte[] bytes) {
            Toast.makeText(getApplicationContext(), "Byte - " + new String(bytes), Toast.LENGTH_LONG).show();
            Log.i(TAG, "Serial received: " + new String(bytes));
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


//        mBean.connect(this, myBeanListener);

        et_email = (EditText) findViewById(R.id.et_email);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_message = (EditText) findViewById(R.id.et_message);

        b_send = (Button) findViewById(R.id.b_send);


        b_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = et_email.getText().toString();
                String subject = et_subject.getText().toString();
//                String message = et_message.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);

                intent.setType("message/rfc822");

                startActivity(Intent.createChooser(intent, "Select Email app"));
            }
        });


        Button b = (Button)this.findViewById(R.id.makethecall);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:999123"));
                startActivity(callIntent);
            }
        });


    }



}
