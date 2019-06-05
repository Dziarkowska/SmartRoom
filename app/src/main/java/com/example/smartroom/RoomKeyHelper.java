package com.example.smartroom;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class RoomKeyHelper
{
    public static void setRoomSpinner(AppCompatActivity activity, Spinner roomSpinner, Integer numOfRooms)
    {
        ArrayList<String> roomNames = new ArrayList<>();
        for(Integer iterator =0; iterator < numOfRooms; ++iterator)
        {
            roomNames.add("ROOM" + (iterator +1));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, roomNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(dataAdapter);
    }

    public static ArrayList<String> getRoomList()
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
            Log.w("Exception ", exception);
        }
        return new ArrayList<>();
    }

    public static Response getResponse(WebTarget webTarget, String jsonString)
        throws InterruptedException, ExecutionException
    {
        Future<Response> response = webTarget.request().async().post(Entity.json(jsonString));
        return response.get();
    }

    public static boolean validateResponse(Response response)
    {
        Integer status = response.getStatus();
        Log.d("Room login status ", status.toString());
        return status.equals(DataConstants.SUCCESSFUL_LOGIN);
    }
}
