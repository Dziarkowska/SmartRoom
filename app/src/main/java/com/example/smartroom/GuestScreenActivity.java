package com.example.smartroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class GuestScreenActivity extends AppCompatActivity {

    private Button vote_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_screen);
        vote_btn = findViewById(R.id.vote_btn);
        vote_btn.getBackground().setAlpha(128);
    }
}
