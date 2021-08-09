package com.example.modos99;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnStart, btnAddDevice;
    boolean doubleBackToExitPressedOnce = false;
    Dialog addDeviceDialog, removeDeviceDialog;

    Button btnAddDeviceCancel, btnAddDeviceIp, btnRemoveDeviceIp, btnRemoveDeviceCancel;
    EditText txtDeviceIp;
    TextView txtDeviceIpView;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Set status bar and navigation bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorYellow));
        window.setNavigationBarColor(this.getResources().getColor(R.color.colorWhite));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

//        Firebase objects
        databaseReference = FirebaseDatabase.getInstance().getReference("Modos99");

//        For call video view page
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoView();
            }
        });

        addDeviceDialog = new Dialog(MainActivity.this);
        addDeviceDialog.setContentView(R.layout.add_device);

        removeDeviceDialog = new Dialog(MainActivity.this);
        removeDeviceDialog.setContentView(R.layout.add_device);

//        Add device (dog)
        btnAddDevice = findViewById(R.id.btnAddDevice);
        btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDeviceDialog.show();

                btnAddDeviceCancel = (Button) addDeviceDialog.findViewById(R.id.btnAddDeviceCancel);
                btnAddDeviceCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDeviceDialog.dismiss();
                    }
                });

                btnAddDeviceIp = (Button) addDeviceDialog.findViewById(R.id.btnAddDeviceIp);
                btnAddDeviceIp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtDeviceIp = (EditText) addDeviceDialog.findViewById(R.id.txtAddDevice);
                        databaseReference.child("IP").setValue(txtDeviceIp.getText().toString());
                        txtDeviceIp.setText("");

                        addDeviceDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Device Added", Toast.LENGTH_SHORT).show();
                    }
                });

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

    public void openVideoView(){
        Intent videoView = new Intent(this, video_view.class);
        startActivity(videoView);
        finish();
    }

}