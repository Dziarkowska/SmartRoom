package com.example.smartroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartroom.mqttconnector.MqttConnector;

public class SettingsActivity extends AppCompatActivity {

    private Button save_changes_btn;
    private SeekBar seekbar;
    private TextView progress2_txt;
    private Switch ac_switch;
    private Switch window_switch;
    private EditText temperature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DataBlock data = (DataBlock) getIntent().getSerializableExtra("data");
        save_changes_btn = findViewById(R.id.save_changes_btn);
        seekbar = findViewById(R.id.seek_bar2_brightness);
        progress2_txt = findViewById(R.id.progress2_txt);
        ac_switch = findViewById(R.id.AC_switch);
        window_switch = findViewById(R.id.windows_switch);
        temperature = findViewById(R.id.set_temp_txt);
        save_changes_btn.getBackground().setAlpha(128);
        setDefaultSettingsValues(data);

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

        save_changes_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBlock data = (DataBlock) getIntent().getSerializableExtra("data");
                boolean window = window_switch.isChecked();
                boolean ac = ac_switch.isChecked();
                double temp =  Double.valueOf(temperature.getText().toString());
                if(validateInsertedData(temp, window, ac))
                {
                    MqttConnector connector = MqttConnector.getInstance(getApplicationContext());
                    connector.sendLightSettings(data.getId(), Double.toString(seekbar.getProgress() / 100.0));
                    connector.sendTemperatureSettings(data.getId(), ac, window, temp);
                    finish();
                }
            }
        });

    }

    private void setDefaultSettingsValues(DataBlock data){
        EditText set_temp_txt = findViewById(R.id.set_temp_txt);
        Switch AC_switch = findViewById(R.id.AC_switch);
        Switch windows_switch = findViewById(R.id.windows_switch);

        seekbar.setProgress(lightInProgress(data));
        progress2_txt.setText("Brightness \n" + lightInProgress(data) + "%");

        set_temp_txt.setText(data.getTempIn());
        AC_switch.setChecked(isACon(data));
        windows_switch.setChecked(isWindowOpen(data));
    }

    private boolean isACon(DataBlock data){

        return data.getClimeOn().equals("ON");
    }

    private boolean isWindowOpen(DataBlock data){


        return data.getWindowOpen().equals("1");
    }

    private int lightInProgress(DataBlock data){
        Double progress__double_val = Double.valueOf(data.getLightIn())*100;
        return progress__double_val.intValue();

    }

    private boolean validateInsertedData(double temp, boolean window, boolean ac)
    {
        if(temp > 50)
        {
            Toast.makeText(getBaseContext(),"Too hot!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(temp < 0)
        {
            Toast.makeText(getBaseContext(),"Too cold!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(window && ac)
        {
            Toast.makeText(getBaseContext(),"Don't open the window and enable AC!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
