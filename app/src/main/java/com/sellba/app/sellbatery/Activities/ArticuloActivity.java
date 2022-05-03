package com.sellba.app.sellbatery.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellba.app.sellbatery.Database.Database;
import com.sellba.app.sellbatery.R;

public class ArticuloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView=findViewById(R.id.articulo_descripcion);
        TextView nombre=findViewById(R.id.articulo_nombre);
        ImageView imagen=findViewById(R.id.articulo_imagen);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        Database database=new Database(getApplicationContext());
      database.open();
      try{
          Intent mIntent = getIntent();
          int id = Integer.parseInt(mIntent.getStringExtra("id"));
          Cursor c =database.getArticulo(id);
          c.moveToFirst();
          textView.setText(c.getString(3));
          nombre.setText(c.getString(1));
          fab.setClickable(false);
          fab.setVisibility(View.INVISIBLE);
          imagen.setImageURI(Uri.parse(c.getString(4)));

      }catch (Exception e){

          database.close();

      }
       database.close();


    }

}
