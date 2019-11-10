package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;
import static com.example.lab3.R.id.currentWeatherImage;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton mImageButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    Button chatRoomButton;
    Button weatherForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        mImageButton = findViewById(R.id.imageButton);
        TextView emailTyped  = findViewById(R.id.emailProfileField);
        chatRoomButton = findViewById(R.id.chatRoomButton);
        String email = getIntent().getStringExtra("ReserveName");
        weatherForecast = findViewById(R.id.weatherForecastButton);



        emailTyped.setText(email);

        mImageButton.setOnClickListener(clk -> {

            dispatchTakePictureIntent();


        });


        chatRoomButton.setOnClickListener(clk -> {
            Intent goToChatRoomActivity = new Intent(ProfileActivity.this, ChatRoomActivity.class);



            startActivity(goToChatRoomActivity);



        });



        weatherForecast.setOnClickListener(clk -> {
            Intent goToWeatherForecast = new Intent(ProfileActivity.this, WeatherForecast.class);



            startActivity(goToWeatherForecast);



        });



        Log.e(ACTIVITY_NAME,"In function: onCreate()");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME,"In function: onActivityResult()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME,"In function: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME,"In function: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME,"In function: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME,"In function: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME,"In function: onDestroy()");
    }
}