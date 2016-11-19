package com.added.addedteacher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Asus 1 on 6/25/2016.
 */
public class AssignmentMode extends Fragment {
    Button viewAssignment,createAssignment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.assignment_mode,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Assignment");
        createAssignment= (Button) getView().findViewById(R.id.createAssignment);
        viewAssignment= (Button) getView().findViewById(R.id.viewassignment);
        createAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssignmentFragment fragment=new AssignmentFragment();
                getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
            }
        });

        viewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToViewAssign fragment=new ToViewAssign();
                getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
            }
        });
    }
}
