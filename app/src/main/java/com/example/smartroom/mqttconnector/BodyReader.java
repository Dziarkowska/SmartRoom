package com.example.smartroom.mqttconnector;

import android.util.Log;

import com.example.smartroom.DataBlock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.io.InputStreamReader;

public class BodyReader {

    private static final String PROBLEM_WITH_CONVERT_INPUT_DATA = "Problem with convert data";
    private static final String LOG_TAG = "BodyReader";

    public DataBlock convertJsonToDataBlock(InputStream request) {
        DataBlock dataBlock;
        try{
            JsonElement elem = new JsonParser().parse(new InputStreamReader(request));
            Gson gson  = new GsonBuilder().create();
            dataBlock = gson.fromJson(elem, DataBlock.class);
        } catch (JsonIOException | JsonSyntaxException e){
            Log.e(LOG_TAG, PROBLEM_WITH_CONVERT_INPUT_DATA + " " + e.toString());
            throw new ConvertErrorException(PROBLEM_WITH_CONVERT_INPUT_DATA);
        }

        return dataBlock;
    }

    DataBlock convertJsonStringToDataBlock(String request) {
        DataBlock dataBlock;
        try{
            GsonBuilder gson  = new GsonBuilder();
            gson.serializeNulls();
            Gson gson1 = gson.create();
            dataBlock = gson1.fromJson(request, DataBlock.class);
        } catch (JsonIOException | JsonSyntaxException e){
            Log.e(LOG_TAG, PROBLEM_WITH_CONVERT_INPUT_DATA + " " + e.toString());
            throw new ConvertErrorException(PROBLEM_WITH_CONVERT_INPUT_DATA);
        }
        return dataBlock;
    }
}
