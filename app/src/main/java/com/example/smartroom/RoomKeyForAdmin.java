package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static java.lang.Math.toIntExact;

public class RoomKeyForAdmin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_key_for_admin);

        final ArrayList<String> roomIds = getRoomList();
        setRoomSpinner(roomIds.size());

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
            Response response = getResponse(target, jsonString);
            if(validateResponse(response))
            {
                openMainScreenActivity(convertJsonToDataBlock(response));
                response.close();
            }
        }
        catch (Exception exception)
        {
            Log.w("O kurwa wyjątek xD: ", exception);
        }
    }

    public void openMainScreenActivity(DataBlock data){

        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.putExtra("login", getIntent().getSerializableExtra("login"));
        intent.putExtra("data", data);
        startActivity(intent);
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

    private ArrayList<String> getRoomList()
    {
        ArrayList<String> rooms;
        try
        {
            WebTarget target = ClientBuilder.newClient().target(DataConstants.ROOM_LIST_ENDPOINT);
            Response response = getResponse(target, "");
            String stringJson = response.readEntity(String.class);
            response.close();
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

    private void setRoomSpinner(Integer numOfRooms)
    {
        Spinner roomSpinner = findViewById(R.id.roomID_spinner);
        ArrayList<String> roomNames = new ArrayList<>();
        for(Integer iterator =0; iterator < numOfRooms; ++iterator)
        {
            roomNames.add("ROOM" + (iterator +1));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(dataAdapter);
    }
}
