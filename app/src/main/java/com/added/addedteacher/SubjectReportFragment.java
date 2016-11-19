package com.added.addedteacher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by ASUS on 05-04-2016.
 */
public class SubjectReportFragment extends Fragment {
    ListView subjectReportListView;
    String subject[]={"V A"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.subject_report_fragment,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        getActivity().setTitle("Student Reports");

        subjectReportListView= (ListView) getView().findViewById(R.id.subjectReportListView);
        SubjectReportFragmentAdapter adapter=new SubjectReportFragmentAdapter(getActivity().getApplicationContext(),subject);
        subjectReportListView.setAdapter(adapter);
        subjectReportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClassReportFragment fragment=new ClassReportFragment();
             /*   Bundle bundle = new Bundle();
                bundle.putInt("id", i);
                fragment.setArguments(bundle);
             */   getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
            }
        });

    }
}
