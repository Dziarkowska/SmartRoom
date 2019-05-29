package com.example.smartroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private Button set_temp_btn;
    private SeekBar seekbar;
    private TextView progress2_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        set_temp_btn = findViewById(R.id.set_temp_btn);
        seekbar = findViewById(R.id.seek_bar2_brightness);
        progress2_txt = findViewById(R.id.progress2_txt);
        set_temp_btn.getBackground().setAlpha(128);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress2_txt.setText("Brightness \n" + progress + "%");
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
