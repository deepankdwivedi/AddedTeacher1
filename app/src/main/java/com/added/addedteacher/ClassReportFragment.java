package com.added.addedteacher;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by ASUS on 21-03-2016.
 */
public class ClassReportFragment extends Fragment{
    int total;
    int Num_Rows;
    int Num_Extra;
    int Extra_Row = 0;
    int attend[][]= new int [7][7];
    int attend_final[];
    private final static int Num_Col = 6;
    // TextView tv;
    String value;
    int id;
    Button buttons[][] = new Button[100][100];
    // String singledata[]= new String[100];
    String singledata[];

    String dataa[][] = {
            { "1.Alan", "2.David", "3.Alex Graves", "4.Daniel", "5.Michelle", "6.Alik","7.Mark" },
            { "8.Jeremy", "9.Miguel", "10.Brian", "11.Niel", "12.David", "13.Michael","14.Jack" },
            { "15.Daniel", "16.David", "17.D.B", "18.John", "19.Alfie", "20.Conleth","21.Aiden" },
            { "22.Jerome", "23.Charles", "24.Rory", "25.Julian", "26.Ben", "27.Jack","28.Michelle" }};

    TableLayout table;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Student Reports");
        table = (TableLayout) getView().findViewById(R.id.tablee);
        populateButton();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.class_list,container,false);
        return v;
    }
    private void populateButton() {

        for (int row = 0; row < 4; row++) {
            TableRow tableRow = new TableRow(getActivity().getApplicationContext());
            // table.addView(table);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            table.addView(tableRow);

            for (int col = 0; col < 7; col++) {
                final int Final_Col = col;
                final int Final_Row = row;
                final Button button = new Button(getActivity().getApplicationContext());
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                button.setText(dataa[row][col]);
                 button.setTextColor(Color.BLACK);
                //button.setBackgroundResource(R.drawable.button_shape);
                button.setPadding(0, 0, 0, 0);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        StudentReportFragment fragment= new StudentReportFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("student_name",button.getText().toString());
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
                    }

                });
                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }

    }
}
