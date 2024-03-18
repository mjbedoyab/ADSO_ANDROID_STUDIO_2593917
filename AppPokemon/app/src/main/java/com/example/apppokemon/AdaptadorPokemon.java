package com.example.apppokemon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AdaptadorPokemon  extends RecyclerView.Adapter<AdaptadorPokemon.ViewHolder>{
    List<Pokemon> ListaPokemons;
    public AdaptadorPokemon(List<Pokemon> lista) {

        this.ListaPokemons = lista;
    }



    @NonNull
    @Override
    public AdaptadorPokemon.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPokemon.ViewHolder holder, int position) {
        Pokemon temporal = ListaPokemons.get(position);
        holder.cargarDatos(temporal);

    }

    @Override
    public int getItemCount() {
        return ListaPokemons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePokemon;
        TextView idPokemon;
        ImageView imgDetalle;
        Context contexto;
        String url;
        String namePokemon;

        int id_pokemon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contexto = itemView.getContext();
            nombrePokemon = itemView.findViewById(R.id.nombrePokemon);
            imgDetalle = itemView.findViewById(R.id.imgDetalle);
            idPokemon = itemView.findViewById(R.id.idPokemon);

        }
        public  void cargarDatos(Pokemon datos){
            nombrePokemon.setText(datos.getNombre());
            idPokemon.setText("00"+datos.getId_pokemon());
            url = datos.getUrl();
            namePokemon = datos.getNombre();
            id_pokemon = datos.getId_pokemon();
            imgDetalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contexto, DetallePokemon.class);
                    intent.putExtra("url", url);
                    intent.putExtra("namePokemon", namePokemon);
                    contexto.startActivity(intent);
                }
            });
        }
    }
}
