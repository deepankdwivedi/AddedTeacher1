package com.added.addedteacher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.added.addedteacher.database.MyDatabase;

import MySharedPreferences.MySharedPreferences;
import adapter.ClassListAdapter;
import model.ClassSectionModel;

/**
 * Created by Asus 1 on 8/5/2016.
 */
public class ClassAttendanceListFragment extends Fragment {
    ListView classAttendanceListView;
    MyDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.classs_list_attendance,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Student Attendance");
        classAttendanceListView= (ListView) getView().findViewById(R.id.classAttendanceListView);
        db=MyDatabase.getDatabaseInstance(getActivity().getBaseContext());
        db.getTeacherSubjectClasses(new MySharedPreferences(getActivity().getBaseContext()).getTeacherIdSharedPreferencesId());
        ClassListAdapter adapter= new ClassListAdapter(getActivity().getBaseContext());
        classAttendanceListView.setAdapter(adapter);
        classAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudentAttendanceListFragment fragment= new StudentAttendanceListFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("class_id", ClassSectionModel.classSectionModelArrayList.get(position).class_id);
                bundle.putInt("sec_id",ClassSectionModel.classSectionModelArrayList.get(position).sec_id);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
            }
        });
    }
}
