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
public class TestMode extends Fragment {
    Button viewAssignment,createAssignment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.test_mode,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Test");
        createAssignment= (Button) getView().findViewById(R.id.createAssignment);
        viewAssignment= (Button) getView().findViewById(R.id.viewassignment);
        createAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFragment fragment=new TestFragment();
                getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
            }
        });

        viewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToViewTest fragment=new ToViewTest();
                getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
            }
        });
    }
}
