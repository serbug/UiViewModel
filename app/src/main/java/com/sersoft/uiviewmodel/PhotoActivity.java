package com.sersoft.uiviewmodel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PhotoActivity extends AppCompatActivity {

    private Button goBackBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        goBackBtn = findViewById(R.id.goBackButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("UiViewModel");



        goBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        // To execute back press
         super.onBackPressed();

        // To do something else
        Toast.makeText(getApplicationContext(), "Back Button Pressed", Toast.LENGTH_SHORT).show();
        // do something on back.
//        startActivity(new Intent(PhotoActivity.this, MainActivity.class));
//        finish();
    }



}
