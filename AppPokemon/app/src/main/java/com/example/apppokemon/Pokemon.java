package com.example.apppokemon;

public class Pokemon {


    String nombre;

    String url;
    int id_pokemon;

    public Pokemon(String nombre, String url, int id_pokemon) {
        this.nombre = nombre;
        this.url = url;
        this.id_pokemon = id_pokemon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId_pokemon() {
        return id_pokemon;
    }

    public void setId_pokemon(int id_pokemon) {
        this.id_pokemon = id_pokemon;
    }
}

