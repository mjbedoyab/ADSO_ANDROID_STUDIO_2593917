package com.example.apppokemon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    AdaptadorPokemon adaptador;
    List<Pokemon> lista;
    Button btnSiguiente;
    Button btnAnterior;


    public String nextUrl;
    public String previousUrl;

    int id_pokemons = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.recycler_pokemon);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnAnterior = findViewById(R.id.btnAnterior);
        traerPokemons();
    }

    public void traerPokemons() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://pokeapi.co/api/v2/pokemon";
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray lista_pokemons = response.getJSONArray("results");
                    previousUrl = response.getString("previous");
                    lista = new ArrayList<>();
                    for (int i = 0; i < lista_pokemons.length(); i++) {
                        id_pokemons++;
                        JSONObject client = lista_pokemons.getJSONObject(i);
                        String name = client.getString("name");
                        String url = client.getString("url");
                        lista.add(new Pokemon(name, url,id_pokemons));
                        System.out.println("Id Pokemon Inicio: "+id_pokemons);
                    }
                    adaptador = new AdaptadorPokemon(lista);
                    recycler.setAdapter(adaptador);
                    recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("El servidor responde con un error:");
                System.out.println(error.getMessage());
            }
        });

        queue.add(solicitud);
    }

    public void next(View view) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = nextUrl != null ? nextUrl : "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20/";
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray lista_pokemons = response.getJSONArray("results");
                    nextUrl = response.getString("next");
                    previousUrl = response.optString("previous");

                    lista = new ArrayList<>();
                    lista.clear();
                    for (int i = 0; i < lista_pokemons.length(); i++) {
                        JSONObject client = lista_pokemons.getJSONObject(i);
                        String name = client.getString("name");
                        String url = client.getString("url");
                        lista.add(new Pokemon(name, url,id_pokemons));
                        id_pokemons ++;
                    }
                    adaptador = new AdaptadorPokemon(lista);
                    recycler.setAdapter(adaptador);
                    recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("El servidor responde con un error:");
                System.out.println(error.getMessage());
            }
        });

        queue.add(solicitud);
    }

    public void previous(View view) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = previousUrl != null ? previousUrl : "https://pokeapi.co/api/v2/pokemon?offset=0&limit=20";
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray lista_pokemons = response.getJSONArray("results");
                    previousUrl = response.optString("previous");
                    nextUrl = response.optString("next");
                    lista = new ArrayList<>();
                    lista.clear();
                    for (int i = 0; i < lista_pokemons.length(); i++) {
                        JSONObject client = lista_pokemons.getJSONObject(i);
                        String name = client.getString("name");
                        String url = client.getString("url");
                        lista.add(new Pokemon(name, url,id_pokemons));
                        id_pokemons--;
                    }
                    adaptador = new AdaptadorPokemon(lista);
                    recycler.setAdapter(adaptador);
                    recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("El servidor responde con un error:");
                System.out.println(error.getMessage());
            }
        });

        queue.add(solicitud);
    }
}

