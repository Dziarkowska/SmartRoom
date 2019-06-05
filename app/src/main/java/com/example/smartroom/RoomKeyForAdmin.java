package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RoomKeyForAdmin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_key_for_admin);

        Spinner room_id_spinner = findViewById(R.id.roomID_spinner);

        Button go_btn = findViewById(R.id.go_btn);
        go_btn.getBackground().setAlpha(128);

        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdminChooseRoomAttempt();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                RoomKeyForAdmin.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.RoomIDs));
        adapter.setDropDownViewResource(R.layout.spinner_style);
        room_id_spinner.setAdapter(adapter);
    }

    private void onAdminChooseRoomAttempt(){

        openMainScreenActivity(); //TODO: tu by trzeba tak jak jest dla guesta z tym spinnerem i wybraniem pokoju, tylko bez hasla
    }

    public void openMainScreenActivity(){

        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.putExtra("login", getIntent().getSerializableExtra("login"));
        startActivity(intent);
    }
}
