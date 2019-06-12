package com.example.smartroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static java.lang.Math.toIntExact;

public class RoomKeyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_key);

        Spinner roomSpinner = findViewById(R.id.roomID_spinner);
        final ArrayList<String> roomIds = RoomKeyHelper.getRoomList();
        RoomKeyHelper.setRoomSpinner(this, roomSpinner, roomIds.size());

        Button go_btn = findViewById(R.id.go_btn);
        go_btn.getBackground().setAlpha(128);

        go_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                onRoomLoginAttempt(roomIds);
            }
        });
    }

    public void openGuestScreenActivity(DataBlock data)
    {
        Intent intent = new Intent(this, GuestScreenActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    private void onRoomLoginAttempt(ArrayList<String> rooms)
    {
        EditText passwordEt = findViewById(R.id.room_pswd);
        String password = passwordEt.getEditableText().toString();

        Spinner roomSpinner = findViewById(R.id.roomID_spinner);
        Integer roomIndicator = toIntExact(roomSpinner.getSelectedItemId());

        String jsonString = "{" + "\"idRoom\": \"" + rooms.get(roomIndicator) +
            "\", \"password\": \"" + password + "\"}";

        Log.w("json ", jsonString);
        WebTarget target = ClientBuilder.newClient().target(DataConstants.ROOM_LOGIN_ENDPOINT);

        try
        {
            Response response = RoomKeyHelper.getResponse(target, jsonString);
            if(RoomKeyHelper.validateResponse(response))
            {
                openGuestScreenActivity(RoomKeyHelper.convertJsonToDataBlock(response));
                response.close();
            }
            else{

                incorrectLoginMessage();

            }
        }
        catch (Exception exception)
        {
            Log.w("Exception ", exception);
        }
    }

    private void incorrectLoginMessage(){

        Toast.makeText(getBaseContext(),"Incorrect password, try again!", Toast.LENGTH_LONG).show();
        EditText room_password = findViewById(R.id.room_pswd);
        room_password.setText("");

    }

}
