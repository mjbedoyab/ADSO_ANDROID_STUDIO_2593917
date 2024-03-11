package com.example.aplicacion_pregunta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicacion_pregunta.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityImprimir extends AppCompatActivity {

    TextView etq_datos;
    LinearLayout contenedor;

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imprimir_main);

        //Bundle datos = getIntent().getExtras();
        //String nombres = datos.getString("nombres");
        //String apellidos = datos.getString("apellidos");

        SharedPreferences archivo = getSharedPreferences("preguntas-v2-db", MODE_PRIVATE);

        //INTENCION PARA ACAMBIAR DE ACTIVITY
        Intent intencion = getIntent();

        //VARIABLE DE SESION
        String id_usuario = archivo.getString("id_usuario", null);


        etq_datos = findViewById(R.id.etq_datos);
        etq_datos.setText(archivo.getString("nombres", null));
        contenedor = findViewById(R.id.contenedor_cuestionarios);
        config = new Config(getApplicationContext());
        //etq_datos.setText("");
        //etq_datos.append("nombres: " + nombres + "\n");
        //etq_datos.append("apellidos: " + apellidos + "\n");


        // CONSUMO GET PARA TRAER LOS CUESTIONARIOS
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = config.getEndpoint("API-Preguntas/obtenerCuestionario.php");

        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);
                    boolean valorBooleano = jsonObject.getBoolean("status");
                    if (valorBooleano){
                        agregarCuestionario(jsonObject);
                        //String id_usuario = jsonObject.getJSONObject("usuario").getString("id_usuario");
                        //String nombres = jsonObject.getJSONObject("usuario").getString("nombres");

                        //cambiarActivity(id_usuario, nombres);
                    }else{
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
        }){
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_usuario", id_usuario);
                return params;
            }
        };

        queue.add(solicitud);



    }

    public void agregarCuestionario(JSONObject objeto){
        JSONArray arreglo = null;
        try {
            arreglo = objeto.getJSONArray("cuestionarios");

            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject cuestionario = arreglo.getJSONObject(i);
                String id = cuestionario.getString("id");
                String fecha_inicio = cuestionario.getString("fecha_inicio");
                String cant_preguntas = cuestionario.getString("cant_preguntas");
                TextView textView = new TextView(getApplicationContext());

                // Concatenar los datos en una cadena
                String texto = "ID: " + cuestionario.getString("id") + "\n" +
                        "CANT PREGUNTAS: " + cuestionario.getString("cant_preguntas") + "\n" +
                        "CANT OK: " + cuestionario.getString("cant_ok") + "\n" +
                        "CANT ERROR: " + cuestionario.getString("cant_error") + "\n" +
                        "FECHA DE INICIO: " + cuestionario.getString("fecha_inicio") + "\n" +
                        "FECHA DE FIN: " + cuestionario.getString("fecha_fin");

                // Definir los parámetros de diseño para el TextView
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Ancho
                        LinearLayout.LayoutParams.WRAP_CONTENT  // Alto
                );
                layoutParams.setMargins(0, 20, 0, 20);
                // Establecer el texto
                textView.setText(texto);
                // Aplicar los parámetros de diseño al TextView
                textView.setLayoutParams(layoutParams);

                int color = ContextCompat.getColor(this, R.color.verde_claro);
                textView.setBackgroundColor(color);

                textView.setBackgroundResource(R.drawable.border_text);

                // Crear el botón
                Button button = new Button(getApplicationContext());
                button.setText("Ver Detalle");
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                // Asignar el evento onClick al botón
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Llamar a la función que deseas
                        detalleCuestionario(id , fecha_inicio, cant_preguntas);
                    }
                });
                // Agregar el TextView al contenedor
                contenedor.addView(textView);
                // Agregar el botón al contenedor
                contenedor.addView(button);
            }





        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void detalleCuestionario(String id, String fecha_inicio, String cant_preguntas){
        Intent intencion = new Intent(getApplicationContext(), DetalleCuestionario.class);
        intencion.putExtra("id",id);
        intencion.putExtra("fecha_inicio",fecha_inicio);
        intencion.putExtra("cant_preguntas",cant_preguntas);
        startActivity(intencion);

        finish();
    }

    public void IniciarQuestionario(View view) {
        Intent intencion = new Intent(getApplicationContext(), CrearCuestionario.class);
        startActivity(intencion);

        finish();
    }

    public void cerrarSesion(View vista){
        SharedPreferences archivo = getSharedPreferences("preguntas-v2-db", MODE_PRIVATE);

        SharedPreferences.Editor editor = archivo.edit();
        editor.clear();
        editor.commit();

        Intent intencion = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intencion);
        finish();
    }
}
