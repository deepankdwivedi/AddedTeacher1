package com.added.addedteacher;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;

import com.added.addedteacher.database.MyDatabase;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import model.SyncModel;

/**
 * Created by adil on 03/06/16.
 */
public class GetOnConnectResponse extends AsyncTask {

    Context context;
    MyDatabase db;

    GetOnConnectResponse(Context c){
        context=c;
    }

    String link;

    @Override
    protected Object doInBackground(Object[] params) {

        //address= (String) params[0];
        //address=address.replace("connection_check","device_connect");

        final JSONObject root=new JSONObject();
        String myPort=null;

        for (int i=0;i<1;i++)
        {
            //JSONArray jArry=new JSONArray();
            //JSONObject jsonObject=new JSONObject();
            try {
                //jsonObject.put("lastupdate",SyncModel.added_sync_array_list.get(i).lastupdate);

                WifiManager wifiMgr = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                //System.out.println(Formatter.formatIpAddress(wifiInfo.getIpAddress()));
                //System.out.println(wifiInfo.getSSID());
                //System.out.println(wifiInfo.getMacAddress());

                for(int port=2426;port<=3000;port++){



                    ServerSocket socket=new ServerSocket(port);
                    myPort= String.valueOf(Integer.parseInt(port+""));
                    Log.e("adil",myPort);
                    socket.close();

                    if (myPort!=null)
                        break;

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(myPort);

                /*
                jsonObject.put("mac",wifiInfo.getMacAddress().toString());
                jsonObject.put("ip",Formatter.formatIpAddress(wifiInfo.getIpAddress()));
                jsonObject.put("user","adil");
                jsonObject.put("school","school");
                jsonObject.put("port",myPort);
                */






            /*
            jArry.put(jsonObject);
            try {
                root.put(SyncModel.added_sync_array_list.get(i).table_name,jArry);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */

        }

        WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();


        try {
            root.put("Boom","P0342016");
            root.put("mac",wifiInfo.getMacAddress().toString());
            root.put("ip", Formatter.formatIpAddress(wifiInfo.getIpAddress()));

            root.put("user","TEA34-0152");

            root.put("schx","P0342016");
            root.put("portx",myPort);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        db= MyDatabase.getDatabaseInstance(context);
        db.getTimeStampFromSyncTable();
        //final JSONObject root= new JSONObject();

        if (SyncModel.added_sync_array_list!=null)
            for (int i=0;i<SyncModel.added_sync_array_list.size();i++)
            {
                JSONArray jArry=new JSONArray();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("lastupdate", SyncModel.added_sync_array_list.get(i).lastupdate);
                    //jsonObject.put("timetable",SyncModel.added_sync_array_list.get(i).table_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jArry.put(jsonObject);
                Log.e("xyz","Json array "+jArry.toString());

                try {
                    root.put(SyncModel.added_sync_array_list.get(i).table_name,jArry);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        //loopBreak=true;
        System.out.println("JSon String :"+root.toString());


        //jArry.put(jsonObject);
        /*
        try {
            root.put(SyncModel.added_sync_array_list.get(0).table_name,jArry);
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        // System.out.println("JSon String :"+root.toString());

        //sending


        /*
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost("http://192.168.1.5/services/device_connect.php");

        //convert parameters into JSON object
        JSONObject holder = jsonObject;
        //passes the results to a string builder/entity
        StringEntity se = null;
        try {
            se = new StringEntity(holder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //sets the post request as the resulting string
        httpost.setEntity(se);
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        Log.e("adil",responseHandler.toString());

*/
        Log.e("adil","http://192.168.0.2/services/device_connect.php");

        StringRequest sr = new StringRequest(Request.Method.POST,"http://192.168.0.2/services/device_connect.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //mPostCommentResponse.requestCompleted();
                Log.e("xyz","response : "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xyz","response error : "+error.toString());
                //mPostCommentResponse.requestEndedWithError(error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("object",root.toString());
                //params.put("pass",userAccount.getPassword());
                //params.put("comment", Uri.encode(comment));
                //params.put("comment_post_ID",String.valueOf(postId));
                //params.put("blogId",String.valueOf(blogId));
                Log.e("xyz","sent");
                return params;
            }


            /*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
            */
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.add(sr);

        Log.e("xyz",root.toString());



        return null;
    }



}

