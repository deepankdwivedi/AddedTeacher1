package com.added.addedteacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;

import MySharedPreferences.MySharedPreferences;

/**
 * Created by ASUS on 25-03-2016.
 */
public class TimeTableFragment extends Fragment {

    Context context;
    MyDatabase db;

    Button buttons[][] = new Button[12][12];
    //String periods[] = { "Days", "1", "2", "3", "4", "5", "6", "7" };
    //String days[] = { "Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun" };
    /*String data[][] = { { "#", "1", "2", "Break", "3", "4", "5", "6", "Break", "7","8","9" },
            { "Monday", "V-A", "-", "II-A", "IV-A", "V-C", "V-D","VII-C","","","","" },
            { "Tuesday", "V-A", "VI-B", "-", "IV-A", "V-C", "V-D","VII-C","","","","" },
            { "Wednesday", "V-A", "VI-B", "II-A", "IV-A", "-", "V-D","VII-C","","","","" },
            { "Thursday", "V-A", "VI-B", "II-A", "IV-A", "V-C", "-","VII-C","","","","" },
            { "Friday", "-", "VI-B", "II-A", "IV-A", "V-C", "V-D","VII-C","","","","" },
            { "Saturday", "V-A", "VI-B", "II-A", "IV-A", "V-C", "-","VII-C","","","","" }
    };*/
    String data[][] = { { "#", "1", "2", "3", "4", "5", "6", "7", "8", "9","10","11" },
            { "Monday", "V-A", "-", "II-A", "IV-A", "V-C", "V-D","VII-C","","","","" },
            { "Tuesday", "V-A", "VI-B", "-", "IV-A", "V-C", "V-D","VII-C","","","","" },
            { "Wednesday", "V-A", "VI-B", "II-A", "IV-A", "-", "V-D","VII-C","","","","" },
            { "Thursday", "V-A", "VI-B", "II-A", "IV-A", "V-C", "-","VII-C","","","","" },
            { "Friday", "-", "VI-B", "II-A", "IV-A", "V-C", "V-D","VII-C","","","","" },
            { "Saturday", "V-A", "VI-B", "II-A", "IV-A", "V-C", "-","VII-C","","","","" }
            };
    String dValues[][] = { { "#", "Name", "Entry Time", "1", "2", "3", "4", "5", "6", "7" },
            { "1", "Mathematics:Profit and Loss", "Free Period", "Mathematics:Numbers", "Mathematics:Algebra", "Physics:Current", "Mathematics:Ratio","Physics:Work" },
            { "2", "Mathematics:Profit and Loss", "Physics:Thermodynamics", "Free Period", "Mathematics:Algebra", "Physics:Current", "Physics:Current","Physics:Work" },
            { "3", "Mathematics:Profit and Loss", "Physics:Thermodynamics", "Mathematics:Numbers", "Mathematics:Algebra", "Free Period", "Physics:Current","Physics:Work" },
            { "4", "Mathematics:Profit and Loss", "Physics:Thermodynamics", "Mathematics:Numbers", "Mathematics:Algebra", "Physics:Current", "Free Period","Physics:Work" },
            { "5", "Free Period", "Physics:Thermodynamics", "Mathematics:Numbers", "Mathematics:Algebra", "Physics:Current", "Physics:Current","Physics:Work" },
            { "6", "Mathematics:Profit and Loss", "Physics:Thermodynamics", "Mathematics:Numbers", "Mathematics:Algebra", "Physics:Current", "Free Period","Physics:Work" },
            { "6", "Mathematics:Profit and Loss", "Physics:Thermodynamics", "Mathematics:Numbers", "Mathematics:Algebra", "Physics:Current", "Free Period","Physics:Work" }};


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    int total_lectures;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.time_table,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Time Table");
        db= MyDatabase.getDatabaseInstance(getActivity().getBaseContext());
        total_lectures=db.getTotalLectures()+1;
        System.out.println("total_lectures"+total_lectures);
        if(total_lectures != 0)
        {
            populateButton(total_lectures);
        }
        else
        {
            Toast.makeText(getActivity().getBaseContext(),"No Lectures found ",Toast.LENGTH_SHORT).show();
        }

    }

    private void populateButton(int total_lectures) {
        TableLayout table = (TableLayout) getView().findViewById(R.id.tablee);
        for (int row = 0; row < 7; row++) {
            TableRow tableRow = new TableRow(getActivity().getApplicationContext());

            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            table.addView(tableRow);

            for (int col = 0; col < total_lectures; col++) {
                final int Final_Col = col;
                final int Final_Row = row;
                if(row!=0 && col!=0)
                {
                    final Button button = new Button(getActivity().getApplicationContext());
                    button.setBackgroundColor(Color.TRANSPARENT);
                    button.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                    button.setBackgroundResource(R.drawable.border);
                    final String tea= new MySharedPreferences(getActivity().getBaseContext()).getTeacherIdSharedPreferencesId();
                    button.setText(db.getClassNameFromTimeTable((row+1),col,tea)+"-"+db.getSectionNameFromTimeTable((row+1),col,tea));
                    button.setTextColor(Color.BLACK);
                    button.setPadding(0, 0, 0, 0);
                    final int row1=row+1;
                    final int col1=col;

                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    getActivity().getApplicationContext());

                            builder.setTitle(db.get_subject(db.getSubjectIdFromTimeTable(row1,col1,tea)));
                            builder.setMessage(dValues[Final_Row][Final_Col])
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    // do things
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();

                            gridButton(Final_Row, Final_Col);



                        }

                    });

                    // New AlertDialog on button click
                    // Created by #Adil

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Subject to be Taught");

                            // Setting Dialog Message`
                            alertDialog.setMessage(db.get_subject(db.getSubjectIdFromTimeTable(row1,col1,tea)));

                            // Setting Icon to Dialog


                            // Setting OK Button
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog closed
                                    //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();


                        }
                    });

                    tableRow.addView(button);
                    buttons[row][col] = button;
                }
                else
                {
                    final Button button = new Button(getActivity().getApplicationContext());
                    button.setBackgroundColor(Color.TRANSPARENT);
                    button.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT, 1.0f));

                    button.setText(data[row][col]);
                    button.setTextColor(Color.BLACK);

                    int width = button.getWidth();
                    button.setMaxWidth(width);
                    button.setMinWidth(width);

                    int height = button.getHeight();
                    button.setMaxHeight(height);
                    button.setMinHeight(height);

                    button.setPadding(0, 0, 0, 0);
                    tableRow.addView(button);


                    buttons[row][col] = button;

                }

            }
        }

    }

    private void gridButton(int x, int y) {
        // Toast
        Button button = buttons[x][y];

        lockButtonSizes();

    }

    private void lockButtonSizes() {
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < total_lectures; col++) {
                Button button = buttons[row][col];
                int width = button.getWidth();
                button.setMaxWidth(width);
                button.setMinWidth(width);

                int height = button.getHeight();
                button.setMaxHeight(height);
                button.setMinHeight(height);

            }
        }
    }
}
