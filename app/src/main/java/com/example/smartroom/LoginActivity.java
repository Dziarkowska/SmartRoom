package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private String SERVER_URL = "http://ec2-35-156-20-198.eu-central-1.compute.amazonaws.com:6969/app-connection-core/loginuser";
    private Integer SUCCESSFUL_LOGIN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.login_btn);
        button.getBackground().setAlpha(128);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginEt = findViewById(R.id.login);
                String login = loginEt.getEditableText().toString();
                Log.w("Login ", login);

                EditText passwordEt = findViewById(R.id.password);
                String password = passwordEt.getEditableText().toString();
                Log.w("Password ", password);

                String jsonString = "{" + "user: " + login + ", " + "password: " + password + "}";
                WebTarget target = ClientBuilder.newClient().target(SERVER_URL);

                try
                {
                    if(validateResponse(target, jsonString))
                        openMainScreenActivity();
                }
                catch (Exception exception)
                {
                    Log.w("O kurwa, wyjątek xD: ", exception);
                }
            }
        });
        button.getBackground().setAlpha(128);

    }

    public void onClick(View view){

    }

    public void openMainScreenActivity(){
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
    }

    private boolean validateResponse(WebTarget webTarget, String jsonString) throws InterruptedException, ExecutionException
    {
        Future<Response> response = webTarget.request().async().post(Entity.json(jsonString));
        Object status = response.get().getStatus();
        response.get().close();
        Log.w("Response ", status.toString());
        if(status.equals(SUCCESSFUL_LOGIN))
        {
            return true;
        }
        return false;
    }
}

