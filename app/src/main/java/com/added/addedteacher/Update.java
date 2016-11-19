package com.added.addedteacher;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Asus 1 on 6/25/2016.
 */
public class Update extends Fragment {
    TextView downloadProgress;
    Button downloadButton;
    DownloadOperation downloadOperation = new DownloadOperation();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View c=inflater.inflate(R.layout.update,container,false);
        return c;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Update");
        downloadProgress = (TextView) getView().findViewById(R.id.download_progress);
        downloadButton =(Button) getView().findViewById(R.id.button);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadOperation.execute("http://192.168.1.6/app-update/app-release.apk");
                downloadButton.setEnabled(false);
            }
        });


    }

    private class DownloadOperation extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urlsFromActivity) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlsFromActivity[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileSize = connection.getContentLength();

                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(Environment.getExternalStorageDirectory()+"/added_teacher_update.apk");

                byte data[] = new byte[4096];
                long totalDownloaded = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    totalDownloaded += count;
                    // publishing the progress....
                    if (fileSize > 0) // only if totalDownloaded length is known
                        publishProgress((int) (totalDownloaded * 100 / fileSize));
                    outputStream.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            downloadProgress.setText("Download Progress:"+progress[0]+"%");
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                Toast.makeText(getActivity().getBaseContext(), "Download Failed with Error: " + result, Toast.LENGTH_LONG).show();
            else
                downloadProgress.setText("File Downloaded to "+Environment.getExternalStorageDirectory()+ " you can view the file using" +
                        " any File Manager Application");
        }
    }




}
