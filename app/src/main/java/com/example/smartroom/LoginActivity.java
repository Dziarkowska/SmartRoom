package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class LoginActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.login_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainScreenActivity();
            }
        });

    }

    public void onClick(View view){
        EditText loginEt = (EditText) findViewById(R.id.login);
        String login = loginEt.getEditableText().toString();
        String url = "http://ec2-35-156-20-198.eu-central-1.compute.amazonaws.com:6969/app-connection-core/loginuser/ff";

        EditText passwordEt = (EditText) findViewById(R.id.password);
        String password = loginEt.getEditableText().toString();

        String jsonString = "{" + "login: " + login + ", " + "password: " + password + "}";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Invocation.Builder request = target.request();

        Response response = request.post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));

    }

    public void openMainScreenActivity(){
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
    }
}

