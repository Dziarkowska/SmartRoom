package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

public class RoomKeyActivity extends AppCompatActivity {

    private Button go_btn;
    private AutoCompleteTextView roomID_list;
    //private String[] rooms ={"room1"};
    private Spinner room_id_spinner;

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

        room_id_spinner = findViewById(R.id.roomID_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RoomKeyActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.RoomIDs));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room_id_spinner.setAdapter(adapter);



    }

    public void openGuestScreenActivity(){
        Intent intent = new Intent(this, GuestScreenActivity.class);
        startActivity(intent);
    }
}
