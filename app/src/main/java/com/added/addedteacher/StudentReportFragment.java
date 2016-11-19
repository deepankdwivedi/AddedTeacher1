package com.added.addedteacher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

/**
 * Created by ASUS on 21-03-2016.
 */
public class StudentReportFragment extends Fragment{


    int Arry[] = { R.id.viewContainer2, R.id.viewContainer3,
            R.id.viewContainer4, R.id.viewContainer5, R.id.viewContainer6,
            R.id.viewContainer7, R.id.viewContainer8, R.id.viewContainer9,
            R.id.viewContainer10, R.id.viewContainer11, R.id.viewContainer12,
            R.id.viewContainer13, R.id.viewContainer14, R.id.viewContainer15, };

    float end[] = { 1.0f, .5f, .75f, .25f, .9f, .25f, .7f, .5f, .7f, .4f, .7f,
            .7f, .7f, .7f };

    String col[] = { "#ffa700", "#008744", "#0057e7", "#d62d20", "#808000",
            "#800000", "#FF00FF", "#800000", "#301414", "#C0C0C0", "#808080",
            "#808080", "#808080", "#ffa700" };

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.student_report,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle=this.getArguments();
        getActivity().setTitle("Student Reports");
        String student_name=bundle.getString("student_name");
        TextView studenNameTextView= (TextView) getView().findViewById(R.id.teacherNameTextView);
        studenNameTextView.setText(student_name);
        formating();
    }

    private void formating() {
        int i;
        for (i = 0; i < 10; i++) {
            v = getView().findViewById(Arry[i]);
            v.setBackgroundColor(Color.parseColor(col[i]));
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog;
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Corrective Measures")/*.setItems((topicsToBeTaught.toArray(new String[topicsToBeTaught.size()])),null)*/;
                    builder.setMessage("This is sample Corrective Measures");
                    dialog=builder.create();
                    dialog.show();

                }
            });
            scaleView(v, 0f, end[i]);
        }
        for (int j = i; j < Arry.length; j++) {
            View v1 = getView().findViewById(Arry[j]);
            v1.setVisibility(View.GONE);


        }
    }
    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(1f, 1f,

                startScale, endScale, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);
        anim.setDuration(2500);
        anim.setFillAfter(true);
        v.startAnimation(anim);

    }
}
