package com.added.addedteacher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.added.addedteacher.database.MyDatabase;

import java.util.GregorianCalendar;

import adapter.StudentCalendarAdapter;

/**
 * Created by Asus 1 on 7/2/2016.
 */
public class StudentAttendanceCalendarFragment extends Fragment {
    TextView textView;
    public GregorianCalendar cal_month, cal_month_copy;
    private StudentCalendarAdapter cal_adapter;
    private TextView tv_month;
    Context context;
    MyDatabase db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_attendance_calendar_fragment,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //textView= (TextView) getView().findViewById(R.id.textView12);
        textView= (TextView) getView().findViewById(R.id.TextView07);
        Bundle bundle=this.getArguments();
        String username=bundle.getString("username");
        String name =bundle.getString("name");
        textView.setText(name);
        //textView.setText(username);
        getActivity().setTitle("Student Attendance");

        context=getActivity().getBaseContext();
        db=MyDatabase.getDatabaseInstance(context);
        db.getHolidayDates();
        db.getStudentPresentDates(username);
        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new StudentCalendarAdapter(getActivity(), cal_month);
        tv_month = (TextView) getView().findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy",
                cal_month));

        ImageView previous = (ImageView) getView().findViewById(R.id.ib_prev);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ImageButton next = (ImageButton) getView().findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        GridView gridview = (GridView) getView().findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((StudentCalendarAdapter) parent.getAdapter())
                        .setSelected(v, position);
                String selectedGridDate = StudentCalendarAdapter.day_string
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((StudentCalendarAdapter) parent.getAdapter())
                        .setSelected(v, position);

                ((StudentCalendarAdapter) parent.getAdapter()).getPositionList(
                        selectedGridDate, getActivity(),v);

            }


        });


    }


    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy",
                cal_month));
    }
}
