package adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.added.addedteacher.R;

import java.util.ArrayList;

import model.StudentModel;

public class StudentAdapter1 extends BaseAdapter  {
		ArrayList<StudentModel> arrlist;
		int Resource;
		Context context;
		LayoutInflater vi;
		public StudentAdapter1(Context context, int resource, ArrayList<StudentModel> listarr) {
			
			arrlist = listarr;
			Resource = resource;
			this.context = context;
			vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// TODO Auto-generated constructor stub
		}

		

		
		public View getView(int position, View convertview, ViewGroup parent) {
			ViewHolder holder;
			if (convertview == null) {
				convertview = vi.inflate(Resource, null);
				holder = new ViewHolder();
				
				holder.t1 = (TextView) convertview.findViewById(R.id.rollno);
				holder.t2 = (TextView) convertview.findViewById(R.id.label);
				holder.attendanceStatus= (TextView) convertview.findViewById(R.id.attendanceStatus);
				convertview.setTag(holder);

			} else {
				holder = (ViewHolder) convertview.getTag();
			}
			
			
			holder.t2.setText(arrlist.get(position).getName());
			holder.t1.setText(arrlist.get(position).getRollno());
			if (arrlist.get(position).getAttendanceStatus())
			{
				holder.attendanceStatus.setText("P");
				holder.attendanceStatus.setTextColor(Color.GREEN);
			}
			else
			{

				holder.attendanceStatus.setText("A");
				holder.attendanceStatus.setTextColor(Color.RED);
			}

			

			return convertview;

		}

		static class ViewHolder {
			
			public TextView t1,t2,attendanceStatus;
			
			
			
			

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrlist.size();
		}




		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}




		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		

			
		}


