package com.added.addedteacher;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import FileUtil.FilePath;
import MySharedPreferences.MySharedPreferences;
import model.Assignment;

/**
 * Created by Asus 1 on 6/28/2016.
 */
public class NotesSharingFragment extends Fragment {
    ArrayList<String> arrr, arrr1, arrr2;
    Spinner classSpinner ,subjectSpinner,topicSpinner;
    Button  selectPic,uploadPic,sendButton;
    private static final int PICK_FILE_REQUEST = 1;
    int selclas,selsec,chapter[],subjectId,chapterId;
    private static final String TAG = MainActivity.class.getSimpleName();
    Context context;
    private String selectedFilePath;
    TextView filePath;
    private String SERVER_URL = "http://192.168.0.13/uploadfile/UploadFileToServer.php";
    private ProgressDialog dialog;
    MyDatabase myDatabase;
    int subject_id[];
    String subject_detail[],chapter_detail[];
    private String notesId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.notes_sharing,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        subjectSpinner=(Spinner)getView().findViewById(R.id.spinnersubject);
        topicSpinner=(Spinner)getView().findViewById(R.id.spinnertopic);
        context=getActivity().getBaseContext();
        final String tea= new MySharedPreferences(context).getTeacherIdSharedPreferencesId();
        final String sch= new MySharedPreferences(context).getTeacherIdSharedPreferencesId();
        myDatabase = MyDatabase.getDatabaseInstance(context);
        filePath= (TextView) getView().findViewById(R.id.filePath);
        selectPic= (Button) getView().findViewById(R.id.selectpic);

        uploadPic= (Button) getView().findViewById(R.id.upload_pic);
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on upload button Click
                if (selectedFilePath != null) {
                    dialog = ProgressDialog.show(getActivity(), "", "Uploading File...", true);
                    notesId="notes-"+sch+"-"+tea+"-"+selclas+"-"+selsec+"-"+subjectId+"-"+chapterId;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            uploadFile(selectedFilePath);
                        }
                    }).start();
                } else {
                    Toast.makeText(getActivity(), "Please choose a File First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        arrr = new ArrayList<String>();
        arrr.add("Select Class");
        arrr1 = new ArrayList<String>();
        arrr1.add("Select Subject");
        arrr2 = new ArrayList<String>();
        arrr2.add("Select Chapter");

        subjectSpinner.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, arrr1));

        topicSpinner.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, arrr2));



        //upLoadServerUri = "http://192.168.43.126/UploadToServer.php";
        new classSpinner()
                .execute();


    }





    int class_id[];
    int sec_id[];
    String class_details[], sec_details[];
    public class classSpinner extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... arg0) {
            class_id=myDatabase.get_class_details("hemant");
            sec_id=myDatabase.get_section_details("hemant");
            int length= class_id.length;
            class_details=new String[length];
            sec_details=new String[length];
            class_details=myDatabase.get_class(class_id);
            sec_details=myDatabase.get_sec(sec_id);
            Assignment.classSection=new ArrayList<Assignment>();
            for(int i=0;i<class_details.length;i++)
            {

                Assignment.classSection.add(new Assignment(class_details[i], sec_details[i]));
            }

            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Locate the spinner
            classSpinner = (Spinner) getView().findViewById(R.id.spinnerclass);
            for(int i=0;i<Assignment.classSection.size();i++)
            {
                arrr.add(Assignment.classSection.get(i).class_value+Assignment.classSection.get(i).sec_value);
            }

            // Set Spinner Adapter
            classSpinner.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_dropdown_item, arrr));
            // Spinner on click
            classSpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            arrr1.clear();
                            arrr2.clear();

                            //finalSection=sec_details[arg2];
                            if(arrr.get(arg2).equals("Select Class")){
                            }else{
                                arg2=arg2-1;
                                new SubjectSpinner(class_id[arg2],sec_id[arg2])
                                        .execute();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

            //super.onPostExecute(result);
        }
    }

    public class SubjectSpinner extends AsyncTask<String, Void, Boolean> {

        public SubjectSpinner(int classId,int secId) {
            selclas = classId;
            selsec=secId;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            arrr1.clear();
            arrr1.add("Select Subject");
            arrr2.clear();
            arrr2.add("Select Chapter");
        }

        protected Boolean doInBackground(String... arg0) {

            subject_id=myDatabase.get_subject_details(selclas,selsec);
            subject_detail=new String[subject_id.length];

            for(int i=0;i<subject_id.length;i++)
            {
                subject_detail[i]=myDatabase.get_subject(subject_id[i]);
            }

            Assignment.subjects=new ArrayList<Assignment>();
            for(int i=0;i<subject_id.length;i++)
            {


                Assignment.subjects.add(new Assignment(subject_detail[i]));
            }

            return null;


        }

        protected void onPostExecute(Boolean result) {
            // Locate the spinner
            subjectSpinner = (Spinner) getView().findViewById(R.id.spinnersubject);
            for(int i=0;i<Assignment.subjects.size();i++)
            {
                arrr1.add(Assignment.subjects.get(i).subject);
            }
            // Set Spinner Adapter
            subjectSpinner.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_dropdown_item, arrr1));
            // Spinner on click
            subjectSpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            arrr2.clear();
                            //finalSubject =subject_detail[arg2];
                            if(arrr1.get(arg2).equals("Select Subject")){
                            }else{
                                arg2=arg2-1;
                                new TopicSpinner(subject_id[arg2])
                                        .execute();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

            //super.onPostExecute(result);
        }
    }

    public class TopicSpinner extends AsyncTask<String, Void, Boolean> {

        public TopicSpinner(int subSelected) {
            subjectId=subSelected;

        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            arrr2.clear();
            arrr2.add("Select Chapter");

        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            chapter=myDatabase.get_chapter_detail(subjectId);
            int length=chapter.length;
            chapter_detail=new String[length];
            for(int i=0;i<length;i++)
            {
                chapter_detail[i]=myDatabase.get_chapter(chapter[i]);
            }


            return null;
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // Locate the spinner
            topicSpinner = (Spinner) getView().findViewById(R.id.spinnertopic);
            for(int i=0;i<chapter_detail.length;i++)
            {
                arrr2.add(chapter_detail[i]);
            }
            // Set Spinner Adapter
            topicSpinner.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_dropdown_item, arrr2));
            // Spinner on click
            topicSpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            //finalTopic =arrr2.get(arg2-1);

                            if(arrr2.get(arg2).equals("Select Chapter")){


                            }else{
                                arg2=arg2-1;
                                chapterId=chapter[arg2];
                                System.out.print("ChapterId"+chapterId);
                                //final WebView learn2crack = (WebView)getView().findViewById(R.id.editorWebView1);

                                //learn2crack.loadUrl("http://addedportal.co.in/h_service/shreya_service/ck/ckeditor/ck2.php?class="+finalClass+"&subject="+finalSubject+"&chapter="+finalTopic);
                                // learn2crack.getSettings().setJavaScriptEnabled(true);
                                //learn2crack.getSettings().setLoadsImagesAutomatically(true);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }

                    });


        }
    }











    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("file/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
           startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(getActivity(), selectedFileUri);
                Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    filePath.setText(selectedFilePath);
                } else {
                    Toast.makeText(getActivity(), "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }




    public int uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            dialog.dismiss();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    filePath.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + notesId + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();
//                Toast.makeText(MainActivity.this,serverResponseCode+"   "+serverResponseMessage,Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            filePath.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/" + fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("File Not Found");
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("URL error!");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Cannot Read/Write File!");
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }




}
