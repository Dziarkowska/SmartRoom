package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;


public class GuestScreenActivity extends AppCompatActivity {

    private Button vote_btn, temp_in_btn, temp_out_btn, AC_btn, smog_btn, people_num_btn, weather_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_screen);
        vote_btn = findViewById(R.id.vote_btn);
        temp_in_btn = findViewById(R.id.temp_in_btn);
        temp_out_btn = findViewById(R.id.temp_out_btn);
        AC_btn = findViewById(R.id.AC_btn);
        smog_btn = findViewById(R.id.smog_btn);
        people_num_btn = findViewById(R.id.people_num_btn);
        weather_btn = findViewById(R.id.weather_btn);
        vote_btn.getBackground().setAlpha(128);
        temp_in_btn.getBackground().setAlpha(20);
        temp_out_btn.getBackground().setAlpha(20);
        AC_btn.getBackground().setAlpha(20);
        smog_btn.getBackground().setAlpha(20);
        people_num_btn.getBackground().setAlpha(20);
        weather_btn.getBackground().setAlpha(20);

        fillInitialValues();

        vote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVoteActivity();
            }
        });
    }

    public void openVoteActivity(){
        Intent intent = new Intent(this,VoteActivity.class);
        startActivity(intent);
    }

    private void fillInitialValues()
    {
        DataBlock data = (DataBlock) getIntent().getSerializableExtra("data");

        people_num_btn.setText("PEOPLE INSIDE:\n" + data.getPeopleInside());
        temp_in_btn.setText("TEMPERATURE INSIDE:\n" + data.getTempIn());
        temp_out_btn.setText("TEMPERATURE OUTSIDE:\n" + data.getTempOut());
        AC_btn.setText("AC LEVEL:\n" + data.getClimeOn());  //pieknie
    }
}
