package com.added.addedteacher;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.added.addedteacher.database.MyDatabase;


@SuppressLint("NewApi") public class ViewCircular extends Fragment {

	Context context;
	MyDatabase myDatabase;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		   return inflater.inflate(R.layout.notes1, container, false);	   
		   
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 context = getActivity().getBaseContext();
		
		myDatabase = MyDatabase.getDatabaseInstance(context);
		TextView tv = (TextView) getView().findViewById(R.id.heading);
		TextView tv1 = (TextView) getView().findViewById(R.id.date);
		TextView tv2 = (TextView) getView().findViewById(R.id.detail);
		TextView tv3 = (TextView) getView().findViewById(R.id.signature);
		TextView tv4 = (TextView) getView().findViewById(R.id.username);
		String t, d;
		//Intent i = getIntent();
		
		Bundle bundle = getArguments();
		d = bundle.getString("data");

		// t = i.getStringExtra("text");
		//d = i.getStringExtra("data");
		// Bundle extras = i.getExtras();
		//
		// String value=extras.getString("circular");
		// tv.setText(d);
		String[] notice = myDatabase.circularp(d);

		tv1.setText(notice[0]);
		tv2.setText(notice[1]);
		tv3.setText(notice[2]);
		tv.setText(notice[3]);
		//tv4.setText("To- "+notice[4]);

	}

}
