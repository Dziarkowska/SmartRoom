package com.example.smartroom;

public final class DataConstants
{
    public static final String[] ROOMS ={"123"};
    public static final String SERVER_URL = "http://ec2-35-156-20-198.eu-central-1.compute.amazonaws.com";
    public static final String SERVER_PORT = "6969";
    public static final String ROOM_LOGIN_ENDPOINT = SERVER_URL + ":" + SERVER_PORT + "/app-connection-core/loginroom";
    public static final String ADMIN_LOGIN_ENDPOINT = SERVER_URL + ":" + SERVER_PORT + "/app-connection-core/loginuser";
    public static final String ADMIN_ROOM_PASSWORD_ENDPOINT = SERVER_URL + ":" + SERVER_PORT + "/app-connection-core/changepass";
    public static final String ROOM_LIST_ENDPOINT = SERVER_URL + ":" + SERVER_PORT + "/app-connection-core/getroomlist";
    public static final Integer SUCCESSFUL_LOGIN = 200;
    public static final String COOLER = "down";
    public static final String WARMER = "up";
    public static final String NO_CHANGES= "ok";
}
