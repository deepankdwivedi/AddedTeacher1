package com.added.addedteacher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.ListModel;

/**
 * Created by Asus 1 on 7/3/2016.
 */
public class ViewCircularFragment extends Fragment{

    Context context;
    //String url = "";\
    MyDatabase mydatabase;
    String link;
    int lengthOfUrl,lastIndex;
    int downloaded;
    ProgressDialog mProgressDialog;
    String fileName=null;
    Button btn;
    View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.view_circular,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Circular");
        System.out.println("student ListModel Class");
        context = getActivity().getBaseContext();
        mydatabase= MyDatabase.getDatabaseInstance(context);
        ListView lv = (ListView) getView().findViewById(R.id.listView1);




        mydatabase.get_list();
        if(ListModel.list_arraylist.size()==0)
        {
            setViewLayout(R.layout.nodataavailable);
        }
        lv.setAdapter(new dataListAdapter(getActivity().getBaseContext()));



    }

    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(v);
    }


    class dataListAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<List> list_arraylist;
        Context context;


        public dataListAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //   context.getSystemService(url);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return ListModel.list_arraylist.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            convertView= inflater.inflate(R.layout.row_search_list, null);


            TextView title = (TextView) convertView.findViewById(R.id.textViewName);
            ImageView img = (ImageView) convertView.findViewById(R.id.download);
            btn = (Button) convertView.findViewById(R.id.button2);
            title.setText(ListModel.list_arraylist.get(position).ar.get(0));

            link = ListModel.list_arraylist.get(position).ar.get(1);

            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
//			   TODO Auto-generated method stub

                    lengthOfUrl=link.length();
                    lastIndex=link.lastIndexOf("/");
                    System.out.println("Last index of / "+lastIndex);
                    fileName=link.substring(lastIndex+1);
                    downloaded=mydatabase.check_if_download(link);
                    if(downloaded==1)
                    {
                        final AlertDialog dialog;
                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        builder.setTitle("File Download");
                        builder.setMessage("It's look like you have already downloaded the file");
                        builder.setPositiveButton("Download",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DownloadFilepdf().execute(link,fileName);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog=builder.create();
                        dialog.show();
                    }
                    else
                    {
                        new DownloadFilepdf().execute(link,fileName);
                    }


                }
            });



            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + fileName);  // -> filename = maven.pdf
                    Uri path = Uri.fromFile(pdfFile);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try{
                        startActivity(pdfIntent);
                    }catch(ActivityNotFoundException e){
                        Toast.makeText(getActivity().getBaseContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            return convertView;
        }
    }

    private class DownloadFilepdf extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }

            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[1024 * 1024];
                int bufferLength = 0;
                long total = 0;

                while((bufferLength = inputStream.read(buffer))>0 ){
                    total += bufferLength;
                    // Publish the progress
                    publishProgress((int) (total * 100 / totalSize));

                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress dialog
            mProgressDialog.setProgress(progress[0]);
            // Dismiss the progress dialog
            //mProgressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());

            mProgressDialog.setMessage("Downloading " + fileName + "...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }


        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            boolean check = mydatabase.insert_circular_detail(link, 1);
            if (check == true) {
                Toast.makeText(getActivity().getBaseContext(), "Successfully Downloaded",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

}
