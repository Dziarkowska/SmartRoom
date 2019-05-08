package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class RoomKeyActivity extends AppCompatActivity {

    private Button go_btn;
    private AutoCompleteTextView roomID_list;
    private String[] rooms ={"room1"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_key);
        go_btn = findViewById(R.id.go_btn);
        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGuestScreenActivity();
            }
        });

        roomID_list = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,rooms);
        roomID_list.setAdapter(adapter);
        roomID_list.setThreshold(1);
    }

    public void openGuestScreenActivity(){
        Intent intent = new Intent(this, GuestScreenActivity.class);
        startActivity(intent);
    }
}
