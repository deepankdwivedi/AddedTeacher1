/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.added.addedteacher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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


public class LoginActivity extends Activity {
    private static final String TAG = "register";
            //RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputSchoolId;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db1;
    MyDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputSchoolId= (EditText) findViewById(R.id.school);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db=  MyDatabase.getDatabaseInstance(LoginActivity.this);

        // SQLite database handler
        db1 = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
               // String school=inputSchoolId.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        RegisterActivity.class);
//                startActivity(i);
                //finish();
            }
        });

    }

    /**
     * function to verify login details in mysql myDatabase
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        final AppController appController = new AppController();


        final JSONObject root = new JSONObject();
        String myPort = null;

        for (int i = 0; i < 1; i++) {
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
                    if (myPort != null) {
                        break;
                    }

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

        String tea = new MySharedPreferences(LoginActivity.this).getTeacherIdSharedPreferencesId();
        String sch = new MySharedPreferences(LoginActivity.this).getSchoolIdSharedPreferencesId();

        try {
            root.put("mac", wifiInfo.getMacAddress().toString());
            root.put("ip", Formatter.formatIpAddress(wifiInfo.getIpAddress()));
            root.put("user", inputEmail.getText().toString().trim());
            root.put("password",inputPassword.getText().toString().trim());
            root.put("schx", inputSchoolId.getText().toString().trim());
            root.put("portx", myPort);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db = MyDatabase.getDatabaseInstance(LoginActivity.this);
        db.getTimeStampFromSyncTable();
        //final JSONObject root= new JSONObject();

        if (SyncModel.added_sync_array_list != null) {
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
            final StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.URL_DOWNLOAD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //mPostCommentResponse.requestCompleted();
                    Log.e("xyz", "response : " + response.toString());
                    try {
                        MyDatabase db = MyDatabase.getDatabaseInstance(LoginActivity.this);
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
                                    if (ResponseReceiveModel.responseReceiveArrayList.get(m).columnName.equals("id")) {
                                        index = m;
                                        System.out.println("index of id :" + index);
                                        break;

                                    }

                                }
                                if (index != -1) {
                                    if (db.checkForUpdate(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue, key)) {
                                        System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue + " primary key value exist in database table: " + key);
                                        db.updateNewData(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue, key);
                                    } else {
                                        db.insertNewData(key);
                                        System.out.println(ResponseReceiveModel.responseReceiveArrayList.get(index).columnValue + " primary key value does not exist in database table: " + key);
                                    }
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
                        try {
                            JSONObject jsonDelete = json.getJSONObject("delete");

                            Iterator iteratorDelete = jsonDelete.keys();
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


                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.print(e.toString());
                        }


                        pDialog.dismiss();

                        //Log.e("adil","getting it = "+ json.getString("0"));
                        //myDatabase.checkForUpdate(json.getInt("id"),json.getString("school_code"),json.getString("school_name"),json.getString("board_id"),json.getString("website"));

                    }
                     catch (JSONException e) {
                        pDialog.dismiss();
                        e.printStackTrace();
                        System.out.print(e.toString());
                    }

                    //makeJSONObjectRequest();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    Log.e("Error response", "response error : " + error.toString());

                    //mPostCommentResponse.requestEndedWithError(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("object", root.toString());
                    params.put("user", inputEmail.getText().toString().trim());
                    params.put("mac", wifiInfo.getMacAddress());
                    params.put("dlastupdate", db.getDeleteLastupdate());
                    params.put("schx", inputSchoolId.getText().toString().trim());

                    return params;
                }

            };


            pDialog.setMessage("Logging in ...");
            showDialog();

            StringRequest strReq = new StringRequest(Method.POST,
                    AppConfig.URL_LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Login Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            // user successfully logged in
                            // Create login session


                            // Now store the user in SQLite
                            String uid = jObj.getString("uid");


                            //JSONObject user = jObj.getJSONObject("user");
                            String name = jObj.getString("name");
                            String email = jObj.getString("email");
                            String school = jObj.getString("school");
                            System.out.println("Login: name  " + name + " email: " + email + "  school:" + school + " uid:" + uid);
                            System.out.println(school);
                            session.setLogin(true, uid, school);
                            //String email = user.getString("user");
                        /*String created_at = user
                                .getString("created_at");
*/
                            // Inserting row in users table
                           // db1.addUser(name, email, uid, null);
                            pDialog.setMessage("Downloading Data");
                            pDialog.show();

                            // Launch main activity


                        } else {
                            // Error in login. Get the error message
                            //String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    response, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user", inputEmail.getText().toString().trim());
                    params.put("password", inputPassword.getText().toString().trim());
                    params.put("mac", wifiInfo.getMacAddress());
                    params.put("school", inputSchoolId.getText().toString().trim());

                    return params;
                }

            };

            appController.addToRequestQueue(strReq, tag_string_req, LoginActivity.this);
            appController.addToRequestQueue(sr, "Download Data", LoginActivity.this);
            // Adding request to request queue
            //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
