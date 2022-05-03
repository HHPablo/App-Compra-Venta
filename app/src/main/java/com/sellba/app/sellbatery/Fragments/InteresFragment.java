package com.sellba.app.sellbatery.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellba.app.sellbatery.Class.Articulo;
import com.sellba.app.sellbatery.Database.Database;
import com.sellba.app.sellbatery.R;
import com.sellba.app.sellbatery.Utils.SessionManager;

import java.util.ArrayList;
public class InteresFragment extends Fragment {


    public InteresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // Set padding for Tiles
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(0, tilePadding, 0, tilePadding);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return recyclerView;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre;
        private CardView mCardViewTop;
        private View insideLayout;
        private ImageView foto;
        private ImageButton interesa;
        private TextView precio;
        private Context context;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.card_articulo2, parent, false));
            mCardViewTop=itemView.findViewById(R.id.card_articulo);
            insideLayout = itemView.findViewById(R.id.layout_card_articulo);
            context=itemView.getContext();
            nombre=(TextView) itemView.findViewById(R.id.card_title);
            foto=(ImageView) itemView.findViewById(R.id.card_image);
            precio=(TextView) itemView.findViewById(R.id.card_precio);
            interesa=(ImageButton) itemView.findViewById(R.id.share_button);
        }
    }
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{
        // Set numbers of List in RecyclerView.
        ArrayList<Articulo> objetos;
        Database database;
        public ContentAdapter(Context context) {
            try {

                database = new Database(context);
                database.open();
                Cursor interes = database.getInterestByUser(new SessionManager(context).getid());
                this.objetos = new ArrayList<Articulo>();
               do{
                   try{
                       Cursor c = database.getArticulo(interes.getInt(2));
                       if (!c.getString(5).equals(new SessionManager(context).getid())) {


                           objetos.add(new Articulo(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));

                       }
                   }catch (Exception e){

                   }
                } while (interes.moveToNext()) ;
                database.close();
            }catch (Exception e){
                database.close();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Articulo user= objetos.get(position);
            holder.nombre.setText(user.getNombre());
            holder.mCardViewTop.setCardBackgroundColor(Color.GRAY);
            holder.foto.setImageURI(Uri.parse(user.getImagen()));
            holder.precio.setText(user.getPrecio()+" €");
            final String id=user.getId();
            holder.interesa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor cursor;
                    String vendedor=user.getVendedor();
                    database.open();
                    cursor=database.getUser(Integer.parseInt(vendedor));


                    String[]to={cursor.getString(2)};
                    sendEmail(to,null,"Compra/Venta SELLBA","Hola "+cursor.getString(1)+"Soy "+user.getVendedor()+". Estoy interesado en tu articulo " +user.getNombre());

                }
            });
        }

        private void sendEmail(String[]emailAddresses,String[]carbonCopies,String subjet, String message)
        {
            Intent emailIntent=new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            String[]to=emailAddresses;
            String[] cc=carbonCopies;
            emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
            emailIntent.putExtra(Intent.EXTRA_CC,cc);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,subjet);
            emailIntent.putExtra(Intent.EXTRA_TEXT,message);
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent,"Email"));

        }


        @Override
        public int getItemCount() {
            return objetos.size();
        }
    }



}
