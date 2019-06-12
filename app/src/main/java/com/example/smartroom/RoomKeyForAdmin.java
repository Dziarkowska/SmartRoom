package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static java.lang.Math.toIntExact;

public class RoomKeyForAdmin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_key_for_admin);

        Spinner roomSpinner = findViewById(R.id.roomID_spinner);
        final ArrayList<String> roomIds = RoomKeyHelper.getRoomList();
        RoomKeyHelper.setRoomSpinner(this, roomSpinner, roomIds.size());

        Button go_btn = findViewById(R.id.go_btn);
        go_btn.getBackground().setAlpha(128);

        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdminChooseRoomAttempt(roomIds);
            }
        });
    }

    private void onAdminChooseRoomAttempt(ArrayList<String> rooms)
    {
        EditText passwordEt = findViewById(R.id.new_room_password_txt);
        String password = passwordEt.getEditableText().toString();

        Spinner roomSpinner = findViewById(R.id.roomID_spinner);
        Integer roomIndicator = toIntExact(roomSpinner.getSelectedItemId());

        String jsonString = "{" + "\"idRoom\": \"" + rooms.get(roomIndicator) +
            "\", \"password\": \"" + password + "\"}";

        Log.w("json ", jsonString);
        WebTarget target = ClientBuilder.newClient().target(DataConstants.ADMIN_ROOM_PASSWORD_ENDPOINT);

        try
        {
            Response response = RoomKeyHelper.getResponse(target, jsonString);
            if(RoomKeyHelper.validateResponse(response))
            {
                openMainScreenActivity(RoomKeyHelper.convertJsonToDataBlock(response));
                response.close();
            }
        }
        catch (Exception exception)
        {
            Log.w("RoomKeyForAdmin ", exception);
        }
    }

    public void openMainScreenActivity(DataBlock data){

        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.putExtra("login", getIntent().getSerializableExtra("login"));
        intent.putExtra("data", data);
        startActivity(intent);
    }


}
