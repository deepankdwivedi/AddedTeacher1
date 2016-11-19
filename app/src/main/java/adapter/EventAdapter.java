package adapter;

import java.io.InputStream;
import java.util.ArrayList;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.added.addedteacher.R;

import model.EventModel;

public class EventAdapter extends BaseAdapter {
	ArrayList<EventModel> arrlist;
	int Resource;
	Context context;
	LayoutInflater vi;

	public EventAdapter(Context context, int resource, ArrayList<EventModel> objects) {
		Resource = resource;
		this.context = context;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return EventModel.eventarray_list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertview, ViewGroup parent) {
		ViewHolder holder;
		if (convertview == null) {
			convertview = vi.inflate(Resource, null);
			holder = new ViewHolder();
			holder.i = (ImageView) convertview.findViewById(R.id.i);
			holder.t1 = (TextView) convertview.findViewById(R.id.tv1);
			holder.t2 = (TextView) convertview.findViewById(R.id.tv2);
			holder.t3 = (TextView) convertview.findViewById(R.id.tv3);
			holder.t4 = (TextView) convertview.findViewById(R.id.tv4);
		
			convertview.setTag(holder);

		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		//new DownloadingImage( holder.i).execute("http://blog.bulksms1.com/wp-content/uploads/2014/09/bulk-sms-school.jpeg");
		
		holder.t1.setText(EventModel.eventarray_list.get(position).evnt_name);
		holder.t2.setText(EventModel.eventarray_list.get(position).evnt_place);
		holder.t3.setText(EventModel.eventarray_list.get(position).evnt_timmings);
		holder.t4.setText(EventModel.eventarray_list.get(position).desc);

		

		return convertview;

	}

	static class ViewHolder {
		public ImageView i;
		public TextView t1;
		public TextView t2;
		public TextView t3;
		public TextView t4;
		
		

	}

	private class DownloadingImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImge;

		public DownloadingImage(ImageView bmImge) {
			this.bmImge = bmImge;

		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay= urls[0];
			Bitmap imcon11=null;
			try{
				InputStream in= new  java.net.URL(urldisplay).openStream();
				imcon11=BitmapFactory.decodeStream(in);
			}
				
		
			
			
		
		catch(Exception e){
			Log.e("Error",e.getMessage());
		e.printStackTrace();
			
			
		}
			return imcon11;
		}
		protected void onPostExecute(Bitmap result){
			bmImge.setImageBitmap(result);
		}
	}
}