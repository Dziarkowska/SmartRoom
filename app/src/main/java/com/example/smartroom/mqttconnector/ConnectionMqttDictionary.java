package com.example.smartroom.mqttconnector;

class ConnectionMqttDictionary {

    static final String BROKER = "tcp://ec2-35-156-20-198.eu-central-1.compute.amazonaws.com:6979";
    static final String CLIENT_ID = "AndroidServerClient";
    static final String IN_CONN_TOPIC = "inConn/updateinfo";
    static final String PREPATE_DATA_TOPIC = "menagement/preparedata";
    static final String MQTT_USER = "usereitMqtt";
    static final String MQTT_PASSWORD = "usereitPass";
}
