package com.added.addedteacher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.added.addedteacher.database.MyDatabase;

import java.util.ArrayList;

import adapter.SubjectClassAdapter;

public class LecturePlanActivity extends Fragment {
	MyDatabase db;
	ArrayList<String[]> subject_class_list;
	Context context;
	ListView subjectClassList;
	 LayoutInflater inflater;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.lectureplan_activity,container,false);
		this.inflater=inflater;
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("Lecture Plan");
		context = getActivity().getApplicationContext();
		db = MyDatabase.getDatabaseInstance(context);
		subject_class_list = db.getSubjectSection();
		subjectClassList=(ListView) getView().findViewById(R.id.subejctClassList);
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.class_header, subjectClassList, false);
		subjectClassList.addHeaderView(header,null,false);

		SubjectClassAdapter subjectClassAdapter = new SubjectClassAdapter(
				context, subject_class_list);
		subjectClassList.setAdapter(subjectClassAdapter);
		subjectClassList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				String[] sub_names=db.getSubjectNames();
				String[] class_names=db.getClassNames();
				String[] sec_name=db.getSectionName();
				ArrayList<String[]> subjectClassSectionIds=db.getClassIds();
				SubjectChapter fragment = new SubjectChapter();
				Bundle bundle = new Bundle();
				bundle.putString("classid", subjectClassSectionIds.get(0)[(arg2-1)]);
				bundle.putString("secid", subjectClassSectionIds.get(1)[(arg2-1)]);
				bundle.putString("subid", subjectClassSectionIds.get(2)[(arg2-1)]);
				bundle.putString("sub_name",sub_names[(arg2-1)]);
				bundle.putString("class_name",class_names[(arg2-1)]);
				bundle.putString("sec_name",sec_name[(arg2-1)]);
				fragment.setArguments(bundle);
				getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();


			}
		});
	}



}
