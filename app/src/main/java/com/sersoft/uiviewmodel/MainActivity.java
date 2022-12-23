package com.sersoft.uiviewmodel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE_FRONT = 1;
    static final int REQUEST_IMAGE_CAPTURE_REAR = 0;
    private static final int PHOTO_ACTIVITY_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 100;
    private Button notificationBtn, searchBtn, goBackBtn, brainButton;
    private RadioGroup radioGroup;
    private RadioButton frontCameraBtn, rearCameraBtn;
    private EditText searchInput;
    private ImageView imageView;
    private TextView showDateTV;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String currentDate;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notificationBtn = findViewById(R.id.notificationBtn);
        searchInput = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchButton);
        frontCameraBtn = findViewById(R.id.frontCameraBtn);
        rearCameraBtn = findViewById(R.id.rearCameraBtn);
        radioGroup = findViewById(R.id.radioGroup);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("UiViewModel");

        brainButton = findViewById(R.id.brainButton);

//Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notify", "Notify", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        notificationBtn.setOnClickListener(v -> {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Notify")
                    .setContentTitle("Notification Title")
                    .setContentText("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                    .setSmallIcon(R.drawable.ic_bell_ring_black_24dp)
                    .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                    managerCompat.notify(1, builder.build());
                }
            },10000);
        });

//Google Search
        searchBtn.setOnClickListener(view -> {
            String searchWords = searchInput.getText().toString(); if (!searchWords.equals("")) {
                googleSearchBrowser(searchWords);
            }

        });

//Permission for using camera
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
                    },CAMERA_REQUEST_CODE);
        }

    }
    //Search with the browser
    private void googleSearchBrowser(String words) {
        try {
        Uri uri = Uri.parse("https://www.google.com/search?q=" + words);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    } catch (ActivityNotFoundException e) { e.printStackTrace();
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
    }
    }

//Taking photos
    public void openFrontCamera(View view) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("com.google.assistant.extra.USE_FRONT_CAMERA", true);
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", REQUEST_IMAGE_CAPTURE_FRONT);
        intent.putExtra("android.intent.extras.CAMERA_FACING", REQUEST_IMAGE_CAPTURE_FRONT);

        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        setContentView(R.layout.activity_photo);

    }
    public void openRearCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("com.google.assistant.extra.USE_BACK_CAMERA", true);
        intent.putExtra("android.intent.extra.USE_BACK_CAMERA", true);
        intent.putExtra("android.intent.extras.LENS_FACING_BACK", REQUEST_IMAGE_CAPTURE_REAR);
        intent.putExtra("android.intent.extras.CAMERA_FACING", REQUEST_IMAGE_CAPTURE_REAR);

        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        setContentView(R.layout.activity_photo);

    }

    //Show photo in other activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Intent[] intent = {new Intent(this, PhotoActivity.class)};

        imageView =findViewById(R.id.selectedImage);
        goBackBtn = findViewById(R.id.goBackButton);

        if (requestCode == CAMERA_REQUEST_CODE ) {
            assert data != null;
            Bundle extras = data.getExtras();
            //get capture image
            Bitmap captureImage = (Bitmap) extras.get("data");
            //set capture image
            imageView.setImageBitmap(captureImage);
        }else{
            imageView.setImageResource(R.drawable.ic_camera_black_24dp);
        }
        goBackBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // setContentView(R.layout.activity_main);
            onBackPressed();
        }
        });

    }

//Brainstorming
    @SuppressLint("SimpleDateFormat")
    public void showDateAndTime(View view) {
        imageView= findViewById(R.id.imageUTM);
        showDateTV = findViewById(R.id.showDateTV);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        currentDate = simpleDateFormat.format(calendar.getTime());

        imageView.setImageResource(R.drawable.iconutm);
        showDateTV.setText(currentDate);
        showDateTV.setVisibility(View.VISIBLE);

    }
}

