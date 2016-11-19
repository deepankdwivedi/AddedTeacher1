package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.added.addedteacher.AppController;
import com.added.addedteacher.R;
import com.added.addedteacher.database.MyDatabase;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import model.TeacherSubstitutionModel;

/**
 * Created by Asus 1 on 9/2/2016.
 */
public class SubstitutionAdapter extends BaseAdapter {

    Context context;
    MyDatabase db;
    LayoutInflater inflater;

    public SubstitutionAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = MyDatabase.getDatabaseInstance(context);
    }

    @Override
    public int getCount() {
        return TeacherSubstitutionModel.teacherSubstitutionModelArrayList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.substitution_list_layout, null);
            viewHolder.classNameTextView = (TextView) convertView.findViewById(R.id.classNameTextView);
            viewHolder.absentTeacherTextView = (TextView) convertView.findViewById(R.id.absentTeacherTextView);
            viewHolder.confirmSubstitution = (Button) convertView.findViewById(R.id.confirmSubstitutionButton);
            viewHolder.cancelSubstitution = (Button) convertView.findViewById(R.id.cancelSubstitutionButton);
            viewHolder.lecture = (TextView) convertView.findViewById(R.id.lecture);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.classNameTextView.setText(db.getClassName(TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).class_id) + " " + db.getSectionName(TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).sec_id));
        viewHolder.absentTeacherTextView.setText(db.getTeacherNameFromId(TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).absentTeacherUsername));
        viewHolder.lecture.setText(TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).lecture);
        System.out.println("scmas" + TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).substitution_status);


        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.confirmSubstitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppController appController = new AppController();
                StringRequest sr = new StringRequest(Request.Method.POST, "http://192.168.1.6/services/substitution_update.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response" + response);
                        if (response.trim().equals("1")) {
                            long rowid = db.confirmSubstitution(TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).id);
                            System.out.println("in here");
                            Toast.makeText(context, "" + rowid, Toast.LENGTH_SHORT).show();
                            db.getSubstitutionList();
                            finalViewHolder.confirmSubstitution.setVisibility(View.INVISIBLE);
                            finalViewHolder.confirmSubstitution.setClickable(false);
                            finalViewHolder.cancelSubstitution.setVisibility(View.VISIBLE);
                            finalViewHolder.cancelSubstitution.setClickable(true);

                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Confirm Error Resposne",error.toString());

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).id + "");
                        params.put("substitution_status", "1");
                        return params;
                    }
                };

                appController.addToRequestQueue(sr, "Confirm Substitution", context);
            }


        });
        viewHolder.cancelSubstitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppController appController = new AppController();
                StringRequest sr = new StringRequest(Request.Method.POST, "http://192.168.1.6/services/substitution_update.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response" + response);
                        if (response.trim().equals("1")) {
                            System.out.println("in here");
                            long rowid = db.cancelSubstitution(TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).id);
                            Toast.makeText(context, "" + rowid, Toast.LENGTH_SHORT).show();
                            db.getSubstitutionList();
                            finalViewHolder.confirmSubstitution.setVisibility(View.VISIBLE);
                            finalViewHolder.confirmSubstitution.setClickable(true);
                            finalViewHolder.cancelSubstitution.setVisibility(View.INVISIBLE);
                            finalViewHolder.cancelSubstitution.setClickable(false);


                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Cancel Error Resposne",error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).id + "");
                        params.put("substitution_status", "0");
                        return params;
                    }
                };

                appController.addToRequestQueue(sr, "Confirm Substitution", context);
//                    new CancelSubstitution(finalViewHolder3.confirmSubstitution, position).execute();
            }


        });


        if (TeacherSubstitutionModel.teacherSubstitutionModelArrayList.get(position).substitution_status == 1) {
            viewHolder.confirmSubstitution.setVisibility(View.INVISIBLE);
            viewHolder.confirmSubstitution.setClickable(false);
            viewHolder.cancelSubstitution.setVisibility(View.VISIBLE);
            viewHolder.cancelSubstitution.setClickable(true);


        } else {
            viewHolder.confirmSubstitution.setVisibility(View.VISIBLE);
            viewHolder.confirmSubstitution.setClickable(true);
            viewHolder.cancelSubstitution.setVisibility(View.INVISIBLE);
            viewHolder.cancelSubstitution.setClickable(false);


        }
        return convertView;
    }

    public class ViewHolder {
        TextView classNameTextView;
        TextView absentTeacherTextView;
        TextView lecture;
        Button confirmSubstitution, cancelSubstitution;

    }

}