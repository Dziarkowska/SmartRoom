package com.example.smartroom.mqttconnector;

class ConnectionMqttDictionary {

    static final String BROKER = "tcp://ec2-35-156-20-198.eu-central-1.compute.amazonaws.com:6979";

    static final String CLIENT_ID = "AndroidServerClient";

    static final String VOTING_TOPIC = "app/voting";
    static final String ROOM_BASE_TOPIC = "app/updateinfo/";

    static final String LIGHT_MANAGEMENT_TOPIC = "management/lightmanage";
    static final String TEMPERATURE_MANAGEMENT_TOPIC = "manage/temp";
    static final String VOTING_MANAGEMENT_TOPIC = "manage/voting";

    static final String MQTT_USER = "usereitMqtt";
    static final String MQTT_PASSWORD = "usereitPass";
}
