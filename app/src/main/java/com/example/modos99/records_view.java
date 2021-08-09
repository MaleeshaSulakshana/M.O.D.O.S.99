package com.example.modos99;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class records_view extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    ImageButton previousPage, btnRotate;
    VideoView videoView;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_view);

//        Set status bar and navigation bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorWhite));
        window.setNavigationBarColor(this.getResources().getColor(R.color.colorPinkLight));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_records_view);

//        Firebase objects
        databaseReference = FirebaseDatabase.getInstance().getReference("Modos99").child("Recording");

//        Get key from recordings_show page
        String keyValue = getIntent().getStringExtra("Key");
        getValueFromFirebase(keyValue);

//        Call previous page
        previousPage = findViewById(R.id.btnBack);
        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Call openVideoView function
                openVideoView();
            }
        });

//        Call video rotate
        btnRotate = (ImageButton) findViewById(R.id.btnRotate);
        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateVideo();
            }
        });

    }

//    Double tap back button to close app
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Double Tap To Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    //    Function to call video view page
    public void openVideoView(){
        Intent recodeView = new Intent(this, recordings_show.class);
        startActivity(recodeView);
        finish();
    }

//    Function for get value data from firebase
    public void getValueFromFirebase(final String keyValue) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String batteryLevel = snapshot.child(keyValue).getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(records_view.this, "Firebase Not Connected",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void rotateVideo() {
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setRotation(90f);
    }

}