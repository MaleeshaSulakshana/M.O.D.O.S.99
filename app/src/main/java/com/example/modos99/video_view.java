package com.example.modos99;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class video_view extends AppCompatActivity {

    boolean hasFocus = true;
    boolean doubleBackToExitPressedOnce = false;
    ImageButton nextPage, btnUp, btnDown, btnLeft, btnRight;
    TextView txtBattery;
    Switch switchLight, switchBark;

    selectedRadio sRadio = new selectedRadio();

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

//        Set status bar and navigation bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorBlack));
        window.setNavigationBarColor(this.getResources().getColor(R.color.colorBlack));

//        Firebase objects
        databaseReference = FirebaseDatabase.getInstance().getReference("Modos99");

//        Create ui components
        txtBattery = findViewById(R.id.txtBattery);

//        Call show battery level function
        showBatteryLevel();

        nextPage = findViewById(R.id.btnNextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Call openRecodeShow function
                openRecodeShow();
            }
        });

//        Click events for control buttons
        btnUp = findViewById(R.id.btnUp);
        btnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    UpdateOn("Up");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    UpdateOff();
                }
                return true;
            }
        });

        btnDown = findViewById(R.id.btnDown);
        btnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    UpdateOn("Down");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    UpdateOff();
                }
                return true;
            }
        });

        btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    UpdateOn("Left");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    UpdateOff();
                }
                return true;
            }
        });

        btnRight = findViewById(R.id.btnRight);
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    UpdateOn("Right");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    UpdateOff();
                }
                return true;
            }
        });

        switchLight = findViewById(R.id.switchLight);
        switchLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    databaseReference.child("Light").setValue("On");
                } else {
                    databaseReference.child("Light").setValue("Off");
                }

            }
        });

        switchBark = findViewById(R.id.switchBark);
        switchBark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    databaseReference.child("Bark").setValue("On");
                } else {
                    databaseReference.child("Bark").setValue("Off");
                }

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

//    Function to call recordings show page
    public void openRecodeShow(){
        Intent recodeView = new Intent(this, recordings_show.class);
        startActivity(recodeView);
        finish();
    }

//    Function for check which radio button checked
    public String checkedRadioButton() {

        RadioButton radioArm, radioBody, radioHead;
        radioArm = findViewById(R.id.radioArm);
        radioBody = findViewById(R.id.radioBody);
        radioHead = findViewById(R.id.radioHead);

        String value = "";

        if (radioBody.isChecked()){
            value = "Body";
        }
        else if (radioHead.isChecked()){
            value = "Head";
        }
        else if (radioArm.isChecked()){
            value = "Arm";
        }

        return value;
    }

//    Function for update fire base "On"
    public void UpdateOn(String command) {
        String value = checkedRadioButton();
        sRadio.setRadioValues(value, command);
        databaseReference.child(value).child(command).setValue("On");
    }

//    Function for update fire base "Off"
    public void  UpdateOff() {
        String value = sRadio.getRadioValue();
        String command = sRadio.getBtnCommand();
        databaseReference.child(value).child(command).setValue("Off");
    }

//    Function for show device battery level
    public void showBatteryLevel() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String batteryLevel = snapshot.child("Battery").getValue().toString();
                txtBattery.setText(batteryLevel+"%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(video_view.this, "Firebase Not Connected",Toast.LENGTH_SHORT).show();
            }
        });
    }

}