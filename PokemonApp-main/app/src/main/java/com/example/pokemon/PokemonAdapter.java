package com.example.pokemon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

// La clase de adaptador se utiliza para proporcionar una vista para cada Pokémon, ver la actividad "item_pokemon".

// Este código utiliza la biblioteca 'Glide' para cargar las imágenes y también la biblioteca 'Chip' que muestra los tipos de Pokémon.
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {
    private List<Pokemon> pokemonList; // el array con los pokemones

    public PokemonAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    // El método "ViewHolder onCreateViewHolder" se llama cuando el RecyclerView necesita un nuevo objeto ViewHolder.
    // Infla un archivo de diseño de recursos (R.layout.item_pokemon) para crear un objeto View y devuelve un objeto ViewHolder que contiene la View.
    // También establece un listener de clic en la View para abrir la actividad de detalles del objeto Pokémon seleccionado.

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Pokemon pokemon = pokemonList.get(position);
                v.getContext().startActivity(pokemon.getIntent(v.getContext()));
            }
        });
        return viewHolder;
    }

    // "onBindViewHolder" actualiza el ViewHolder con los datos del objeto Pokémon correspondiente,
    // establece el ImageView con la imagen del Pokémon usando Glide y agrega vistas Chip para cada tipo de Pokémon.

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.nameTextView.setText(pokemon.getName());
        holder.idTextView.setText(String.format("#%03d", pokemon.getId()));
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl())
                .into(holder.imageView);
        holder.typeChipGroup.removeAllViews();
        for (String type : pokemon.getTypes()) {
            Chip chip = new Chip(holder.itemView.getContext());
            chip.setText(type);
            holder.typeChipGroup.addView(chip);
        }
    }

    @Override
    public int getItemCount() {
        return pokemonList.size(); // devuelve el tamaño de la lista de Pokémon.
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView idTextView;
        public ImageView imageView;
        public ChipGroup typeChipGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            idTextView = itemView.findViewById(R.id.id_text_view);
            imageView = itemView.findViewById(R.id.image_view);
            typeChipGroup = itemView.findViewById(R.id.type_chip_group);
        }
    }
}
