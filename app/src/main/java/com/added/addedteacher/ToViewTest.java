package com.added.addedteacher;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.added.addedteacher.database.MyDatabase;

import java.util.ArrayList;

import adapter.AssignAdapter;
import model.ViewAssignmentModel;

/**
 * Created by Asus 1 on 6/26/2016.
 */
public class ToViewTest extends Fragment {

    ListView list;
    ViewAssignmentModel model;
    Context context;
    MyDatabase mdb;
    ArrayList<ViewAssignmentModel> arrayList= new ArrayList<ViewAssignmentModel>();
    public String testmode;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.listview_page,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Test");
        context = getActivity().getBaseContext();
        mdb= MyDatabase.getDatabaseInstance(getActivity().getBaseContext());
        list= (ListView) getView().findViewById(R.id.listView);
        list.setAdapter(new AssignAdapter(getActivity(),mdb.get_test()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String head = mdb.get_test().get(position).getSub_line();
                //   String heading= "asdf";
                ViewTest fragment = new ViewTest();
                Bundle bundle = new Bundle();
                bundle.putString("heading", head);
                fragment.setArguments(bundle);
                FragmentTransaction transaction=getActivity().getFragmentManager().beginTransaction();
                transaction.add(R.id.framecontainer,fragment,"view").
                        addToBackStack(null).
                        commit();

                //String heading=arrayList.get(position).getSub_line();




            }
        });







    }
}
