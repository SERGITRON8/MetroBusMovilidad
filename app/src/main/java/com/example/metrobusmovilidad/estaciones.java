package com.example.metrobusmovilidad;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class estaciones extends AppCompatActivity implements View.OnClickListener{

    private Spinner spinner1,spinner2,spinner3,spinner4;
    Button btnSolicitar;
    public         List<ModeloEstacion> listaLinea1;
    public         List<ModeloEstacion> listaLinea2;
    public int     IdLineaArribo = 0,IdEstacionArribo = 0,IdLineaDescenso=0,IdEstacionDescenso=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones);

        btnSolicitar = (Button) findViewById(R.id.btn_apoyo);
        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(IdEstacionArribo==0||IdEstacionDescenso==0){
//Mensaje validacioón

                          Toast.makeText(estaciones.this, "Seleccione los campos ",
                        Toast.LENGTH_LONG).show();


                    }else{
                        postRequest();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        listaLinea1 = new ArrayList<ModeloEstacion>();
        listaLinea2 = new ArrayList<ModeloEstacion>();


        String [] linea1 = {"Talleres", "San Bernabe", "Unidad Modelo", "Aztlan", "Penitenciaria", "Alfonso Reyes","Mitras", "Simon Bolivar", "Hospital","Edison", "Central", "Cuauhtemoc","Del Golfo", "Felix U. Gomez", "Parque Fundidora", "Y Griega", "Eloy Cavazos", "Lerdo De Tejada", "Exposicion"};
        String [] linea2 = {"Sendero","Santiago Tapia","San Nicolas","Anahuac","Universidad","Niños Heroes","Regina","General Anaya","Cuauhtemoc","Alameda","Fundadores","Padre Mier","General Zaragoza"};


        for(int i = 0; i<linea1.length; i++){
            ModeloEstacion estacion = new ModeloEstacion();
            estacion.id = i+1;
            estacion.nombreEstacion = linea1[i];
            listaLinea1.add((estacion));
        }

        for(int i = 0; i<linea2.length; i++){
            ModeloEstacion estacion = new ModeloEstacion();
            estacion.id = i+1;
            estacion.nombreEstacion = linea2[i];
            listaLinea2.add((estacion));
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        spinner1 = (Spinner)findViewById(R.id.spr_ubilinea1);
        ArrayAdapter <ModeloEstacion> adapter1 = new ArrayAdapter<ModeloEstacion>(this, android.R.layout.simple_spinner_item, listaLinea1);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(adapter1.NO_SELECTION, true); //Add this line before setting listener
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(estaciones.this, listaLinea1.get(position).nombreEstacion,
                        Toast.LENGTH_LONG).show();*/

                IdLineaArribo = 1;
                IdEstacionArribo = listaLinea1.get(position).id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });


        spinner3 = (Spinner)findViewById(R.id.spr_deslinea1);
        ArrayAdapter <ModeloEstacion> adapter3 = new ArrayAdapter<ModeloEstacion>(this, android.R.layout.simple_spinner_item, listaLinea1);
        spinner3.setAdapter(adapter3);
        spinner3.setSelection(adapter3.NO_SELECTION, true); //Add this line before setting listener
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IdLineaDescenso = 1;
                IdEstacionDescenso = listaLinea1.get(position).id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });






        spinner2 = (Spinner)findViewById(R.id.spr_ubilinea2);
        ArrayAdapter <ModeloEstacion> adapter2 = new ArrayAdapter<ModeloEstacion>(this, android.R.layout.simple_spinner_item,listaLinea2);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(adapter2.NO_SELECTION, true); //Add this line before setting listener
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                IdLineaArribo = 2;
                IdEstacionArribo = listaLinea2.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });



        spinner4 = (Spinner)findViewById(R.id.spr_deslinea2);
        ArrayAdapter <ModeloEstacion> adapter4 = new ArrayAdapter<ModeloEstacion>(this, android.R.layout.simple_spinner_item,listaLinea2);
        spinner4.setAdapter(adapter4);
        spinner4.setSelection(adapter4.NO_SELECTION, true); //Add this line before setting listener
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               @Override
                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                                   IdLineaDescenso = 2;
                                                   IdEstacionDescenso = listaLinea2.get(position).id;
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {
                                                   //Another interface callback
                                               }
                                           });




        findViewById(R.id.btn_ubilinea1).setOnClickListener(this);
        findViewById(R.id.btn_ubilinea2).setOnClickListener(this);
        findViewById(R.id.btn_deslinea1).setOnClickListener(this);
        findViewById(R.id.btn_deslinea2).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    switch (view.getId()) {

        case R.id.btn_ubilinea1:
            if (spinner1.getVisibility() == View.GONE)
            {
                spinner1.setVisibility(View.VISIBLE);
                spinner2.setVisibility(View.GONE);
            }else
            {
                spinner1.setVisibility(View.GONE);

            }
            break;
        case R.id.btn_ubilinea2:
            if (spinner2.getVisibility() == View.GONE)
            {
                spinner2.setVisibility(View.VISIBLE);
                spinner1.setVisibility(view.GONE);
            }else
            {
                spinner2.setVisibility(View.GONE);
            }
            break;

            case R.id.btn_deslinea1:
                if (spinner3.getVisibility() == View.GONE)
                {
                    spinner3.setVisibility(View.VISIBLE);
                    spinner4.setVisibility(View.GONE);
                }else
                {
                    spinner3.setVisibility(View.GONE);

                }
                break;
            case R.id.btn_deslinea2:
                if (spinner4.getVisibility() == View.GONE)
                {
                    spinner4.setVisibility(View.VISIBLE);
                    spinner3.setVisibility(view.GONE);
                }else
                {
                    spinner4.setVisibility(View.GONE);
                }
                break;
    }
    }

    public class ModeloEstacion {
        int id;
        String nombreEstacion;

        @Override
        public String toString() {
            return nombreEstacion;
        }
    }

    public void postRequest() throws IOException {

        //user = null;
        //pass = null;



        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://tslserver.ddns.net:5959/api/estacion";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        try {
            //postdata.put("email", "francisco");
            //postdata.put("password", "1234");
            postdata.put("IdLineaArribo", IdLineaArribo);
            postdata.put("IdEstacionArribo", IdEstacionArribo);
            postdata.put("IdLineaDescenso", IdLineaDescenso);
            postdata.put("IdEstacionDescenso", IdEstacionDescenso);

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //cuando falla el apoi
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                estaciones.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(estaciones.this, "Se grabo el registro correctamente", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
    }


}
