package com.added.addedteacher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.added.addedteacher.database.MyDatabase;

import adapter.SubstitutionAdapter;
import model.TeacherSubstitutionModel;

/**
 * Created by Asus 1 on 9/2/2016.
 */
public class TeacherSubstitution extends Fragment {

    MyDatabase db;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.teacher_substitution,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView= (ListView) getView().findViewById(R.id.substitutionListView);
        db= MyDatabase.getDatabaseInstance(getActivity().getBaseContext());
        db.getSubstitutionList();
        System.out.println("Substitution List Size:"+TeacherSubstitutionModel.teacherSubstitutionModelArrayList.size());
        SubstitutionAdapter adapter= new SubstitutionAdapter(getActivity().getBaseContext());
        listView.setAdapter(adapter);
    }
}
