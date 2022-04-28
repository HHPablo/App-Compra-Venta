package com.sellba.app.sellbatery.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sellba.app.sellbatery.Database.Database;
import com.sellba.app.sellbatery.R;
import com.sellba.app.sellbatery.Utils.SessionManager;


public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST = 1;
    private Context context;
    private SessionManager sessionManager;
    Database database;
    TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = getApplication();
        database =new Database(getApplication());
        sessionManager=new SessionManager(this);
         email=(TextView)findViewById(R.id.email_perfil);
        TextView nombre=(TextView)findViewById(R.id.user_name_perfil);
        Button modificar_contraseña=(Button)findViewById(R.id.button_modificar_contraseña);
        Button modificar_email=(Button)findViewById(R.id.button_modificar_email);
        email.setText(sessionManager.getUserDetails().get("email"));
        nombre.setText(sessionManager.getUserDetails().get("name"));
        modificar_contraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent = new Intent(getApplicationContext(),ChangePasswordActivity.class);
                startActivity(intent);

            }
        });
        modificar_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),ChangeEmailActivity.class);
                startActivity(i);
                finish();
            }
        });

    }





}
