package com.kingwanyama.water_saver;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Leakage extends AppCompatActivity {

    private Button takePictureButton;
    private ImageView imageView;
    private File file;
    FirebaseStorage mFirebaseStorage;
    FirebaseDatabase mFirebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leakage);

        takePictureButton = (Button) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageview);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseStorage=FirebaseStorage.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Reporting. Please Wait...");
        progressDialog.setCancelable(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 100;

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    private  File saveImage(Bitmap bmp){
        String storageDirectory=Environment.getExternalStorageDirectory().toString();
        OutputStream outStream=null;
        File file=new File(storageDirectory, System.currentTimeMillis()+".png");
        if (file.exists()){
            file.delete();
            file=new File(storageDirectory,System.currentTimeMillis()+".png");
        }

        try {
            outStream=new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,100,outStream);
            outStream.flush();
            outStream.close();

        }catch (Exception e){
             e.printStackTrace();
             Log.d(TAG, "saveImage: "+e.getMessage());
        }
        return  file;
    }


   static   String TAG="PICTURE_FILE";

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            upload();

        }else{
            Log.d(TAG, "onActivityResult: Could not pick the photo");
        }

   }

    private void upload() {


        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected==false){
            Toast.makeText(this, "Kindly Check your internet Connectivity", Toast.LENGTH_LONG).show();
        }

        else{



        progressDialog.show();

        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();

        StorageReference ref=mFirebaseStorage.getReference().child("Image/"+System.currentTimeMillis()+".jpeg");
        UploadTask uploadTask=ref.putBytes(datas);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
                progressDialog.dismiss();
                Toast.makeText(Leakage.this, "Failed to Upload.Check Data Connection", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uri_file=taskSnapshot.getDownloadUrl();
                Log.d(TAG, "onSuccess: UPLOAD_URI "+uri_file.toString());

                //save to firebase db
                String pic_url=uri_file.toString();
                DatabaseReference dbRef=mFirebaseDatabase.getReference("Leakage Information/"+System.currentTimeMillis());
                dbRef.setValue(pic_url).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Leakage.this, "Successfully Reported", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Leakage.this, MainActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Leakage.this, "Failed to Connect to the Db", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
    }
}