package com.added.addedteacher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.added.addedteacher.database.MyDatabase;

/**
 * Created by SWASTIKA on 18-05-2016.
 */
public class ViewAssignment extends Fragment{

 Context context;
    MyDatabase myDatabase;
    private TextView vClass,vSec,vSub,vChap,vSubline,vDetail;
    String d,classid,secid,subid,chapid;
    String []details=new String[6];
    String []ss;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.view_assignment,container,false);
        //Log.e("adil",d = getArguments().getString("heading"));



        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Assignment");
        context = getActivity().getBaseContext();
        myDatabase = MyDatabase.getDatabaseInstance(context);

        vClass=(TextView)getView().findViewById(R.id.classid);
        vSec=(TextView)getView().findViewById(R.id.secid);
        vSub=(TextView)getView().findViewById(R.id.subid);
        vChap=(TextView)getView().findViewById(R.id.chapid);
        vSubline=(TextView)getView().findViewById(R.id.subline);
        vDetail=(TextView)getView().findViewById(R.id.subtext);

        //Bundle bundle = getArguments();
        d = getArguments().getString("heading");
            Log.e("heading  ",d);

        details=myDatabase.get_particular_assign_details(d);
        Log.e("chapterid from myDatabase :", details[3]);
        classid=myDatabase.get_class(details[0]);
        secid=myDatabase.get_sec(details[1]);
        subid=myDatabase.get_sub(details[2]);
        chapid=myDatabase.get_chap_name(details[3]);



        ss = myDatabase.getSs(d);

        vClass.setText(classid);
        vSec.setText(secid);
        vSub.setText(subid);
        vChap.setText(chapid);
        vSubline.setText(ss[0]);
        vDetail.setText(ss[1]);







    }




}
