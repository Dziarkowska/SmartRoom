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

import java.util.Random;

public class MqttConnector implements MqttCallbackExtended {
    private static MqttConnector ourInstance = null;
    private final MqttAndroidClient mqttAndroidClient;
    private static final String LOG_TAG = "MqttConnector";
    private static final int QoS = 1;
    private BodyReader bodyReader = new BodyReader();
    private MqttConnectorEventsListener listener;

    public interface MqttConnectorEventsListener {

        void onConnectionError();
        void onSubscribingError();
        void onSendingError();

        void onConnected();
        void onSubscribed();
        void onDeliveryCompleted();

        void onMessageReceived(DataBlock dataBlock);
    }

    public static MqttConnector getInstance(Context ctx, MqttConnectorEventsListener listener) {

        if (ourInstance == null)
            ourInstance = new MqttConnector(ctx, listener);

        return ourInstance;
    }

    private MqttConnector(Context ctx, MqttConnectorEventsListener listener) {
        this.listener = listener;

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
            this.listener.onConnectionError();
            Log.e(LOG_TAG, "Cannot connect: " + e.getMessage());
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {

        listener.onConnected();
        Log.i(LOG_TAG, "Mqtt connected successfully");
        try {
            mqttAndroidClient.subscribe(ConnectionMqttDictionary.IN_CONN_TOPIC, QoS);
            listener.onSubscribed();
            Log.i(LOG_TAG, "Subscribe successfully");
        }catch (MqttException e){
            listener.onSubscribingError();
            Log.e(LOG_TAG, "Cannot subscribe: " + e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

        Log.i(LOG_TAG, "Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Log.i(LOG_TAG, "Message arrived");
        try {
            DataBlock dataBlock = bodyReader.convertJsonStringToDataBlock(message.toString());

            listener.onMessageReceived(dataBlock);
        }catch (Throwable exception){
            Log.e(LOG_TAG, "Error while receiving message: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        listener.onDeliveryCompleted();
        Log.i(LOG_TAG, "Delivery completed");
    }

    public void prepareData() {

        // TODO uzupelnic odpowiednio payload
        sendMessage(ConnectionMqttDictionary.PREPATE_DATA_TOPIC, "");
    }

    private void sendMessage(String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(QoS);
        try {
            mqttAndroidClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            listener.onSendingError();
            Log.e(LOG_TAG, "Error while sending message: " + e.getMessage());
        }
    }
}
