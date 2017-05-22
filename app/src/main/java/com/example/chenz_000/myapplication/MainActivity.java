package com.example.chenz_000.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
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

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.LedColor;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    Bean mBean;

    String TAG = "Verkstad Bean";

    /*
     code from: http://stackoverflow.com/questions/26854480/android-app-bluetooth-lightblue-bean
     */

    BeanDiscoveryListener blistener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int i) {
            mBean = bean;

            BeanManager.getInstance().cancelDiscovery();
            Toast.makeText(getApplicationContext(), "Bean discovered - "+this, Toast.LENGTH_LONG).show();

            // some information for the log
            Log.i(TAG,  "Bean discovered - name: "+ bean.getDevice().getName());
            Log.i(TAG,  "Bean discovered - address: "+ bean.getDevice().getAddress());
            Log.i(TAG,  "Bean discovered - BT class: "+ bean.getDevice().getBluetoothClass().toString());

            /*
             this is the connected fun
             */
            mBean.connect(getApplicationContext(), myBeanListener);

            Log.i(TAG, "Starting connection");
        }

        @Override
        public void onDiscoveryComplete() {
            int numbre = BeanManager.getInstance().getBeans().size();
            Collection<Bean> beans = BeanManager.getInstance().getBeans();

            Toast.makeText(getApplicationContext(), numbre+" Beans Found", Toast.LENGTH_LONG).show();
        }
    };

    BeanListener myBeanListener = new BeanListener() {
        @Override
        public void onConnected() {
            Toast.makeText(getApplicationContext(), "CONNECTED TO BEAN", Toast.LENGTH_LONG).show();

            // reading things takes a callback:
//            mBean.readSketchMetaData(new Callback<SketchMetaData>() {
//                @Override
//                public void onResult(SketchMetaData result) {
//                    Log.i(TAG, "Running sketch " + result);
//                }
//            });

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



    public void lightOff(View v) {
        // after the connection is instantiated, briefly flash the led:
        Log.i(TAG, "Sending command: light off");
        mBean.sendSerialMessage("L/0/0\n");
    }

    public void lightOn(View v) {
        // after the connection is instantiated, briefly flash the led:
        Log.i(TAG, "Sending command: light on");
        mBean.sendSerialMessage("L/0/99\n");
    }

    public void errorTest(View v) {
        // after the connection is instantiated, briefly flash the led:
        Log.i(TAG, "Sending command: lalala");
        mBean.sendSerialMessage("l");
    }



    EditText et_email, et_subject, et_message;

    Button b_send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BeanManager.getInstance().startDiscovery(blistener);

        setContentView(R.layout.activity_main);
        et_email = (EditText) findViewById(R.id.et_email);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_message = (EditText) findViewById(R.id.et_message);

        b_send = (Button) findViewById(R.id.b_send);

        b_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = et_email.getText().toString();
                String subject = et_subject.getText().toString();
                String message = et_message.getText().toString();

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
