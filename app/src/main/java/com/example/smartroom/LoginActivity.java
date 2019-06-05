package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class LoginActivity extends AppCompatActivity {

    private final String SERVER_URL = "http://ec2-35-156-20-198.eu-central-1.compute.amazonaws.com:6969/app-connection-core/loginuser";
    private final Integer SUCCESSFUL_LOGIN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = findViewById(R.id.login_btn);
        button.getBackground().setAlpha(128);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onAdminLoginAttempt();
            }
        });
        button.getBackground().setAlpha(128);
    }



    public void openRoomKeyForAdminActivity(){

        Intent intent = new Intent( this, RoomKeyForAdmin.class);
        startActivity(intent);
    }

    private void onAdminLoginAttempt()
    {
        EditText loginEt = findViewById(R.id.login);
        String login = loginEt.getEditableText().toString();
        Log.i("Login ", login);

        EditText passwordEt = findViewById(R.id.password);
        String password = passwordEt.getEditableText().toString();

        String jsonString = "{" + "\"user\": \"" + login + "\", \"password\": \"" + password + "\"}";
        WebTarget target = ClientBuilder.newClient().target(SERVER_URL);

        try
        {
            if(validateResponse(target, jsonString))
            {
                openRoomKeyForAdminActivity();
            }
            else{
                incorrectLoginMessage();
            }
        }
        catch (Exception exception)
        {
            Log.w("O kurwa wyjątek xD: ", exception);
        }
    }

    private boolean validateResponse(WebTarget webTarget, String jsonString)
        throws InterruptedException, ExecutionException
    {
        Future<Response> response = webTarget.request().async().post(Entity.json(jsonString));
        Integer status = response.get().getStatus();
        Log.w("########################################", response.get().toString());
        response.get().close();
        Log.d("Admin login status ", status.toString());
        return status.equals(SUCCESSFUL_LOGIN);
    }

    private void incorrectLoginMessage(){

        Toast.makeText(getBaseContext(),"Incorrect login or password, try again!", Toast.LENGTH_LONG).show();
        EditText login_txt = findViewById(R.id.login);
        EditText password_txt = findViewById(R.id.password);

        login_txt.setText("");
        password_txt.setText("");
    }
}

