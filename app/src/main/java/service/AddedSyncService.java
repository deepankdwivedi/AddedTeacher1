package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;

import com.added.addedteacher.AppConfig;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import MySharedPreferences.MySharedPreferences;
import model.ResponseReceiveModel;
import model.SyncModel;
import model.UploadRowDataModel;

/**
 * Created by ASUS on 09-04-2016.
 */
public class AddedSyncService extends Service {
    public MyDatabase db;
    ArrayList<String> tableNameArrayList,tableColumnArrayList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this,"Alarm start",Toast.LENGTH_SHORT).show();
        final JSONObject root = new JSONObject();
        String myPort = null;

        for (int i = 0; i < 1; i++) {
            db= MyDatabase.getDatabaseInstance(this);
            //JSONArray jArry=new JSONArray();
            //JSONObject jsonObject=new JSONObject();
            try {
                //jsonObject.put("lastupdate",SyncModel.added_sync_array_list.get(i).lastupdate);

                WifiManager wifiMgr = (WifiManager) getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                //System.out.println(Formatter.formatIpAddress(wifiInfo.getIpAddress()));
                //System.out.println(wifiInfo.getSSID());
                //System.out.println(wifiInfo.getMacAddress());

                for (int port = 2426; port <= 3000; port++) {


                    ServerSocket socket = new ServerSocket(port);
                    myPort = String.valueOf(Integer.parseInt(port + ""));
                    //Log.e("adil", myPort);
                    socket.close();
                    if (myPort != null)
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








        }

        WifiManager wifiMgr = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        final WifiInfo wifiInfo = wifiMgr.getConnectionInfo();


        try {

            root.put("mac", wifiInfo.getMacAddress().toString());
            root.put("ip", Formatter.formatIpAddress(wifiInfo.getIpAddress()));
            root.put("user", new MySharedPreferences(this).getTeacherIdSharedPreferencesId());
            root.put("dlastupdate",db.getDeleteLastupdate());
            root.put("schx", new MySharedPreferences(this).getSchoolIdSharedPreferencesId());
            root.put("portx", myPort);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        db = MyDatabase.getDatabaseInstance(this);
        db.getTimeStampFromSyncTable();
        //final JSONObject root= new JSONObject();

        if (SyncModel.added_sync_array_list != null)
            for (int i = 0; i < SyncModel.added_sync_array_list.size(); i++) {
                JSONArray jArry = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("lastupdate", SyncModel.added_sync_array_list.get(i).lastupdate);
                    //jsonObject.put("timetable",SyncModel.added_sync_array_list.get(i).table_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jArry.put(jsonObject);
                Log.e("xyz", "Json array " + jArry.toString());

                try {
                    root.put(SyncModel.added_sync_array_list.get(i).table_name, jArry);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        System.out.println("JSon String :" + root.toString());
        Log.e("adil", AppConfig.URL_DOWNLOAD);
        StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.URL_DOWNLOAD, new Response.Listener<String>() {
            //StringRequest sr = new StringRequest(Request.Method.POST, "http://192.168.1.7/db/connect.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //mPostCommentResponse.requestCompleted();
                Log.e("xyz", "response : " + response.toString());
                try {
                    MyDatabase db= MyDatabase.getDatabaseInstance(AddedSyncService.this);
                    JSONObject json = new JSONObject(response);
                    JSONObject jsonInner = json.getJSONObject("sync");

                    Iterator iterator = jsonInner.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        System.out.println("Array :" + key);

                        JSONArray d = jsonInner.getJSONArray(key);

                        System.out.println("Array Length  " + d.length());
                        // JSONObject issue = json.getJSONObject(key);
                        for (int i = 0; i < d.length(); i++) {
                            ResponseReceiveModel.responseReceiveArrayList = new ArrayList<>();
                            JSONObject j = d.getJSONObject(i);
                            Iterator iterator1 = j.keys();
                            while (iterator1.hasNext()) {
                                String key1 = (String) iterator1.next();
                                String keyValue = j.getString(key1);
                                ResponseReceiveModel.responseReceiveArrayList.add(new ResponseReceiveModel(key1, keyValue));
                            }
                            int index=-1;
                            for (int m = 0; m < ResponseReceiveModel.responseReceiveArrayList.size(); m++) {
                                if (ResponseReceiveModel.responseReceiveArrayList.get(m).columnName.equals("id")) {
                                    index=m;
                                    System.out.println("index of id :"+index);
                                    break;

                                }

                            }
                            if(index!=-1)
                            {
                                if(db.checkForUpdate(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue,key))
                                {
                                    System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue+" primary key value exist in database table: "+key);
                                    db.updateNewData(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue,key);
                                }
                                else
                                {
                                    db.insertNewData(key);
                                    System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue+" primary key value does not exist in database table: "+key);
                                }
                            }
                            else
                            {
                                System.out.println("primary key value not in json");
                            }

                            for (int k = 0; k < ResponseReceiveModel.responseReceiveArrayList.size(); k++) {

                                System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(k).columnName + ":" + ResponseReceiveModel.responseReceiveArrayList.get(k).columnValue);

                            }
                            //System.out.print("\n"+key+" row "+i+" Ends Here ");
                                        /*jsonKeys.indexOf()
                                        MyDatabase myDatabase= new MyDatabase(MainActivity.this);
                                        myDatabase.checkForUpdate();*/
                        }

                    }
                    try {

                        JSONObject jsonDelete = json.getJSONObject("delete");

                        Iterator iteratorDelete = jsonDelete.keys();
                        if (iteratorDelete.hasNext())
                        {
                            while (iteratorDelete.hasNext()) {
                                String key = (String) iteratorDelete.next();
                                System.out.println("Array :" + key);

                                JSONArray d = jsonDelete.getJSONArray(key);

                                System.out.println("Array Length  " + d.length());
                                for (int i = 0; i < d.length(); i++) {
                                    ResponseReceiveModel.responseReceiveArrayList = new ArrayList<>();
                                    JSONObject j = d.getJSONObject(i);
                                    Iterator iterator1 = j.keys();
                                    while (iterator1.hasNext()) {
                                        String key1 = (String) iterator1.next();
                                        String keyValue = j.getString(key1);
                                        ResponseReceiveModel.responseReceiveArrayList.add(new ResponseReceiveModel(key1, keyValue));
                                    }
                                    int index = -1;
                                    for (int m = 0; m < ResponseReceiveModel.responseReceiveArrayList.size(); m++) {
                                        if (ResponseReceiveModel.responseReceiveArrayList.get(m).columnName.equals("id")) {
                                            index = m;
                                            System.out.println("index of id :" + index);
                                            break;

                                        }

                                    }
                                    if (index != -1) {
                                        if (db.checkForDelete(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue, key)) {
                                            System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue + " primary key value exist in database for delete in table: " + key);
                                            db.deleteData(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue, key);
                                        } else {

                                            System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue + " primary key value does not exist in database for delete in table: " + key);
                                        }
                                    } else {
                                        System.out.println("primary key value not in json");
                                    }

                                    for (int k = 0; k < ResponseReceiveModel.responseReceiveArrayList.size(); k++) {

                                        System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(k).columnName + ":" + ResponseReceiveModel.responseReceiveArrayList.get(k).columnValue);

                                    }

                                }

                            }
                        }


                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                        System.out.print(e.toString());
                    }


                    //Log.e("adil","getting it = "+ json.getString("0"));
                    //myDatabase.checkForUpdate(json.getInt("id"),json.getString("school_code"),json.getString("school_name"),json.getString("board_id"),json.getString("website"));



                }
                catch (JSONException e) {
                    e.printStackTrace();
                    System.out.print(e.toString());
                }

                //makeJSONObjectRequest();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error response", "response error : " + error.toString());
                //mPostCommentResponse.requestEndedWithError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("object", root.toString());
                params.put("user", new MySharedPreferences(AddedSyncService.this).getTeacherIdSharedPreferencesId());
                params.put("mac", wifiInfo.getMacAddress());
                params.put("schx", new MySharedPreferences(AddedSyncService.this).getSchoolIdSharedPreferencesId());

                return params;
            }

        };


        final JSONObject root1 = new JSONObject();
        try {
            root1.put("mac", wifiInfo.getMacAddress());
            root1.put("user",new MySharedPreferences(AddedSyncService.this).getTeacherIdSharedPreferencesId());
            root1.put("schx",new MySharedPreferences(AddedSyncService.this).getSchoolIdSharedPreferencesId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db = MyDatabase.getDatabaseInstance(this);
        tableNameArrayList=db.getTableNameFromDatabase();


        if (tableNameArrayList!= null)
            for (int i = 0; i < tableNameArrayList.size(); i++) {
                JSONArray jsonArray= new JSONArray();
                System.out.println("\n"+tableNameArrayList.get(i));
                tableColumnArrayList=db.getColumnDataFromTable(tableNameArrayList.get(i));
                db.getRowDataFromTable(tableNameArrayList.get(i));

                for(int j=0;j<UploadRowDataModel.rowDataModelArray.size();j++)
                {
                    JSONObject jsonObject= new JSONObject();
                    for(int column=0;column<tableColumnArrayList.size();column++) {
                        try {
                            jsonObject.put(tableColumnArrayList.get(column),getColumnData(column,j));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(getColumnData(column,j));
                    }
                    jsonArray.put(jsonObject);
                }
                try {
                    root1.put(tableNameArrayList.get(i),jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Server Upload Response", response);
                try {
                    MyDatabase db = MyDatabase.getDatabaseInstance(AddedSyncService.this);
                    JSONObject json = new JSONObject(response);
                    JSONObject jsonInner = json.getJSONObject("sync");

                    Iterator iterator = jsonInner.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        System.out.println("Array :" + key);

                        JSONArray d = jsonInner.getJSONArray(key);

                        System.out.println("Array Length  " + d.length());
                        // JSONObject issue = json.getJSONObject(key);
                        for (int i = 0; i < d.length(); i++) {
                            ResponseReceiveModel.responseReceiveArrayList = new ArrayList<>();
                            JSONObject j = d.getJSONObject(i);
                            Iterator iterator1 = j.keys();
                            while (iterator1.hasNext()) {
                                String key1 = (String) iterator1.next();
                                String keyValue = j.getString(key1);
                                ResponseReceiveModel.responseReceiveArrayList.add(new ResponseReceiveModel(key1, keyValue));
                            }
                            int index = -1;
                            for (int m = 0; m < ResponseReceiveModel.responseReceiveArrayList.size(); m++) {
                                if (ResponseReceiveModel.responseReceiveArrayList.get(m).columnName.equals("cid")) {
                                    index = m;
                                    System.out.println("index of cid :" + index);
                                    break;

                                }

                            }
                            if (index != -1) {
                                db.updateNewUploadData(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue, key);

                            } else {
                                System.out.println("primary key value not in json");
                            }

                            for (int k = 0; k < ResponseReceiveModel.responseReceiveArrayList.size(); k++) {

                                System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(k).columnName + ":" + ResponseReceiveModel.responseReceiveArrayList.get(k).columnValue);

                            }
                            //System.out.print("\n"+key+" row "+i+" Ends Here ");
                                        /*jsonKeys.indexOf()
                                        MyDatabase myDatabase= new MyDatabase(MainActivity.this);
                                        myDatabase.checkForUpdate();*/
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })        {
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("object", root1.toString());
                params.put("user", new MySharedPreferences(AddedSyncService.this).getTeacherIdSharedPreferencesId());
                params.put("mac", wifiInfo.getMacAddress());
                params.put("schx", new MySharedPreferences(AddedSyncService.this).getSchoolIdSharedPreferencesId());

                Log.e("data uploaded is :", root1.toString());
                return params;

            }

        };

        Log.e("Final Json Object",root1.toString());
        //db.testDatabase();


        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(sr);
        mRequestQueue.add(stringRequest);

        Log.e("xyz", root.toString());


        return super.onStartCommand(intent, flags, startId);
    }



    public String getColumnData(int column,int index)
    {

        switch (column)
        {

            case 0:
                return UploadRowDataModel.rowDataModelArray.get(index).column1;
            case 1:
                return UploadRowDataModel.rowDataModelArray.get(index).column2;
            case 2:
                return UploadRowDataModel.rowDataModelArray.get(index).column3;
            case 3:
                return UploadRowDataModel.rowDataModelArray.get(index).column4;
            case 4:
                return UploadRowDataModel.rowDataModelArray.get(index).column5;
            case 5:
                return UploadRowDataModel.rowDataModelArray.get(index).column6;
            case 6:
                return UploadRowDataModel.rowDataModelArray.get(index).column7;
            case 7:
                return UploadRowDataModel.rowDataModelArray.get(index).column8;
            case 8:
                return UploadRowDataModel.rowDataModelArray.get(index).column9;
            case 9:
                return UploadRowDataModel.rowDataModelArray.get(index).column10;
            case 10:
                return UploadRowDataModel.rowDataModelArray.get(index).column11;
            case 11:
                return UploadRowDataModel.rowDataModelArray.get(index).column12;
            case 12:
                return UploadRowDataModel.rowDataModelArray.get(index).column13;
            case 13:
                return UploadRowDataModel.rowDataModelArray.get(index).column14;
            case 14:
                return UploadRowDataModel.rowDataModelArray.get(index).column15;
            case 15:
                return UploadRowDataModel.rowDataModelArray.get(index).column16;
            case 16:
                return UploadRowDataModel.rowDataModelArray.get(index).column17;
            case 17:
                return UploadRowDataModel.rowDataModelArray.get(index).column18;
            case 18:
                return UploadRowDataModel.rowDataModelArray.get(index).column19;
            case 19:
                return UploadRowDataModel.rowDataModelArray.get(index).column20;


        }
        return null;

    }

}
