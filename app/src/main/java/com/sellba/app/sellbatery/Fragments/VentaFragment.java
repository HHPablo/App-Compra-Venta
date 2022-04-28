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

import com.sellba.app.sellbatery.Activities.ArticuloActivity;
import com.sellba.app.sellbatery.Class.Articulo;
import com.sellba.app.sellbatery.Database.Database;
import com.sellba.app.sellbatery.R;
import com.sellba.app.sellbatery.Utils.SessionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VentaFragment extends Fragment {


    public VentaFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    ContentAdapter adapter;
    public  void noti(){
try {recyclerView.setAdapter(new ContentAdapter(getActivity()));

}catch (Exception e){

}

}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
         adapter = new ContentAdapter(getActivity());
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
            super(inflater.inflate(R.layout.card_articulo3, parent, false));
            mCardViewTop=itemView.findViewById(R.id.card_articulo);
            insideLayout = itemView.findViewById(R.id.layout_card_articulo);
            context=itemView.getContext();
            nombre=(TextView) itemView.findViewById(R.id.card_title);
            foto=(ImageView) itemView.findViewById(R.id.card_image);
            precio=(TextView) itemView.findViewById(R.id.card_precio);
        }
    }
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{
        // Set numbers of List in RecyclerView.
        ArrayList<Articulo> objetos;
        Database database;
        Context context;
        public ContentAdapter(Context context) {
            try{
                this.context=context;
                database =new Database(context);
                database.open();
                Cursor c= database.getArticuloByUser(new SessionManager(context).getid());
                this.objetos = new ArrayList<Articulo>();
                do{
                    objetos.add(new Articulo(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5)));
                } while (c.moveToPrevious());

                database.close();
            }catch (Exception e){

            }
        }
        public void add(){
            database =new Database(context);
            database.open();
            Cursor c= database.getArticuloByUser(new SessionManager(context).getid());
            c.moveToLast();
            objetos.add(new Articulo(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5)));
            database.close();

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



            holder.mCardViewTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), ArticuloActivity.class);
                    intent.putExtra("id",id);
                    v.getContext().startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return objetos.size();
        }
    }



}
