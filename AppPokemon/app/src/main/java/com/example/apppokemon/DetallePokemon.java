package com.example.apppokemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

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

public class DetallePokemon extends AppCompatActivity {
    RecyclerView recycler;
    RecyclerView recycler_img;
    TextView alturaPokemon;
    TextView pesoPokemon;
    TextView nombrePokemon;

    AdaptadorHabilidadesPokemon adaptador_habilidades_pokemon;
    List<HabilidadesPokemon> lista_habilidades_pokemon;

    List<String> lista_imagenes_pokemon;
    AdaptadorImagenPokemon adaptador_imagenes_pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pokemon);
        recycler = findViewById(R.id.recycler_habilidad_pokemon);
        recycler_img = findViewById(R.id.recycler_img_pokemon);
        alturaPokemon = findViewById(R.id.alturaPokemon);
        pesoPokemon = findViewById(R.id.pesoPokemon);
        nombrePokemon = findViewById(R.id.nombrePokemon);

        String namePokemon = getIntent().getStringExtra("namePokemon");
        nombrePokemon.setText(namePokemon);


        String url = getIntent().getStringExtra("url");
        obtenerDetallesPokemon(url);

    }

    public void obtenerDetallesPokemon(String url) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject sprites = response.optJSONObject("sprites");

                    String backDefault = sprites.optString("back_default", "");
                    String backShiny = sprites.optString("back_shiny", "");
                    String front_default = sprites.optString("front_default", "");
                    String frontShiny = sprites.optString("front_shiny", "");
                    lista_imagenes_pokemon = new ArrayList<>();
                    lista_imagenes_pokemon.add(backDefault);
                    lista_imagenes_pokemon.add(front_default);
                    lista_imagenes_pokemon.add(backShiny);
                    lista_imagenes_pokemon.add(frontShiny);

                    adaptador_imagenes_pokemon = new AdaptadorImagenPokemon(lista_imagenes_pokemon);
                    recycler_img.setAdapter(adaptador_imagenes_pokemon);
                    recycler_img.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false));

                    int altura = response.getInt("height");
                    alturaPokemon.setText(String.valueOf(altura));
                    int peso = response.getInt("weight");
                    pesoPokemon.setText(String.valueOf(peso));

                    JSONArray habilidades = response.getJSONArray("abilities");
                    lista_habilidades_pokemon = new ArrayList<>();

                    for (int i = 0; i < habilidades.length(); i++) {
                        JSONObject habilidad = habilidades.getJSONObject(i);
                        String nombreHabilidad = habilidad.getJSONObject("ability").getString("name");
                        lista_habilidades_pokemon.add(new HabilidadesPokemon(nombreHabilidad));
                    }

                    adaptador_habilidades_pokemon = new AdaptadorHabilidadesPokemon(lista_habilidades_pokemon);
                    recycler.setAdapter(adaptador_habilidades_pokemon);
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