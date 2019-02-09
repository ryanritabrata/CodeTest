package com.tradeapplication.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

public class NetworkStatusChange extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        //start alarm when ever connected to internet which adds the location and

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isConnected = (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting())
                || (wifi != null && wifi.isConnectedOrConnecting());
        if (isConnected) {
            Intent spinnerNotification = new Intent("NETWORK_STATUS");
            spinnerNotification.putExtra("IS_Network_Available", "yes");
            LocalBroadcastManager.getInstance(context).sendBroadcast(spinnerNotification);
        } else {
            Intent spinnerNotification = new Intent("NETWORK_STATUS");
            spinnerNotification.putExtra("IS_Network_Available", "no");
            LocalBroadcastManager.getInstance(context).sendBroadcast(spinnerNotification);
        }
    }

}