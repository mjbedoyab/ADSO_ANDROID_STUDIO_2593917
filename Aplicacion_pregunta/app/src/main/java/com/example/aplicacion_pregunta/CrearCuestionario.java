package com.example.aplicacion_pregunta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicacion_pregunta.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrearCuestionario extends AppCompatActivity {
    TextView nombre_usuario;
    TextView fecha;
    String id_usuario_post;
    String fecha_actual;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuestionario);

        nombre_usuario = findViewById(R.id.nombre_usuario);
        fecha = findViewById(R.id.fecha);
        config = new Config(getApplicationContext());

        // Obtenemos la fecha y hora actual
        Date fechaHoraActual = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fecha_actual = formato.format(fechaHoraActual);

        SharedPreferences archivo = getSharedPreferences("preguntas-v2-db", MODE_PRIVATE);
        String id_usuario = archivo.getString("id_usuario", null);
        id_usuario_post = id_usuario;

        nombre_usuario.setText(archivo.getString("nombres", ""));
        fecha.setText("" + fecha_actual);
    }

    public void consumoInsertCuestionario(View view) {
        // CONSUMO GET PARA TRAER LOS CUESTIONARIOS
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = config.getEndpoint("API-Preguntas/crearCuestionario.php");

        StringRequest solicitud = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean valorBooleano = jsonObject.getBoolean("status");

                    if (valorBooleano) {
                        cambiarActivity(jsonObject);
                    } else {
                        System.out.println("Error en el estado");
                    }

                } catch (JSONException e) {
                    System.out.println("El servidor POST responde con un error:");
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("El servidor POST responde con un error:");
                System.out.println(error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_usuario", id_usuario_post);
                params.put("fecha_inicio", fecha_actual);
                return params;
            }
        };

        queue.add(solicitud);
    }

    public void cambiarActivity(JSONObject objeto) {
        String id_cuestionario = null;
        try {
            id_cuestionario = String.valueOf(objeto.getInt("id_cuestionario"));
            Intent intencion = new Intent(getApplicationContext(), PreguntasCuestionarios.class);
            intencion.putExtra("id", id_cuestionario);
            intencion.putExtra("fecha_actual", fecha_actual);
            startActivity(intencion);

            finish();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
