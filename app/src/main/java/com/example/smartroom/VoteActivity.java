package com.example.smartroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import static android.view.Window.FEATURE_NO_TITLE;

public class VoteActivity extends AppCompatActivity {

    private Button cooler_btn, warmer_btn, nochanges_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vote);

        cooler_btn = findViewById(R.id.cooler_btn);
        warmer_btn = findViewById(R.id.warmer_btn);
        nochanges_btn = findViewById(R.id.nochanges_btn);

        cooler_btn.getBackground().setAlpha(128);
        warmer_btn.getBackground().setAlpha(128);
        nochanges_btn.getBackground().setAlpha(128);

    }
}
