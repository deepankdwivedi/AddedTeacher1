package com.added.addedteacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by adil on 26/05/16.
 */
public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("adil","receiver");
        Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();

        final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(1==1) {
                Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
                MainActivity mainActivity=new MainActivity();
                new GetOnConnectResponse(context).execute();
                //SyncIt syncIt=new SyncIt();
                //syncIt.new CheckServer(context).execute("http://"+"192.168.0.2"+"/services/connection_check.php");
            }


        }

    }
}
