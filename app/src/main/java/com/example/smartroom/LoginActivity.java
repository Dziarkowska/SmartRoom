package com.example.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final Button button = findViewById(R.id.login_btn);
        button.getBackground().setAlpha(128);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onAdminLoginAttempt();
            }
        });

    }

    public void openRoomKeyForAdminActivity(String login){

        Intent intent = new Intent( this, RoomKeyForAdmin.class);
        intent.putExtra("login", login);
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
        WebTarget target = ClientBuilder.newClient().target(DataConstants.ADMIN_LOGIN_ENDPOINT);

        try
        {
            if(validateResponse(target, jsonString))
            {
                openRoomKeyForAdminActivity(login);
            }
            else{
                incorrectLoginMessage();
            }
        }
        catch (Exception exception)
        {
            Log.w("LoginActivity ", exception);
        }
    }

    private boolean validateResponse(WebTarget webTarget, String jsonString)
        throws InterruptedException, ExecutionException
    {
        Future<Response> response = webTarget.request().async().post(Entity.json(jsonString));
        Integer status = response.get().getStatus();
        response.get().close();
        Log.d("Admin login status ", status.toString());
        return status.equals(DataConstants.SUCCESSFUL_LOGIN);
    }

    private void incorrectLoginMessage(){

        Toast.makeText(getBaseContext(),"Incorrect login or password, try again!", Toast.LENGTH_LONG).show();
        EditText login_txt = findViewById(R.id.login);
        EditText password_txt = findViewById(R.id.password);

        login_txt.setText("");
        password_txt.setText("");
    }
}

