package com.added.addedteacher;

import android.animation.Animator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Asus 1 on 5/6/2016.
 */
public class AddedStore extends Fragment {
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.added_fragment,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("ADD-Ed Store");
    }
}
