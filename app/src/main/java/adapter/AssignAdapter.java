package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.added.addedteacher.R;

import java.util.List;

import model.ViewAssignmentModel;

/**
 * Created by SWASTIKA on 21-05-2016.
 */
public class AssignAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    AssignmentViewHolder holder;
    private List<ViewAssignmentModel> list = null;
    //private ArrayList<ViewAssignmentModel> arraylist;


    public AssignAdapter(Context context, List<ViewAssignmentModel> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        //this.arraylist = new ArrayList<ViewAssignmentModel>();
        //this.arraylist.addAll(list);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ViewAssignmentModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row=convertView;
        if (convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.row_text,parent,false);
            holder=new AssignmentViewHolder(row);
            row.setTag(holder);

        }else{
            holder= (AssignmentViewHolder) row.getTag();
        }

        holder.sub_line.setText(list.get(position).sub_line);
        holder.classes.setText(list.get(position).className);
        holder.subject.setText(list.get(position).subject);
        return row;
    }

    public class AssignmentViewHolder {
        TextView classes;
        TextView sub_line;
        TextView subject;

        AssignmentViewHolder(View item){
            classes= (TextView) item.findViewById(R.id.classheading);
            subject= (TextView) item.findViewById(R.id.subjectheading);
            sub_line= (TextView) item.findViewById(R.id.heading);
        }

    }

}
