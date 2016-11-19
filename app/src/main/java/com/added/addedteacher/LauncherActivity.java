package com.added.addedteacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by adil on 03/06/16.
 */

public class LauncherActivity extends AppCompatActivity {

    boolean isLoggedIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
             SharedPreferences sp=getSharedPreferences("TeacherLogin",MODE_PRIVATE);
                isLoggedIn=sp.getBoolean("isLoggedIn",false);

        if (isLoggedIn){
            startActivity(new Intent(LauncherActivity.this,MainActivity1.class));
            finish();
        }else {
            startActivity(new Intent(LauncherActivity.this,LoginActivity.class));
            finish();
        }

    }
}
