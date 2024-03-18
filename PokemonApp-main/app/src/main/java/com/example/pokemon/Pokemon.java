package com.example.pokemon;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private String name;
    private int id;
    private String weight;
    private String height;
    private String Stat0;
    private String Stat1;
    private String Stat2;
    private String Stat3;
    private String Stat4;
    private String Stat5;
    private String imageUrl;
    private List<String> types;

    private String description;

    // Constructor
    public Pokemon(String name, int id, String imageUrl, List<String> types, String weight, String height, String Stat0, String Stat1, String Stat2, String Stat3, String Stat4, String Stat5) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.types = types;
        this.weight = weight;
        this.height = height;
        this.Stat0 = Stat0;
        this.Stat1 = Stat1;
        this.Stat2 = Stat2;
        this.Stat3 = Stat3;
        this.Stat4 = Stat4;
        this.Stat5 = Stat5;
    }

    // Métodos de obtención (getters)

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getStat0() {
        return Stat0;
    }

    public String getStat1() {
        return Stat1;
    }

    public String getStat2() {
        return Stat2;
    }

    public String getStat3() {
        return Stat3;
    }

    public String getStat4() {
        return Stat4;
    }

    public String getStat5() {
        return Stat5;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getDescription() {
        return description;
    }

    // Este intent explícito es necesario para abrir la actividad "PokemonDetailActivity",
    // y también transmite información sobre un Pokemon que fue clickeado.
    public Intent getIntent(Context context) {
        Intent intent = new Intent(context, PokemonDetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
        intent.putExtra("imageUrl", imageUrl);
        intent.putStringArrayListExtra("types", new ArrayList<String>(types));
        intent.putExtra("description", description);
        intent.putExtra("weight", weight);
        intent.putExtra("height", height);
        intent.putExtra("Stat0", Stat0);
        intent.putExtra("Stat1", Stat1);
        intent.putExtra("Stat2", Stat2);
        intent.putExtra("Stat3", Stat3);
        intent.putExtra("Stat4", Stat4);
        intent.putExtra("Stat5", Stat5);

        return intent;
    }
}

