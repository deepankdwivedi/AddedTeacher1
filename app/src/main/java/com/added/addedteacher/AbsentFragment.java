package com.added.addedteacher;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;

import java.util.ArrayList;

import MySharedPreferences.MySharedPreferences;

/**
 * Created by ASUS on 22-03-2016.
 */
public class AbsentFragment extends Fragment {
    String str[][];
    MyDatabase db;
    int total;
    int count = 0;
    EditText edit[];
    String name[];
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list_name = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.absent_fragment, container, false);

        return v;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        db = MyDatabase.getDatabaseInstance(getActivity().getApplicationContext());
        if(db.last_day_Absent(new MySharedPreferences(getActivity().getApplicationContext()).getTeacherIdSharedPreferencesId())!= null){
            str=db.last_day_Absent(new MySharedPreferences(getActivity().getApplicationContext()).getTeacherIdSharedPreferencesId());
            total=str[0].length;

//TextView tv=(TextView)findViewById(R.id.textView1);
//String temp="";
            int c=0;
            for(int i=0;i<total;i++)
                if(str[1][i].equals("na"))
                    c++;
            edit=new EditText[c];
            name=new String[c];

//tv.setText(temp+" ");
            populate();
        }
        else
        {
            TextView tv=(TextView)getView().findViewById(R.id.textView1);
            tv.setText("No Record Found For Last Day Absent Students");
        }

    }
    public void populate() {
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.tablee);

        for (int i = 0; i < total; i++) {

//			LinearLayout l = (LinearLayout) findViewById(R.id.table);
            LinearLayout l = new LinearLayout(getActivity().getApplicationContext());

            l.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            l.setOrientation(LinearLayout.HORIZONTAL);
            l.setGravity(Gravity.CENTER);
            l.setPadding(0, 5, 0, 5);
            LinearLayout l1 = new LinearLayout(getActivity().getApplicationContext());

            l1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
            l1.setOrientation(LinearLayout.HORIZONTAL);
            l1.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView tv = new TextView(getActivity().getApplicationContext());
            // TextView tvv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            //tv.setPadding(0, 5, 0, 5);
            // tvv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setText(str[0][i]);
            tv.setTextColor(Color.parseColor("#000000"));
            // tvv.setText(nameReason[1]);
            l1.addView(tv);
            // l1.addView(tvv);
            l.addView(l1);
            //layout.addView(l);
            if (str[1][i].equals("na")) {
                LinearLayout l2 = new LinearLayout(getActivity().getApplicationContext());
                l2.setOrientation(LinearLayout.HORIZONTAL);
                l2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

                EditText ed = new EditText(getActivity().getApplicationContext());
                //ed.setMinimumHeight(20);
                //ed.setMaxHeight(20);
                ed.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
                ed.setBackgroundColor(Color.parseColor("#000000"));
                edit[count]=ed;
                name[count]=str[2][i];
                count++;
                l2.addView(ed);
//				Button bt = new Button(this);
//			bt.setLayoutParams(new LinearLayout.LayoutParams(
//						LinearLayout.LayoutParams.MATCH_PARENT,
//					LinearLayout.LayoutParams.WRAP_CONTENT,3.0f));
//			 bt.setBackgroundResource(R.drawable.mybuttonn);
//				bt.setText("OK");
//				final String temp=nameReason[i];
//				final String reason=ed.getText().toString();
//				 bt.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							//SendDataToServer(temp,reason);
//							 Toast.makeText(Absent.this, " Submitted", Toast.LENGTH_LONG).show();
//						}
//					});
//				l2.addView(bt);
                l.addView(l2);
                //layout.addView(l);



            }

            else {
                LinearLayout l2 = new LinearLayout(getActivity().getApplicationContext());
                l2.setOrientation(LinearLayout.HORIZONTAL);
                l2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                l2.setGravity(Gravity.CENTER_HORIZONTAL);

                // TextView tv = new TextView(this);
                TextView tvv = new TextView(getActivity().getApplicationContext());
                tvv.setGravity(Gravity.CENTER_HORIZONTAL);
                tvv.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                //tv.setPadding(0, 5, 0, 5);
                // tvv.setGravity(Gravity.CENTER_HORIZONTAL);
                tvv.setText(str[1][i]);
                //tv.setTextColor(Color.parseColor("#000000"));
                // tvv.setText(nameReason[1]);
                l2.addView(tvv);
                l.addView(l2);
                //layout.addView(l);
            }
            layout.addView(l);
        }
        LinearLayout l3 = new LinearLayout(getActivity().getApplicationContext());
        l3.setOrientation(LinearLayout.HORIZONTAL);
        l3.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        l3.setGravity(Gravity.CENTER_HORIZONTAL);
        Button bt = new Button(getActivity().getApplicationContext());
        bt.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        //bt.setBackgroundResource(R.drawable.mybuttonn);
        bt.setText("OK");
        bt.setGravity(Gravity.CENTER);
        //final String temp=nameReason[i];
        //final String reason=ed.getText().toString();
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                update_reason();
                Toast.makeText(getActivity().getApplicationContext(), "Updated ", Toast.LENGTH_LONG).show();
                //finish();

            }
        });
        l3.addView(bt);
        layout.addView(l3);
    }
    public void update_reason()
    {
        for(int i=0;i<edit.length;i++)
        {
            String st=edit[i].getText().toString().trim();
            if(st !=null)
                list.add(st);
            list_name.add(name[i]);
        }
        db.last_day_absent_update(list, list_name);
//		TextView tv=(TextView)findViewById(R.id.textView1);
//		tv.setText(list+" ");
//		TextView tv1=(TextView)findViewById(R.id.textView2);
//		tv1.setText(list_name+" ");
    }
}