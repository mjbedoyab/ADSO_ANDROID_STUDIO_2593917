package com.example.aplicacion_pregunta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicacion_pregunta.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView campo_email;
    TextView campo_contrasena;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campo_email = findViewById(R.id.campo_email);
        campo_contrasena = findViewById(R.id.campo_contrasena);

        config = new Config(getApplicationContext());

        validarSesion();

    }

    public void validar_ingreso(View vista){
        String email = campo_email.getText().toString();
        String password = campo_contrasena.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = config.getEndpoint("API-Preguntas/validarIngreso.php");

        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);

                    String id_usuario = jsonObject.getJSONObject("usuario").getString("id_usuario");
                    String nombres = jsonObject.getJSONObject("usuario").getString("nombres");
                    cambiarActivity(id_usuario, nombres);

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
                params.put("correo", email);
                params.put("password", password);

                return params;
            }
        };

        queue.add(solicitud);

    }

    public void cambiarActivity(String id_usuario, String nombres){
        SharedPreferences archivo = getSharedPreferences("preguntas-v2-db", MODE_PRIVATE);

        SharedPreferences.Editor editor = archivo.edit();
        editor.putString("id_usuario", id_usuario);
        editor.putString("nombres", nombres);
        editor.commit();

        Intent intencion = new Intent(getApplicationContext(), ActivityImprimir.class);
        //intencion.putExtra("id_usuario", id_usuario);
        //intencion.putExtra("nombres", nombres);

        startActivity(intencion);
        finish();
    }

    public void validarSesion(){
        SharedPreferences archivo = getSharedPreferences("preguntas-v2-db", MODE_PRIVATE);
        String id_usuarui = archivo.getString("id_usuaruo", null);
        String nombres = archivo.getString("nombres", null);

        if (id_usuarui!=null && nombres!=null){
            Intent intencion = new Intent(getApplicationContext(), ActivityImprimir.class);
            startActivity(intencion);
            finish();
        }
    }



}