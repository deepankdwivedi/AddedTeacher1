package com.added.addedteacher;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by adil on 01/06/16.
 */
public class VirtualPresenceFragment extends Fragment {


    String path=null;
    View view=null;
    Button bSnap, bUpload,bChoose;
    Spinner currentClassSpinner,classSpinner,sectionSpinner;
    ImageView snap;
    Bitmap bmp;
    boolean isBChoose=false,isBSnap=false;
    private int PICK_IMAGE_REQUEST = 2;

    private String UPLOAD_URL ="http://addedportal.co.in/h_service/ashwani_service/upload.php";
            //"http://simplifiedcoding.16mb.com/VolleyUpload/upload.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String serverResponseMessage="";

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Virtual Presence");
        snap = (ImageView) getView().findViewById(R.id.ivSnap);
        bChoose= (Button) getView().findViewById(R.id.bChoose);
        bChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                isBSnap=false;
                isBChoose=true;

            }
        });
        bSnap = (Button) getView().findViewById(R.id.bSnap);
        bSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBSnap=true;
                isBChoose=false;
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 0);
            }
        });
        bUpload = (Button) getView().findViewById(R.id.bUpload);
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadImage();
                //uploadFile(path);
                //uploadImagefunction();
                new UploadTask().execute();

            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null)
            view=inflater.inflate(R.layout.virtualpresence,container,false);

        return view;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = //encode(imageBytes);
                Base64.encodeToString(imageBytes, Base64.DEFAULT);

        Log.e("adil",encodedImage.length()+"");

        writeData(encodedImage);
        return encodedImage;
    }


    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            snap.setImageBitmap(bitmap);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            Log.e("adil","decode error : "+e.toString());
            return null;
        }
    }


    public void writeData(String data){

        File path = getActivity().getExternalFilesDir(null);
        //Then create your file object:

        File file = new File(path, "myfilename.txt");
        //RequiresPermission.Write a string to the file

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public class UploadTask extends AsyncTask{


        @Override
        protected Object doInBackground(Object[] params) {
            if(path!=null) {
                uploadFile(path);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText( getActivity().getBaseContext(),"Please select a pic",Toast.LENGTH_LONG).show();
        }
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

//        if (!sourceFile.isFile()) {
//
//            //dialog.dismiss();
//
////            Log.e("uploadFile", "Source File not exist :"
////                    +uploadFilePath + "" + uploadFileName);
//
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
////                    messageText.setText("Source File not exist :"
////                            +uploadFilePath + "" + uploadFileName);
//                }
//            });
//
//            return 0;
//
//        }
        if (1 == 1) {
            int serverResponseCode = 0;
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(UPLOAD_URL);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    Log.e("adil", "reading");
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
                                    + " http://www.androidexample.com/media/uploads/";
                            //+
                            //uploadFileName;

                            //messageText.setText(msg);
                            Toast.makeText(getActivity().getApplicationContext(), "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                Log.e("adil",ex.toString());
                //dialog.dismiss();
                ex.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(getActivity().getApplicationContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                // dialog.dismiss();
                Log.e("adil", e.toString());
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(getActivity().getApplicationContext(), "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("adil upload", "Exception : "
                        + e.toString());
            }
            //dialog.dismiss();
            Log.e("adil","server response code : "+serverResponseCode);
            return serverResponseCode;

        } // End else block
        return  0;
    }


    public void uploadImagefunction(){

        int serverResponseCode=0;
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String pathToOurFile = path;
                //"/data/file_to_send.mp3";
        String urlServer = UPLOAD_URL;
                //"http://192.168.1.1/handle_upload.php";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            outputStream = new DataOutputStream( connection.getOutputStream() );
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = connection.getResponseCode();
            serverResponseMessage = connection.getResponseMessage();

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception ex)
        {
            //Exception handling
            Log.e("adil",ex.toString());
        }

    }


    /*

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getActivity(), s , Toast.LENGTH_LONG).show();
                        Log.e("Adil",s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        Log.e("adil",volleyError.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bmp);

                //Getting Image Name
                String name = "Notes";
                //editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, "uploaded_file");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        //requestQueue.add(stringRequest);
    }

    */


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("adil","onActivityResult"+ " "+ resultCode);
        if (resultCode==getActivity().RESULT_OK){
            if (isBSnap) {
                Bundle extras = data.getExtras();
                bmp = (Bitmap) extras.get("data");
                snap.setImageBitmap(bmp);
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
                //Setting the Bitmap to ImageView
                    snap.setImageBitmap(bmp);
            }
        }

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}
