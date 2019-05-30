package com.example.smartroom;

import static java.lang.Math.toIntExact;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class RoomKeyActivity extends AppCompatActivity {

    private AutoCompleteTextView roomID_list;   // what is this?
    private final String[] rooms ={"123"};  //TODO: make sure to not exceed
    private final String SERVER_URL = "http://ec2-35-156-20-198.eu-central-1.compute.amazonaws.com:6969/app-connection-core/loginrooom";
    private final Integer SUCCESSFUL_LOGIN = 200;   //TODO: move repetitive constants to other file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_key);

        Spinner room_id_spinner = findViewById(R.id.roomID_spinner);

        Button go_btn = findViewById(R.id.go_btn);
        go_btn.getBackground().setAlpha(128);

        go_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                onRoomLoginAttempt();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            RoomKeyActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.RoomIDs));
        adapter.setDropDownViewResource(R.layout.spinner_style);
        room_id_spinner.setAdapter(adapter);
    }

    public void openGuestScreenActivity()
    {
        Intent intent = new Intent(this, GuestScreenActivity.class);
        startActivity(intent);
    }

    private void onRoomLoginAttempt()
    {
        EditText passwordEt = findViewById(R.id.room_pswd);
        String password = passwordEt.getEditableText().toString();

        Spinner roomSpinner = findViewById(R.id.roomID_spinner);
        Integer roomIndicator = toIntExact(roomSpinner.getSelectedItemId());

        String jsonString = "{" + "\"idRoom\": \"" + rooms[roomIndicator] +
            "\", \"password\": \"" + password + "\"}";

        Log.w("json ", jsonString);
        WebTarget target = ClientBuilder.newClient().target(SERVER_URL);

        try
        {
            if(validateResponse(target, jsonString))
            {
                openGuestScreenActivity();
            }
        }
        catch (Exception exception)
        {
            Log.w("O kurwa wyjątek xD: ", exception);
        }
    }

    private boolean validateResponse(WebTarget webTarget, String jsonString)
        throws InterruptedException, ExecutionException
    {
        Future<Response> response = webTarget.request().async().post(Entity.json(jsonString));
        Object status = response.get().getStatus();
        response.get().close();
        Log.d("Room login status ", status.toString());
        return status.equals(SUCCESSFUL_LOGIN);
    }
}
