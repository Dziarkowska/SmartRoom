package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;

public class MainScreenActivity extends AppCompatActivity {


    private Button vote_btn, temp_in_btn, temp_out_btn, AC_btn, smog_btn, people_num_btn, weather_btn;
    private TableRow tablerow_btn;
    private SeekBar seekbar;
    private TextView progress_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        vote_btn = findViewById(R.id.vote_btn);
        temp_in_btn = findViewById(R.id.temp_in_btn);
        temp_out_btn = findViewById(R.id.temp_out_btn);
        AC_btn = findViewById(R.id.AC_btn);
        smog_btn = findViewById(R.id.smog_btn);
        people_num_btn = findViewById(R.id.people_num_btn);
        weather_btn = findViewById(R.id.weather_btn);
        tablerow_btn = findViewById(R.id.table_row4);
        seekbar = findViewById(R.id.seek_bar_brightness);
        progress_txt = findViewById(R.id.progress_txt);
        vote_btn.getBackground().setAlpha(128);
        temp_in_btn.getBackground().setAlpha(20);
        temp_out_btn.getBackground().setAlpha(20);
        AC_btn.getBackground().setAlpha(20);
        smog_btn.getBackground().setAlpha(20);
        people_num_btn.getBackground().setAlpha(20);
        weather_btn.getBackground().setAlpha(20);
        tablerow_btn.getBackground().setAlpha(20);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_txt.setText("Brightness \n" + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
