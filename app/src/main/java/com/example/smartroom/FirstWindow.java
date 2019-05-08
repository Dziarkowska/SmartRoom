package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstWindow extends AppCompatActivity {

    private Button button_adm, button_guest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_window);
        button_adm = findViewById(R.id.admin_btn);
        button_guest = findViewById(R.id.guest_btn);
        button_adm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
        button_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomKeyActivity();
            }
        });

    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openRoomKeyActivity(){
        Intent intent = new Intent(this, RoomKeyActivity.class);
        startActivity(intent);
    }
}
