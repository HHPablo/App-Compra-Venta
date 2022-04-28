package com.sellba.app.sellbatery.Activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sellba.app.sellbatery.Database.Database;
import com.sellba.app.sellbatery.R;
import com.sellba.app.sellbatery.Utils.SessionManager;

/**
 * Created by David Garcia Herrero on 12/01/2018.
 */

public class ChangePasswordActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
       context = getApplication();
        database =new Database(getApplication());
        sessionManager=new SessionManager(this);
        final EditText contraseña_antigua=(EditText)findViewById(R.id.oldPassword);
        final EditText contraseña_nueva=(EditText)findViewById(R.id.newPassword);
        final EditText repetir_contraseña_nueva=(EditText)findViewById(R.id.repeatNewPassword);
        Button guardar=(Button)findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get username, password from EditText
                String password = contraseña_antigua.getText().toString();
                String newPassword = contraseña_nueva.getText().toString();
                String repeatPassword=repetir_contraseña_nueva.getText().toString();

                // Check if username, password is filled

                if(password.trim().length() <= 0) {
                    contraseña_antigua.setError("Escriba Antigua Contraseña");
                }else if (newPassword.trim().length() <= 0){
                    contraseña_nueva.setError("Escriba Nueva Contraseña");

                }else if (repeatPassword.trim().length() <= 0){
                    repetir_contraseña_nueva.setError("Repita La Nueva Contraseña");
                }else if (!newPassword.equals(repeatPassword)){
                    repetir_contraseña_nueva.setError("No Coincide La Nueva Contraseña");
                }
                else {
                    Cursor cursor;

                        database.open();
                        cursor=database.getUserByName(sessionManager.getUserDetails().get("name"));
                        if(!password.equals(cursor.getString(3))){
                            contraseña_antigua.setError("Contraseña Incorrecta");
                        }
                        else{
                            database.updateUser(cursor.getLong(0),cursor.getString(1),cursor.getString(2),newPassword,cursor.getString(4));
                            Toast.makeText(getApplicationContext(),"Se ha actualizado la contraseña correctamente",Toast.LENGTH_LONG).show();
                            finish();
                        }
                        database.close();

                    }

                }
            }
        );


            }
        }


