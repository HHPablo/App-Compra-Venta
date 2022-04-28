package com.sellba.app.sellbatery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sellba.app.sellbatery.Activities.MenuActivity;
import com.sellba.app.sellbatery.Database.Database;
import com.sellba.app.sellbatery.Utils.SessionManager;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private  Context context;

    Database database;
    // Session Manager Class
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplication();
        database =new Database(getApplication());

        session=new SessionManager(context);

        Button button_info=(Button) findViewById(R.id.boton_info);
        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri path= Uri.parse("https://github.com/Was17/App-Compra-Venta");
                Intent intent=new Intent(Intent.ACTION_VIEW,path);
                startActivity(intent);
            }
        });
        Button button_inciar_sesion=(Button) findViewById(R.id.button_inicio_sesion);
        Button button_registro=(Button) findViewById(R.id.button_registro);
        button_inciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                View view=getLayoutInflater().inflate(R.layout.dialog_inicio_sesion,null);
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                dialog.show();
                final EditText email=view.findViewById(R.id.email_sesion);
                final EditText editPassword= (EditText) view.findViewById(R.id.password_sesion);
                //PARA VISUALIZAR CONTRASEÑA
                // editPassword.setTransformationMethod(null);
                // PARA BLOQUEAR VISUALIZACION
                //editPassword.setTransformationMethod(new PasswordTransformationMethod());
                Button buttonSesion=(Button) view.findViewById(R.id.button_dialog_inicio_sesion);
                buttonSesion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        // Get username, password from EditText
                        String username = email.getText().toString();
                        String password = editPassword.getText().toString();

                        // Check if username, password is filled
                        if(username.trim().length() > 0 && password.trim().length() > 0){
                            // For testing puspose username, password is checked with sample data
                            // username = test
                            // password = test
                            if(!username.equals("name")){
                                Cursor c;
                                try {
                                    database.open();
                                    c = database.getUserByName(username);
                                    if(username.equals(c.getString(1)) && password.equals(c.getString(3))){

                                        // Creating user login session
                                        // For testing i am stroing name, email as follow
                                        // Use user real data

                                        session.createLoginSession(username, c.getString(2),c.getString(0));
                                        database.close();
                                        // Staring MainActivity
                                        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta",Toast.LENGTH_LONG).show();
                                        database.close();

                                    }
                                }catch(Exception e){

                                    Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta",Toast.LENGTH_LONG).show();
                                    database.close();
                                }
                            }else{
                                // username / password doesn't match
                                Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Usuario o contraseña sin completar",Toast.LENGTH_LONG).show();

                            // user didn't entered username or password
                            // Show alert asking him to enter the details


                        }

                    }
                });

                Button buttonRegistro=(Button) view.findViewById(R.id.button_dialog_cancelar_inicio);
                buttonRegistro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });
        button_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                View view=getLayoutInflater().inflate(R.layout.dialog_registro,null);
                builder.setView(view);

                final EditText Email=view.findViewById(R.id.email_registro);
                final EditText user_name=view.findViewById(R.id.user_name_registro);
                final EditText Password=view.findViewById(R.id.password_registro);
                Button buttn=(Button) view.findViewById(R.id.button_dialog_registro);
                buttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get username, password from EditText
                        String username = user_name.getText().toString();
                        String password = Password.getText().toString();
                        String email=       Email.getText().toString();

                        // Check if username, password is filled

                        if(email.trim().length() <= 0) {
                            Email.setError("Email sin rellenar");
                        }else if (!validarEmail(email.trim())){
                            Email.setError("Email no valido");

                        }else if (username.trim().length() <= 0){
                            user_name.setError("Usuario sin rellenar");
                        }else if (password.trim().length() <= 0){
                            Password.setError("Contraseña sin rellenar");
                        }
                        else {
                            Cursor cursor;
                            try{
                                database.open();
                                cursor=database.getUserByName(username);
                                if(username.equals(cursor.getString(1))){
                                    user_name.setError("El Usuario ya existe");
                                }
                                database.close();
                            }catch(Exception e) {
                                try{
                                    cursor=database.getUserByEmail(email);
                                    if(email.equals(cursor.getString(2))){
                                        Email.setError("El Email ya existe");
                                    }
                                    database.close();

                                }catch(Exception a) {
                                    database.insertUser(username, email, password, "sdg");
                                    int x=database.getAllUsers().getCount();
                                    database.close();

                                    session.createLoginSession(username, email, String.valueOf(x));

                                    // Staring MainActivity
                                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }
    public boolean validarEmail(String email) {
        Pattern pattern= Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
