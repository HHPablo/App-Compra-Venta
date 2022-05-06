package com.sellba.app.sellbatery.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sellba.app.sellbatery.Database.Database;
import com.sellba.app.sellbatery.R;
import com.sellba.app.sellbatery.Utils.SessionManager;

import java.util.regex.Pattern;


public class ChangeEmailActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    Database database;

    String x ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);
        context = getApplication();
        database =new Database(getApplication());
        sessionManager=new SessionManager(this);
        final EditText email_antiguo=(EditText)findViewById(R.id.oldEmail);
        final EditText email_nuevo=(EditText)findViewById(R.id.newEmail);

        Button guardar=(Button)findViewById(R.id.guardarEmail);
        guardar.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           // Get username, password from EditText
                                           String antiguo = email_antiguo.getText().toString();
                                           String nuevo = email_nuevo.getText().toString();
                                           x =email_nuevo.getText().toString();
                                           // Check if username, password is filled

                                           if(antiguo.trim().length() <= 0) {
                                               email_antiguo.setError("Escriba Antiguo Correo");
                                           }else if (nuevo.trim().length() <= 0) {
                                               email_nuevo.setError("Escriba Nuevo Correo");
                                           }else if(!validarEmail(nuevo)){
                                               email_nuevo.setError("Formato de correo erroneo");

                                           }
                                           else {
                                               Cursor cursor,cursor1;

                                               database.open();
                                               cursor=database.getUserByName(sessionManager.getUserDetails().get("name"));
                                               if(!antiguo.equals(cursor.getString(2))){
                                                   email_antiguo.setError("Correo Incorrecto");

                                               }
                                               else{
                                                   try{
                                                    cursor1=database.getUserByEmail(nuevo);
                                                    if(nuevo.equals(cursor1.getString(2))){
                                                        email_nuevo.setError("El Correo ya esta en uso por otro Usuario");
                                                    }


                                                   }catch(Exception e){
                                                       SessionManager sessionManager=new SessionManager(getApplicationContext());
                                                       sessionManager.email(nuevo);
                                                       database.updateUser(cursor.getLong(0),cursor.getString(1),nuevo,cursor.getString(3),cursor.getString(4));
                                                       Toast.makeText(getApplicationContext(),"Se ha actualizado el correo correctamente",Toast.LENGTH_LONG).show();
                                                       finish();

                                                   }

                                               }
                                               database.close();

                                           }

                                       }
                                   }
        );


    }
    public boolean validarEmail(String email) {
        Pattern pattern= Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void finish() {
        Intent intent=new Intent(this,ProfileActivity.class);
        startActivity(intent);
       super.finish();
    }
}
