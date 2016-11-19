package adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.added.addedteacher.MainActivity;
import com.added.addedteacher.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import model.CalendarCollection;
import model.StudentAbsentPresentModel;


public class StudentCalendarAdapter extends BaseAdapter {
    private Context context;

    private java.util.Calendar month;
    public GregorianCalendar pmonth;

    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;
    TextView dayView;

    private ArrayList<String> items;
    public static List<String> day_string;
    private View previousView;
    public ArrayList<String> date_collection_arr;

    public StudentCalendarAdapter(Context getCalendarView, GregorianCalendar monthCalendar) {
        //this.date_collection_arr = present_dates;
        StudentCalendarAdapter.day_string = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = getCalendarView;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();

    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, null);

        }

        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = day_string.get(position).split("-");

        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // setting curent month's days in blue color.
            dayView.setTextColor(Color.BLACK);
        }

        if (day_string.get(position).equals(curentDateString)) {

            v.setBackgroundResource(R.drawable.current_date);
        }

        else {
            v.setBackgroundColor(Color.parseColor("#f9f9f9"));
        }
        MainActivity m=new MainActivity();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = (Date)formatter.parse(day_string.get(position));
            if(day_string.get(position).compareTo(curentDateString)<0 && m.getSchoolRunningStatus(startDate,context))
            {

                v.setBackgroundResource(R.drawable.rounded_calender_item_absent);
            }
            else
            {
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < StudentAbsentPresentModel.presentDaysArrayList.size(); i++) {
            if (day_string.get(position).equals(
                    StudentAbsentPresentModel.presentDaysArrayList.get(i))) {

                v.setBackgroundResource(R.drawable.rounded_calender_item_present);
            }

        }

        for (int i = 0; i < StudentAbsentPresentModel.absentDaysArrayList.size(); i++) {
            if (day_string.get(position).equals(
                    StudentAbsentPresentModel.absentDaysArrayList.get(i).date)) {

                v.setBackgroundResource(R.drawable.rounded_calender_item_absent);
               // setEventView(v, position, dayView);
            }
        }
        for (int i = 0; i < CalendarCollection.holiday_dates.size(); i++) {
            if (day_string.get(position).equals(
                    CalendarCollection.holiday_dates.get(i).holidayDate)) {

                v.setBackgroundResource(R.drawable.rounded_calendar_item_holiday);
                setEventView(v, position, dayView);
            }
        }

        dayView.setText(gridvalue);

        // create date string for comparison
        String date = day_string.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the items array
		/*
		 * ImageView iw = (ImageView) v.findViewById(R.id.date_icon); if
		 * (date.length() > 0 && items != null && items.contains(date)) {
		 * iw.setVisibility(View.VISIBLE); } else { iw.setVisibility(View.GONE);
		 * }
		 */

        // setEventView(v, position, dayView);

        return v;
    }

    public View setSelected(View view, int pos) {
		/*if (previousView != null) {
			previousView.setBackgroundColor(Color.parseColor("#343434"));


		}
		for(int i=0;i<CalendarCollection.date_collection_arr.size();i++)
		{
		if(day_string.get(pos).equals(CalendarCollection.date_collection_arr.get(i).presentDate))
				{
					previousView.setBackgroundResource(R.drawable.rounded_calender_item_present);
				}
		}
		view.setBackgroundColor(Color.CYAN);*/

        int len = day_string.size();
        if (len > pos) {
            if (day_string.get(pos).equals(curentDateString)) {

            } else {

                previousView = view;

            }

        }

        return view;
    }

    public void refreshDays() {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the three
         * month's (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        /**
         * setting the start date as previous month's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);

        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

    public void setEventView(View v, int pos, TextView txt) {

        int len = StudentAbsentPresentModel.absentDaysArrayList.size();
        for (int i = 0; i < len; i++) {

            String date = StudentAbsentPresentModel.absentDaysArrayList.get(i).date;
            String message = StudentAbsentPresentModel.absentDaysArrayList.get(i).reason;
            int len1 = day_string.size();
            if (len1 > pos) {

                if (day_string.get(pos).equals(date)) {
                   /* v.setBackgroundColor(Color.parseColor("#343434"));
                    v.setBackgroundResource(R.drawable.rounded_calender_item_absent);

                    txt.setTextColor(Color.WHITE);*/
                }

            }
        }

    }

    public void getPositionList(String date, final Activity act, final View vi) {

        int len = StudentAbsentPresentModel.absentDaysArrayList.size();
        for (int i = 0; i < len; i++) {

            String event_date = StudentAbsentPresentModel.absentDaysArrayList.get(i).date;

            String event_message = StudentAbsentPresentModel.absentDaysArrayList.get(i).reason;

            if (date.equals(event_date)) {


                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Date: " + event_date)
                        .setMessage("Reason: " + event_message)
                        .setPositiveButton(
                                "OK",
                                new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                break;
            } else {

            }
        }

    }

}