package com.example.lab3;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton mImageButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    Button chatButton;
    Button weatherForecast;
    Button toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        mImageButton = findViewById(R.id.profilePictureButton);
        EditText editText = findViewById(R.id.userProfileEmail);
        chatButton = findViewById(R.id.chatButton);
        String email = getIntent().getStringExtra("ReserveName");
        weatherForecast = findViewById(R.id.weatherForecastButton);
        toolBar = findViewById(R.id.testToolbarButton);



        editText.setText(email);

        mImageButton.setOnClickListener(clk -> {

            dispatchTakePictureIntent();


        });


        chatButton.setOnClickListener(clk -> {
            Intent goToChatRoomActivity = new Intent(ProfileActivity.this, ChatRoomActivity.class);



            startActivity(goToChatRoomActivity);



        });



        weatherForecast.setOnClickListener(clk -> {
            Intent goToWeatherForecast = new Intent(ProfileActivity.this, WeatherForecast.class);



            startActivity(goToWeatherForecast);



        });

        toolBar.setOnClickListener(clk -> {
            Intent goToToolBar = new Intent(ProfileActivity.this, TestToolbar.class);



            startActivity(goToToolBar);



        });

        Log.e(ACTIVITY_NAME, "In Function: onCreate");

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In Function: onStart");
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In Function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In Function: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In Function: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In Function: onDestroy");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(ACTIVITY_NAME, "In Function: onActivityResult");
    }




    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    }




}