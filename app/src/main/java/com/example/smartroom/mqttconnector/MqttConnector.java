package com.example.smartroom.mqttconnector;

import android.content.Context;
import android.util.Log;

import com.example.smartroom.DataBlock;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MqttConnector implements MqttCallbackExtended {
    private static MqttConnector ourInstance = null;
    private final MqttAndroidClient mqttAndroidClient;
    private static final String LOG_TAG = "MqttConnector";
    private static final int QoS = 2;
    private BodyReader bodyReader = new BodyReader();
    private MqttConnectorEventsListener listener;
    private String currentlySubscribedRoom = "";

    public interface MqttConnectorEventsListener {

        void onConnectionError();
        void onSubscribingError();
        void onSendingError();

        void onConnected();
        void onSubscribed();
        void onDeliveryCompleted();

        void onMessageReceived(DataBlock dataBlock);

        void onVotingStarts();
        void onVotingEnds();
    }

    public static MqttConnector getInstance(Context ctx) {

        if (ourInstance == null)
            ourInstance = new MqttConnector(ctx);

        return ourInstance;
    }

    private MqttConnector(Context ctx) {

        mqttAndroidClient = new MqttAndroidClient(ctx, ConnectionMqttDictionary.BROKER,
                ConnectionMqttDictionary.CLIENT_ID + new Random().nextInt());
        mqttAndroidClient.setCallback(this);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(ConnectionMqttDictionary.MQTT_USER);
        options.setPassword(ConnectionMqttDictionary.MQTT_PASSWORD.toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        try {
            mqttAndroidClient.connect(options);
            Log.i(LOG_TAG, "Start connecting");
        } catch (MqttException e) {
            if (listener != null) this.listener.onConnectionError();
            Log.e(LOG_TAG, "Cannot connect: " + e.getMessage());
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {

        if (listener != null) listener.onConnected();
        Log.i(LOG_TAG, "Mqtt connected successfully");
        try {
            mqttAndroidClient.subscribe(ConnectionMqttDictionary.VOTING_TOPIC, QoS);
            if (!currentlySubscribedRoom.equals(""))
                subscribeToRoom(currentlySubscribedRoom);
            Log.i(LOG_TAG, "Subscribe successfully to  " + currentlySubscribedRoom);
        }catch (MqttException e){
            if (listener != null) listener.onSubscribingError();
            Log.e(LOG_TAG, "Cannot subscribe: " + e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

        Log.i(LOG_TAG, "Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Log.i(LOG_TAG, "Message arrived to " + ConnectionMqttDictionary.ROOM_BASE_TOPIC + currentlySubscribedRoom );

        String msg = message.toString();
        try {

            if (topic.equals(ConnectionMqttDictionary.VOTING_TOPIC)) {

                if (msg.equals("START")) {
                    if (listener != null) listener.onVotingStarts();
                }
                else if (msg.equals("STOP")) {
                    if (listener != null) listener.onVotingEnds();
                }
            } else if (topic.equals(ConnectionMqttDictionary.ROOM_BASE_TOPIC + currentlySubscribedRoom)) {

                DataBlock dataBlock = bodyReader.convertJsonStringToDataBlock(msg);
                if (listener != null) listener.onMessageReceived(dataBlock);
            }
        }catch (Throwable exception){
            Log.e(LOG_TAG, "Error while receiving message: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        if (listener != null) listener.onDeliveryCompleted();
        Log.i(LOG_TAG, "Delivery completed");
    }

    public void setEventsListener(MqttConnectorEventsListener listener) {

        this.listener = listener;
    }

    public void subscribeToRoom(String roomId) {

        try {
            mqttAndroidClient.subscribe(ConnectionMqttDictionary.ROOM_BASE_TOPIC + roomId, QoS);
            currentlySubscribedRoom = roomId;
            if (listener != null) listener.onSubscribed();
            Log.i(LOG_TAG, "Subscribe successfully to " + currentlySubscribedRoom);
        }catch (MqttException e){
            if (listener != null) listener.onSubscribingError();
            Log.e(LOG_TAG, "Cannot subscribe: " + e.getMessage());
        }
    }

    public void unsubscribeRoom() throws MqttException {

        mqttAndroidClient.unsubscribe(ConnectionMqttDictionary.ROOM_BASE_TOPIC + currentlySubscribedRoom);
        currentlySubscribedRoom = "";
    }

    public void sendLightSettings(String lightLevel) {

        sendLightSettings(currentlySubscribedRoom, lightLevel);
    }

    public void sendLightSettings(String roomId, String lightLevel) {

        try {
            JSONObject json = new JSONObject();
            json.put("id", roomId);
            json.put("light_in", lightLevel);
            sendMessage(ConnectionMqttDictionary.LIGHT_MANAGEMENT_TOPIC, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendTemperatureSettings(boolean isClimeOn, boolean isWindowOpen, double temp) {

        sendTemperatureSettings(currentlySubscribedRoom, isClimeOn, isWindowOpen, temp);
    }

    public void sendTemperatureSettings(String roomId, boolean isClimeOn, boolean isWindowOpen, double temp) {

        try {
            JSONObject json = new JSONObject();
            json.put("id", roomId);
            json.put("isClimeOn", Boolean.toString(isClimeOn));
            json.put("isWindowOpen", Boolean.toString(isWindowOpen));
            json.put("temp", temp);
            sendMessage(ConnectionMqttDictionary.TEMPERATURE_MANAGEMENT_TOPIC, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendVote(String answer) {

        sendVote(currentlySubscribedRoom, answer);
    }

    public void sendVote(String roomId, String answer) {

        try {
            JSONObject json = new JSONObject();
            json.put("id", roomId);
            json.put("value", answer);
            sendMessage(ConnectionMqttDictionary.VOTING_MANAGEMENT_TOPIC, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(QoS);
        try {
            mqttAndroidClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            if (listener != null) listener.onSendingError();
            Log.e(LOG_TAG, "Error while sending message: " + e.getMessage());
        }
    }
}
