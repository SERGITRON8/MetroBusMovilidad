package com.example.metrobusmovilidad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class estaciones extends AppCompatActivity implements View.OnClickListener {

    private final FirebaseDatabase Db = FirebaseDatabase.getInstance();
    private FirebaseAuth fAuth;
    private final DatabaseReference requestCollection = Db.getReference().child("Requests").push();
    private DatabaseReference usersCollection = Db.getReference().child("Users");
    private Spinner spinner1, spinner2, spinner3, spinner4;
    Button btnSolicitar, btnLogout;
    public List<ModeloEstacion> listaLinea1;
    public List<ModeloEstacion> listaLinea2;
    public int IdLineaArribo = 0, IdEstacionArribo = 0, IdLineaDescenso = 0, IdEstacionDescenso = 0;
    public String lineaDestino, lineaOrigen, estacionDestino, estacionOrigen;
    public TextView userStatus;
    private static final String TAG = "Response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones);

        String authUser;
        authUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        usersCollection = usersCollection.child(authUser);
        userStatus = findViewById(R.id.userStatus);

        usersCollection.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model_usuario schema_user = snapshot.getValue(Model_usuario.class);
                assert schema_user != null;
                String arrived = schema_user.getArrived();

                if (arrived.equals("true")) {
                    userStatus.setVisibility(View.INVISIBLE);
                    btnSolicitar.setClickable(true);
                    spinner1.setClickable(true);
                    spinner2.setClickable(true);
                    spinner3.setClickable(true);
                    spinner4.setClickable(true);
                } else {
                    userStatus.setVisibility(View.VISIBLE);
                    btnSolicitar.setClickable(false);
                    spinner1.setClickable(false);
                    spinner2.setClickable(false);
                    spinner3.setClickable(false);
                    spinner4.setClickable(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled set visible start: "+ error);
            }
        });

        btnSolicitar = findViewById(R.id.btn_apoyo);
        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (IdEstacionArribo == 0 || IdEstacionDescenso == 0) {
                        Toast.makeText(estaciones.this, "Seleccione una estacion de origen y destino", Toast.LENGTH_LONG).show();
                    } else {
                        postRequest();
                    }

                } catch (IOException e) {
                    Log.d(TAG, "error: "+ e);
                    e.printStackTrace();
                }
            }
        });

        btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(estaciones.this , "Session Terminada.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        listaLinea1 = new ArrayList<ModeloEstacion>();
        listaLinea2 = new ArrayList<ModeloEstacion>();


        String[] linea1 = {"Talleres", "San Bernabe", "Unidad Modelo", "Aztlan", "Penitenciaria", "Alfonso Reyes", "Mitras", "Simon Bolivar", "Hospital", "Edison", "Central", "Cuauhtemoc", "Del Golfo", "Felix U. Gomez", "Parque Fundidora", "Y Griega", "Eloy Cavazos", "Lerdo De Tejada", "Exposicion"};
        String[] linea2 = {"Sendero", "Santiago Tapia", "San Nicolas", "Anahuac", "Universidad", "Niños Heroes", "Regina", "General Anaya", "Cuauhtemoc", "Alameda", "Fundadores", "Padre Mier", "General Zaragoza"};


        for (int i = 0; i < linea1.length; i++) {
            ModeloEstacion estacion = new ModeloEstacion();
            estacion.id = i + 1;
            estacion.nombreEstacion = linea1[i];
            listaLinea1.add((estacion));
        }

        for (int i = 0; i < linea2.length; i++) {
            ModeloEstacion estacion = new ModeloEstacion();
            estacion.id = i + 1;
            estacion.nombreEstacion = linea2[i];
            listaLinea2.add((estacion));
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        spinner1 = findViewById(R.id.spr_ubilinea1);
        ArrayAdapter<ModeloEstacion> adapter1 = new ArrayAdapter<ModeloEstacion>(this, android.R.layout.simple_spinner_item, listaLinea1);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(adapter1.NO_SELECTION, true); //Add this line before setting listener
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(estaciones.this, listaLinea1.get(position).nombreEstacion,
                        Toast.LENGTH_LONG).show();*/

                IdLineaArribo = 1;
                IdEstacionArribo = listaLinea1.get(position).id;
                lineaOrigen = "Linea 1";
                estacionOrigen = linea1[IdEstacionArribo];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });


        spinner3 = findViewById(R.id.spr_deslinea1);
        ArrayAdapter<ModeloEstacion> adapter3 = new ArrayAdapter<ModeloEstacion>(this, android.R.layout.simple_spinner_item, listaLinea1);
        spinner3.setAdapter(adapter3);
        spinner3.setSelection(adapter3.NO_SELECTION, true); //Add this line before setting listener
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IdLineaDescenso = 1;
                IdEstacionDescenso = listaLinea1.get(position).id;
                lineaDestino = "Linea 1";
                estacionDestino = linea1[IdEstacionDescenso];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });


        spinner2 = findViewById(R.id.spr_ubilinea2);
        ArrayAdapter<ModeloEstacion> adapter2 = new ArrayAdapter<ModeloEstacion>(this, android.R.layout.simple_spinner_item, listaLinea2);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(adapter2.NO_SELECTION, true); //Add this line before setting listener
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IdLineaArribo = 2;
                IdEstacionArribo = listaLinea2.get(position).id;
                lineaDestino = "Linea 2";
                estacionDestino = linea2[IdEstacionArribo];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });


        spinner4 = findViewById(R.id.spr_deslinea2);
        ArrayAdapter<ModeloEstacion> adapter4 = new ArrayAdapter<ModeloEstacion>(this, android.R.layout.simple_spinner_item, listaLinea2);
        spinner4.setAdapter(adapter4);
        spinner4.setSelection(adapter4.NO_SELECTION, true); //Add this line before setting listener
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IdLineaDescenso = 2;
                IdEstacionDescenso = listaLinea2.get(position).id;
                lineaDestino = "Linea 2";
                estacionDestino = linea2[IdEstacionDescenso];
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
                if (spinner1.getVisibility() == View.GONE) {
                    spinner1.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.GONE);
                } else {
                    spinner1.setVisibility(View.GONE);

                }
                break;
            case R.id.btn_ubilinea2:
                if (spinner2.getVisibility() == View.GONE) {
                    spinner2.setVisibility(View.VISIBLE);
                    spinner1.setVisibility(View.GONE);
                } else {
                    spinner2.setVisibility(View.GONE);
                }
                break;

            case R.id.btn_deslinea1:
                if (spinner3.getVisibility() == View.GONE) {
                    spinner3.setVisibility(View.VISIBLE);
                    spinner4.setVisibility(View.GONE);
                } else {
                    spinner3.setVisibility(View.GONE);

                }
                break;
            case R.id.btn_deslinea2:
                if (spinner4.getVisibility() == View.GONE) {
                    spinner4.setVisibility(View.VISIBLE);
                    spinner3.setVisibility(View.GONE);
                } else {
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
        final String[] user_name = new String[1];
        final String[] user_phone = new String[1];
        String authUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userStatus = findViewById(R.id.userStatus);
        HashMap<String, String> requestMap = new HashMap<>();

        usersCollection.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model_usuario schema_user = snapshot.getValue(Model_usuario.class);

                requestMap.put("id_origin_line", String.valueOf(IdLineaArribo));
                requestMap.put("id_origin_estacion", String.valueOf(IdEstacionArribo));
                requestMap.put("origin_line", String.valueOf(lineaOrigen));
                requestMap.put("origin_estacion", String.valueOf(estacionOrigen));
                requestMap.put("id_destiny_line", String.valueOf(IdLineaDescenso));
                requestMap.put("id_destiny_estacion", String.valueOf(IdEstacionDescenso));
                requestMap.put("destiny_line", String.valueOf(lineaDestino));
                requestMap.put("destiny_estacion", String.valueOf(estacionDestino));
                requestMap.put("user_name", String.valueOf(schema_user.getName()));
                requestMap.put("user_phone", String.valueOf(schema_user.getPhone()));

                requestCollection.setValue(requestMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null){
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("arrived", "false");
                            usersCollection.updateChildren(userMap);

                            userStatus.setVisibility(View.VISIBLE);
                            btnSolicitar.setClickable(false);
                            spinner1.setClickable(false);
                            spinner2.setClickable(false);
                            spinner3.setClickable(false);
                            spinner4.setClickable(false);

                            Toast.makeText(estaciones.this , "Solicitud Recibida!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(estaciones.this , "Ocurrio un error. " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled User Collection: "+ error);
            }
        });

//        MediaType MEDIA_TYPE = MediaType.parse("application/json");
//        String url = "http://tslserver.ddns.net:5959/api/estacion";
//
//        OkHttpClient client = new OkHttpClient();
//
//        JSONObject postdata = new JSONObject();
//        try {
//            //postdata.put("email", "francisco");
//            //postdata.put("password", "1234");
//            postdata.put("IdLineaArribo", IdLineaArribo);
//            postdata.put("IdEstacionArribo", IdEstacionArribo);
//            postdata.put("IdLineaDescenso", IdLineaDescenso);
//            postdata.put("IdEstacionDescenso", IdEstacionDescenso);
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                String mMessage = e.getMessage().toString();
//                Log.w("failure Response", mMessage);
//                //cuando falla el apoi
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//
//                estaciones.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(estaciones.this, "Se grabo el registro correctamente", Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//            }
//        });
    }


}
