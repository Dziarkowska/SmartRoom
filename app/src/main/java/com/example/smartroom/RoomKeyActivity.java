package com.example.smartroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static java.lang.Math.toIntExact;

public class RoomKeyActivity extends AppCompatActivity {

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

    public void openGuestScreenActivity(DataBlock data)
    {
        Intent intent = new Intent(this, GuestScreenActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    private void onRoomLoginAttempt()
    {
        getRoomList();
        EditText passwordEt = findViewById(R.id.room_pswd);
        String password = passwordEt.getEditableText().toString();

        Spinner roomSpinner = findViewById(R.id.roomID_spinner);
        Integer roomIndicator = toIntExact(roomSpinner.getSelectedItemId());

        String jsonString = "{" + "\"idRoom\": \"" + DataConstants.ROOMS[roomIndicator] +
            "\", \"password\": \"" + password + "\"}";

        Log.w("json ", jsonString);
        WebTarget target = ClientBuilder.newClient().target(DataConstants.ROOM_LOGIN_ENDPOINT);

        try
        {
            Response response = getResponse(target, jsonString);
            if(validateResponse(response))
            {
                openGuestScreenActivity(convertJsonToDataBlock(response));
                response.close();
            }
            else{

                incorrectLoginMessage();

            }
        }
        catch (Exception exception)
        {
            Log.w("O kurwa wyjątek xD: ", exception);
        }
    }

    private boolean validateResponse(Response response)
    {
        Integer status = response.getStatus();
        Log.d("Room login status ", status.toString());
        return status.equals(DataConstants.SUCCESSFUL_LOGIN);
    }

    private Response getResponse(WebTarget webTarget, String jsonString)
        throws InterruptedException, ExecutionException
    {
        Future<Response> response = webTarget.request().async().post(Entity.json(jsonString));
        return response.get();
    }

    private ArrayList<String> getRoomList()
    {
        ArrayList<String> rooms;
        try
        {
            WebTarget target = ClientBuilder.newClient().target(DataConstants.ROOM_LIST_ENDPOINT);
            Response response = getResponse(target, "");
            String stringJson = response.readEntity(String.class);
            JsonElement elem = new JsonParser().parse(stringJson);
            Gson gson  = new GsonBuilder().create();
            rooms = gson.fromJson(elem, ArrayList.class);
            return rooms;
        }
        catch (Exception exception)
        {
            Log.w("O kurwa wyjątek xD: ", exception);
        }
        return null;
    }

    private DataBlock convertJsonToDataBlock(Response response)
    {
        DataBlock dataBlock;
        try
        {
            String stringJson = response.readEntity(String.class);
            Log.d("Received json ", stringJson);
            JsonElement elem = new JsonParser().parse(stringJson);
            Gson gson  = new GsonBuilder().create();
            dataBlock = gson.fromJson(elem, DataBlock.class);
        }
        catch (JsonIOException | JsonSyntaxException exception)
        {
            Log.e("Exception ", exception.toString());
            return new DataBlock();
        }
        return dataBlock;
    }

    private void incorrectLoginMessage(){

        Toast.makeText(getBaseContext(),"Incorrect login or password, try again!", Toast.LENGTH_LONG).show();
        EditText room_password = findViewById(R.id.room_pswd);
        room_password.setText("");

    }
}
