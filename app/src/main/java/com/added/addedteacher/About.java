package com.added.addedteacher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ASUS on 22-04-2016.
 */
public class About extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.about,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("About");

    }
}
