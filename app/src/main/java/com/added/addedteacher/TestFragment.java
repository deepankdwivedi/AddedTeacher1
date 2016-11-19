package com.added.addedteacher;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import MySharedPreferences.MySharedPreferences;
import model.TestCreation;

/**
 * Created by ASUS on 11-04-2016.
 */
public class TestFragment extends Fragment implements View.OnClickListener {

    private int PICK_IMAGE_REQUEST = 2;
    private String  UPLOAD_URL ="http://addedportal.co.in/h_service/ashwani_service/test.php" ;
    ArrayList<String> arrr, arrr1, arrr2;
    int selclas,selsec,chapter[],subjectId,chapterId;
    String selectedSubject;
    String finalClass="Select Class", finalSubject="Select Subject", finalTopic="Select Topic",finalSection;
    Button submitAssignment;
    EditText subjectlineText,assignmentDescription,date;
    private Button uploadButton, btnselectpic,btnsendAssignment;
    String testId="",subjectLine="",subjectDesc="";
    Spinner classSpinner ,subjectSpinner,topicSpinner;
    boolean insert_assignment;
    int subject_id[];
    String img,testid, subline,subtext;
    String subject_detail[],chapter_detail[];
    int classid, subid,chapid;
    private String imagepath = null;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
    Context context;
    private String upLoadServerUri = null;
    MyDatabase myDatabase;
    public static EditText dateEditText;
    private ImageButton selectDate;
    private Bitmap bmp;
    private String path=null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  v=inflater.inflate(R.layout.test_fragment,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Test");
        context=getActivity().getBaseContext();
        myDatabase=MyDatabase.getDatabaseInstance(context);
        subjectSpinner=(Spinner)getView()
                .findViewById(R.id.spinnersubject);
        dateEditText= (EditText) getView().findViewById(R.id.editText);
        selectDate= (ImageButton) getView().findViewById(R.id.button6);
        topicSpinner=(Spinner)getView().findViewById(R.id.spinnertopic);
        btnsendAssignment=(Button)getView().findViewById(R.id.sendbutton1);
        subjectlineText=(EditText)getView().findViewById(R.id.SubjectLineText1);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment= new DatePickerFragment();
                fragment.show(getFragmentManager(),"assigmentdatepicker");

            }
        });
        assignmentDescription=(EditText)getView().findViewById(R.id.AssignmentDetailText1);

        //Transparent editText
        //Adil
        subjectlineText.getBackground().setAlpha(50);
        assignmentDescription.getBackground().setAlpha(50);

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


    public static void populateSetDate(int year, int month, int day) {
        dateEditText.setText( day+ "/" + month + "/" + year);
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            populateSetDate(year,month,day);
        }
    }



    int class_id[];
    int sec_id[];
    String class_details[], sec_details[];
    public class classSpinner extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... arg0) {
            class_id=myDatabase.get_class_details(new MySharedPreferences(context).getTeacherIdSharedPreferencesId());
            sec_id=myDatabase.get_section_details(new MySharedPreferences(context).getTeacherIdSharedPreferencesId());
            int length= class_id.length;
            class_details=new String[length];
            sec_details=new String[length];
            class_details=myDatabase.get_class(class_id);
            sec_details=myDatabase.get_sec(sec_id);
            TestCreation.classSection=new ArrayList<TestCreation>();
            for(int i=0;i<class_details.length;i++)
            {

                TestCreation.classSection.add(new TestCreation(class_details[i], sec_details[i]));
            }

            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Locate the spinner
            classSpinner = (Spinner) getView().findViewById(R.id.spinnerclass);
            for(int i=0;i<TestCreation.classSection.size();i++)
            {
                arrr.add(TestCreation.classSection.get(i).class_value+TestCreation.classSection.get(i).sec_value);
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
                                classid=arg2;
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

            TestCreation.subjects=new ArrayList<TestCreation>();
            for(int i=0;i<subject_id.length;i++)
            {


                TestCreation.subjects.add(new TestCreation(subject_detail[i]));
            }

            return null;


        }

        protected void onPostExecute(Boolean result) {
            // Locate the spinner
            subjectSpinner = (Spinner) getView().findViewById(R.id.spinnersubject);
            for(int i=0;i<TestCreation.subjects.size();i++)
            {
                arrr1.add(TestCreation.subjects.get(i).subject);
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
                                subid=arg2;
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
                                //final WebView learn2crack = (WebView)findViewById(R.id.editorWebView1);

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
    boolean isBChoose=false,isBSnap=false;

    protected int uploadFile(String sourceFileUri) {
        // TODO Auto-generated method stub
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile=null;
        if (sourceFileUri!=null) {
            sourceFile = new File(sourceFileUri);
        }
        else {
            Toast.makeText(context,"Please Select a Pic",Toast.LENGTH_LONG).show();
        } if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :" + imagepath);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("Source File not exist :" + imagepath);
                }
            });

            return 0;

        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(UPLOAD_URL);

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " C:/wamp/www/uploads";
                            //messageText.setText(msg);
                            Toast.makeText(context,
                                    "File Upload Complete.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }

                // close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText
                        //.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(context,
                                "MalformedURLException", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
						/*messageText.setText("Got Exception : see logcat ");
						Toast.makeText(context,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();*/
                    }
                });
                Log.e("Upload file Exception",
                        "Exception : " + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnselectpic) {
            showFileChooser();
            isBSnap=true;
            isBChoose=false;
        } else if (v == uploadButton) {

            new UploadTask().execute();
        }
        else if(v==btnsendAssignment)
        {

            subline=subjectlineText.getText().toString();
            subtext = assignmentDescription.getText().toString();


            testId=selclas+"-"+selsec+"-"+subjectId+"-"+chapterId;

            System.out.print("Test "+testId);
            subjectLine=subjectlineText.getText().toString();

            subjectDesc=assignmentDescription.getText().toString();
            insert_assignment=myDatabase.insert_test_details(testId, selclas, subjectId,chapterId, subjectLine, subjectDesc,selsec,img);
            if(insert_assignment) {
                Toast.makeText(context, "Test Created", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Cration Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class UploadTask extends AsyncTask{


        @Override
        protected Object doInBackground(Object[] params) {
            uploadFile(path);
            return null;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("adil","onActivityResult"+ " "+ resultCode);
        if (resultCode==getActivity().RESULT_OK){
            if (isBSnap) {
                Bundle extras = data.getExtras();
               // bmp = (Bitmap) extras.get("data");

            }
        }


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            //path=getPath(filePath);
            //filePath.getPath();
            //getRealPathFromURI(filePath);
            Log.e("adil","path "+path);
            if (isBChoose) {

                //Bundle extras = data.getExtras();
                //bmp = (Bitmap) extras.get("data");
                //Getting the Bitmap from Gallery
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    Uri tempUri = getImageUri(getActivity(), bmp);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));

                    Log.e("adil","path : "+finalFile.getAbsolutePath().toString());

                    path=finalFile.getAbsolutePath().toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}
