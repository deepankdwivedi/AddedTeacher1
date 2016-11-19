package com.added.addedteacher;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.added.addedteacher.database.MyDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Circular;

/**
 * Created by Asus 1 on 9/6/2016.
 */
@SuppressLint("NewApi") public class ToViewCircular1 extends Fragment {

    Context context;
    int j, i;
    MyDatabase myDatabase;
    String formatteddate;

    // String id, edate, event, detail, holiday;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.text, container, false);

    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        context = getActivity();
	/*
	 * LinearLayout layout = (LinearLayout ) findViewById(R.id.outer);
	 */final int N = 10; // total number of textviews to add
        TableLayout table = (TableLayout) getView().findViewById(R.id.table);
        myDatabase = MyDatabase.getDatabaseInstance(context);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());

        // SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        // formatteddate= dateFormat.format(new Date());
        //
        // final TextView[][] myTextViews = new TextView[N][N];
        // create an empty array;
        // String schedule[];
        myDatabase.get_circular(formatteddate);
        TableRow tablerow1 = new TableRow(getActivity());

        tablerow1.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        TextView textView = new TextView(getActivity().getBaseContext());
        textView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        textView.setText("Date");
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.border);
        tablerow1.addView(textView);
        TextView textView1 = new TextView(getActivity());
        textView1.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        textView1.setText("Circular");
        textView1.setTextColor(Color.parseColor("#000000"));
        textView1.setBackgroundResource(R.drawable.border);

        textView1.setGravity(Gravity.CENTER);
        textView1.setTypeface(null, Typeface.BOLD);
        // textView1.setTextSize(2);
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
        tablerow1.addView(textView1);
        TextView textView2 = new TextView(getActivity());
        textView2.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        textView2.setText("Signature");
        textView2.setTextColor(Color.parseColor("#000000"));
        textView2.setTypeface(null, Typeface.BOLD);
        textView2.setBackgroundResource(R.drawable.border);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
        textView2.setGravity(Gravity.CENTER);
        tablerow1.addView(textView2);
        TextView textView3 = new TextView(getActivity());


        table.addView(tablerow1);

        for (i = 0; i < Circular.circular_arraylist.size(); i++) {

			/*
			 * LinearLayout layout1 = new LinearLayout(this);
			 * layout1.setLayoutParams(new
			 * LinearLayout.LayoutParams(LinearLayout
			 * .LayoutParams.WRAP_CONTENT,LinearLayout
			 * .LayoutParams.WRAP_CONTENT,1.0f));
			 * layout1.setOrientation(LinearLayout.HORIZONTAL);
			 *
			 * for(j=0;j<AcademicCalendar.academic_calendar_arraylist.size();j++)
			 * {
			 */TableRow tablerow = new TableRow(getActivity());

            tablerow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            for (j = 0; j < Circular.circular_arraylist.get(i).ar.size()-1; j++) {

                final TextView tv = new TextView(getActivity());
                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setText(Circular.circular_arraylist.get(i).ar.get(j));
                tv.setBackgroundResource(R.drawable.border1);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                tv.setGravity(Gravity.CENTER);

                tablerow.addView(tv);

                tablerow.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        TableLayout tab = (TableLayout) v.getParent();
                        int index = tab.indexOfChild(v) - 1;
                        System.out.println(Circular.circular_arraylist
                                .get(index).ar.get(3));
                        String data = (Circular.circular_arraylist
                                .get(index).ar.get(3))+"";

                        ViewCircular fragment = new ViewCircular();

                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        fragment.setArguments(bundle);

                        getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

                    }
                });

            }

            table.addView(tablerow);

        }
    }
}
