package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.added.addedteacher.R;

import model.ClassSectionModel;

/**
 * Created by Asus 1 on 8/5/2016.
 */
public class ClassListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    public ClassListAdapter(Context context)
    {
        this.context=context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return ClassSectionModel.classSectionModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null )
        {
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.classsectionlistayout,null);
            holder.classTextView= (TextView) convertView.findViewById(R.id.classSectionListTextView);
            convertView.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.classTextView.setText(ClassSectionModel.classSectionModelArrayList.get(position).c_name+"  "+ClassSectionModel.classSectionModelArrayList.get(position).section);
        return convertView;
    }
    public class ViewHolder
    {
        TextView classTextView;
    }
}
