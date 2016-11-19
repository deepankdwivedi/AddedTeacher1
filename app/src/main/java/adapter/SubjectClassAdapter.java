package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.added.addedteacher.R;

import java.util.ArrayList;

public class SubjectClassAdapter extends BaseAdapter {
	Context context;
	ArrayList<String[]> subject_class_list;
	LayoutInflater inflater;

	public SubjectClassAdapter(Context context,
			ArrayList<String[]> subject_class_list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.subject_class_list = subject_class_list;
		inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return subject_class_list.get(0).length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView=inflater.inflate(R.layout.class_subject, null);
			holder=new ViewHolder();
			holder.classTextView=(TextView) convertView.findViewById(R.id.classTextView);
			holder.sectionTextView=(TextView) convertView.findViewById(R.id.sectionTextView);
			holder.subjectTextView=(TextView) convertView.findViewById(R.id.subjectTextView);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.classTextView.setText(subject_class_list.get(1)[position]);
		holder.sectionTextView.setText(subject_class_list.get(2)[position]);
		holder.subjectTextView.setText(subject_class_list.get(0)[position]);
		return convertView;
	}

	public class ViewHolder {
		TextView subjectTextView;
		TextView classTextView;
		TextView sectionTextView;
	}

}