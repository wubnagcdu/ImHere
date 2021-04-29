package org.paul.lib.mgr;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.text.TextUtils;

public class NetChangeBroad extends BroadcastReceiver {

    private ConnectivityManager.NetworkCallback callback;

    @Override
    public void onReceive(Context context, Intent intent) {
        handleReceive(context,intent);
    }

    private void handleReceive(Context context,Intent intent) {
        if(isInitialStickyBroadcast()){
            return;
        }
        String action = intent.getAction();
        if(TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=null;
            networkInfo=connectivityManager.getActiveNetworkInfo();

        }
    }
}
