package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.smartroom.mqttconnector.MqttConnector;


public class GuestScreenActivity extends AppCompatActivity {

    private Button vote_btn, temp_in_btn, temp_out_btn, AC_btn, smog_btn, people_num_btn, weather_btn;
    static private boolean isVoteOn = true;


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
        vote_btn.setEnabled(isVoteOn);

        DataBlock data = (DataBlock) getIntent().getSerializableExtra("data");
        fillMeasurementsValues(data);

        MqttConnector connector = MqttConnector.getInstance(getApplicationContext());
        MqttConnector.MqttConnectorEventsListener listener = new MqttConnector.MqttConnectorEventsListener() {
            @Override
            public void onConnectionError()
            {}

            @Override
            public void onSubscribingError()
            {}

            @Override
            public void onSendingError()
            {}

            @Override
            public void onConnected()
            {}

            @Override
            public void onSubscribed()
            {}

            @Override
            public void onDeliveryCompleted()
            {}

            @Override
            public void onMessageReceived(DataBlock dataBlock)
            {
                fillMeasurementsValues(dataBlock);
            }

            @Override
            public void onVotingStarts()
            {
                isVoteOn = true;
                vote_btn.setEnabled(isVoteOn);
            }

            @Override
            public void onVotingEnds()
            {
                isVoteOn = false;
                vote_btn.setEnabled(isVoteOn);
            }
        };
        connector.setEventsListener(listener);
        connector.subscribeToRoom(data.getId());

        vote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVoteActivity();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        MqttConnector connector = MqttConnector.getInstance(getApplicationContext());
        try
        {
            connector.unsubscribeRoom();
        }
        catch(Exception e)
        {
            Log.e("GuestScreenActivity", e.toString());
        }

    }

    public void openVoteActivity(){
        Intent intent = new Intent(this,VoteActivity.class);
        startActivity(intent);
    }

    private void fillMeasurementsValues(DataBlock data)
    {
        people_num_btn.setText("PEOPLE INSIDE:\n" + data.getPeopleInside());
        temp_in_btn.setText("TEMPERATURE INSIDE:\n" + String.format("%.2f",Double.valueOf(data.getTempIn())) + " °C");
        temp_out_btn.setText("TEMPERATURE OUTSIDE:\n" + String.format("%.2f",Double.valueOf(data.getTempOut())) + " °C");
        AC_btn.setText("AC LEVEL:\n" + data.getClimeOn());
        weather_btn.setText("POLLUTION LVL INSIDE:\n" + String.format("%.2f",Double.valueOf(data.getAirQuaIn()) * 100) + " %");
        smog_btn.setText("POLLUTION LVL OUTSIDE:\n" + String.format("%.2f",Double.valueOf(data.getAirQuaOut()) * 100) + " %");
    }
}
