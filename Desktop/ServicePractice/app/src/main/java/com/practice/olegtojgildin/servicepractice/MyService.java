package com.practice.olegtojgildin.servicepractice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by olegtojgildin on 15/12/2018.
 */

public class MyService extends IntentService {

    public static final int MESSAGE_REGISTER_CLIENT = 0;
    public static final int MESSAGE_UNREGISTER_CLIENT = 1;
    public static final int MESSAGE_STOPPING = 2;
    public static final int MESSAGE_INFO = 3;
    public static final int MESSAGE_SET_VALUE = 4;
    public static boolean isBind;

    private List<Messenger> mClient=new ArrayList<Messenger>();

    private Messenger mMessenger = new Messenger(new IncomingHandler());

    public MyService() {
        super("MyService");
    }

    public static final Intent newIntent(Context context){
        return new Intent(context,MyService.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        isBind = true;

        try {
            for (int i = 0; i < 1000; i++) {
                if (!isBind) {
                    sendToClients(Message.obtain(null, MESSAGE_STOPPING));
                    return;
                }
                sendToClients(Message.obtain(null, MESSAGE_INFO, i));
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void sendToClients(Message message) {
        for (Messenger messenger : mClient) {
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_REGISTER_CLIENT:
                    mClient.add(msg.replyTo);
                    break;
                case MESSAGE_UNREGISTER_CLIENT:
                    mClient.remove(msg.replyTo);
                    break;
                case MESSAGE_SET_VALUE:
                    break;
            }
        }
    }
}
