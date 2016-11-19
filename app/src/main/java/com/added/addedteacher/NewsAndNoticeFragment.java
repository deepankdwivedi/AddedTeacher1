package com.added.addedteacher;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.added.addedteacher.database.MyDatabase;

import java.util.ArrayList;

import adapter.EventAdapter;
import model.EventModel;

/**
 * Created by ASUS on 24-03-2016.
 */
public class NewsAndNoticeFragment  extends Fragment{


    ListView ls;

    EventAdapter ea;
    String subject_arr[],detail_arr[],place_arr[],timings_arr[];
    ArrayList<EventModel> listt;
    MyDatabase myDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.news_and_notice,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("News and Notice");
        ls = (ListView) getView().findViewById(R.id.list);
        myDatabase=MyDatabase.getDatabaseInstance(getActivity().getBaseContext());
        listt = new ArrayList<EventModel>();
        new eventsAsyncTask()
                .execute();
    }



    public class eventsAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            EventModel.eventarray_list=new ArrayList<EventModel>();
            subject_arr=myDatabase.get_news_title();
            System.out.println(subject_arr.length);
            detail_arr=myDatabase.get_news_detail();
            place_arr=myDatabase.get_news_place();
            timings_arr=myDatabase.get_news_timings();
            for (int i = 0; i <subject_arr.length; i++)
            {
                EventModel.eventarray_list.add(new EventModel(subject_arr[i], place_arr[i], detail_arr[i], timings_arr[i]));

            }
            return true;



        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            EventAdapter adap = new EventAdapter(getActivity().getBaseContext(),
                    R.layout.list_item, listt);
            ls.setAdapter(adap);/*}*/
        }
    }

}
