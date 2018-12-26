package com.practice.olegtojgildin.servicepractice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    TextView textView;
    Button stopService;
    private Messenger mService;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();
        initListener();
        bindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindService();
    }

    public void initViews() {
        textView = findViewById(R.id.result);
        stopService = findViewById(R.id.stopService);
    }

    public void initListener() {
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyService.isBind = false;
            }
        });
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MyService.MESSAGE_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Log.d("SecondActivity", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            Log.d("SecondActivity", "disconnected");
        }
    };


    public void bindService() {
        bindService(MyService.newIntent(SecondActivity.this), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBindService() {
        Message msg = Message.obtain(null, MyService.MESSAGE_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(mServiceConnection);
        Toast.makeText(SecondActivity.this, "Service stopped", Toast.LENGTH_LONG).show();
    }


    public static final Intent newIntent(Context context) {
        return new Intent(context, SecondActivity.class);
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyService.MESSAGE_STOPPING:
                    textView.setText(getString(R.string.stoppedService));
                    break;
                case MyService.MESSAGE_INFO:
                    textView.setText(msg.obj.toString());
                    break;
            }
        }
    }
}
