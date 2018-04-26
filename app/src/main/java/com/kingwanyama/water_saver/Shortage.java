package com.kingwanyama.water_saver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


/**
 * Created by kingwanyama on 11/15/17.
 */

public class Shortage extends AppCompatActivity{
    String date;
    String meterNumber;
    String phoneNumber;

    TextView Shortage,EDIT1,EDIT2,EDIT3;
    FirebaseDatabase mFirebaseDatabase;
    ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortage);



        Shortage = (TextView) findViewById(R.id.shortageText);
        EDIT1 = (TextView) findViewById(R.id.edit1);
        EDIT2 = (TextView) findViewById(R.id.edit2);
        EDIT3 = (TextView) findViewById(R.id.edit3);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Reporting. Please Wait...");
        progressDialog.setCancelable(false);
    }


    public void ShotejSubmit(View view) {
        date = EDIT1.getText().toString().trim();
         meterNumber = EDIT2.getText().toString().trim();
        phoneNumber = EDIT3.getText().toString().trim();

    if (date.isEmpty()||phoneNumber.isEmpty()){
        Toast.makeText(this, "Kindly Fill in the Date and or Phone number", Toast.LENGTH_LONG).show();
    }else {
        shortage();
    }

    }


    private void shortage() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected==false){
            Toast.makeText(this, "Kindly check your Internet Connectivity", Toast.LENGTH_LONG).show();
        }
        else {
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] information = baos.toByteArray();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Shortage Information/" + System.currentTimeMillis());



        ref.setValue("Date=" + date + " Meter_Number=" + meterNumber + " Phone_Number=" + phoneNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                EDIT1.setText("");
                EDIT2.setText("");
                EDIT3.setText("");
                Intent main = new Intent(Shortage.this, MainActivity.class);
                startActivity(main);
                Toast.makeText(Shortage.this, "Successfully Reported", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Shortage.this, "Failed to Connect to the Db", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                }
        });

    } }


}

