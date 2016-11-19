package com.added.addedteacher;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Asus 1 on 6/26/2016.
 */
public class UpdateDemo extends Fragment {

    private long enqueue;
    private DownloadManager dm;
    Button startDownload,showdownload;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_demo,container,false);
    }




        @Override
    public void onStart() {
        super.onStart();
            startDownload= (Button) getView().findViewById(R.id.button1);
            showdownload= (Button) getView().findViewById(R.id.button2);
            startDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse("http://www.vogella.de/img/lars/LarsVogelArticle7.png"));
                    enqueue = dm.enqueue(request);

                }
            });
            showdownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                    startActivity(i);
                }
            });
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                        long downloadId = intent.getLongExtra(
                                DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(enqueue);
                        Cursor c = dm.query(query);
                        if (c.moveToFirst()) {
                            int columnIndex = c
                                    .getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (DownloadManager.STATUS_SUCCESSFUL == c
                                    .getInt(columnIndex)) {

                                ImageView view = (ImageView) getView().findViewById(R.id.imageView1);
                                String uriString = c
                                        .getString(c
                                                .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                view.setImageURI(Uri.parse(uriString));
                            }
                        }
                    }
                }
            };

            getActivity().registerReceiver(receiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }


}
