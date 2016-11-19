package com.added.addedteacher;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by ASUS on 05-04-2016.
 */
public class SubjectReportFragmentAdapter extends BaseAdapter {
    Context context;
    String[]subject;
    LayoutInflater inflater;
    public SubjectReportFragmentAdapter(Context context, String[] subject) {
        this.context=context;
        this.subject=subject;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subject.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=inflater.inflate(R.layout.subject_report_list,null);
        TextView subjectTextView= (TextView) v.findViewById(R.id.subjectTextView);
        subjectTextView.setText(subject[i]);
        subjectTextView.setTextColor(Color.BLACK);
        return v;
    }
}
