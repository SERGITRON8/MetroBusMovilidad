package com.example.metrobusmovilidad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class solicitudes extends AppCompatActivity {

    private List<String> listaSolicitudes;
    private List<modeloSolicitudes> listaModeloSolicitudes;
    private modeloSolicitudes itemSelected;
    static final String SCAN = "com.google.zxing.client.android.SCAN";
    Button btnRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);
//        muestraSolicitudes();

//        btnRefresh=findViewById(R.id.btn_actualizar);
//        btnRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                muestraSolicitudes();
//            }
//        });

    }

    public void ScanBar ( View view ) {
        try {
            //this intent is used to call start for bar code
            Intent in = new Intent( SCAN );
            in.putExtra( "SCAN_MODE", "PRODUCT_MODE" );
            startActivityForResult( in, 0 );
        } catch ( ActivityNotFoundException e) {
            showDialog( solicitudes.this,"No scanner found", "Download Scanner code Activity?"," Yes", "No" ).show();

        }
    }
    private Dialog showDialog ( final Activity act, CharSequence title,CharSequence message, CharSequence yes, CharSequence no ) {

        // a subclass of dialog that can display buttons and message
        AlertDialog.Builder download = new AlertDialog.Builder( act );
        download.setTitle( title );
        download.setMessage ( message );
        download.setPositiveButton ( yes, new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick( DialogInterface dialog, int i ) {
                // TODO Auto-generated method stub
                //uri to download barcode scanner
                Uri uri = Uri.parse( "market://search?q=pname:" + "com.google.zxing.client.android" );
                Intent in = new Intent ( Intent.ACTION_VIEW, uri );
                try {
                    act.startActivity ( in );
                } catch ( ActivityNotFoundException e) {

                }
            }
        });
        download.setNegativeButton ( no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick ( DialogInterface dialog, int i ) {
                // TODO Auto-generated method stub
            }
        });
        return download.show();
    }


    private void muestraSolicitudes(){

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://tslserver.ddns.net:5959/api/estacion/getsolicitudes";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("algo", "algo");

        } catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .get()
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
            public void onResponse(Call call, final Response response) throws IOException {


                solicitudes.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            llenaListView(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    private void llenaListView(String solicitudes)  {


        //ObjectMapper mapper = new ObjectMapper();
        try {
            JSONArray  jsonObject = new JSONArray (solicitudes);
            modeloSolicitudes  modelo;// = new modeloSolicitudes();
            listaModeloSolicitudes = new ArrayList<>();

            for(int i=0; i < jsonObject.length(); i++) {
                JSONObject jsonobject = jsonObject.getJSONObject(i);

                String idLineaArribo       = jsonobject.getString("idLineaArribo");
                String idEstacionArribo    = jsonobject.getString("idEstacionArribo");
                String idLineaDescenso  = jsonobject.getString("idLineaDescenso");
                String idEstacionDescenso = jsonobject.getString("idEstacionDescenso");
                String esArribo = jsonobject.getString("esArribo");
                String esDestino = jsonobject.getString("esDestino");
                String idUnico = jsonobject.getString("idUnico");

                modelo=new modeloSolicitudes();

                modelo.idLineaArribo = Integer.parseInt((idLineaArribo));
                modelo.idEstacionArribo = Integer.parseInt((idEstacionArribo));
                modelo.idLineaDescenso = Integer.parseInt((idLineaDescenso));
                modelo.idEstacionDescenso = Integer.parseInt((idEstacionDescenso));
                modelo.idUnico = Integer.parseInt((idUnico));
                modelo.esArribo = Boolean.parseBoolean((esArribo));
                modelo.esDestino = Boolean.parseBoolean((esDestino));

                listaModeloSolicitudes.add((modelo));
            }

            Log.d("termino","mensae");

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }

        final ListView milista =(ListView) findViewById(R.id.lvSolicitudes);
        milista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(solicitudes.this, "Clickasooo ",
                        Toast.LENGTH_SHORT).show();

                try {

                     itemSelected =   listaModeloSolicitudes.get(position);
                    //this intent is used to call start for bar code
                    Intent in = new Intent( SCAN );
                    in.putExtra( "SCAN_MODE", "PRODUCT_MODE" );
                    startActivityForResult( in, 0 );
                } catch ( ActivityNotFoundException e) {
                    showDialog( solicitudes.this,"No scanner found", "Download Scanner code Activity?"," Yes", "No" ).show();

                }



            }
        });
        //final ListView milista =findViewById(R.id.lvSolicitudes);
        //listaSolicitudes=new ArrayList<>();
        ArrayAdapter<modeloSolicitudes> solAdapter = new ArrayAdapter<>(solicitudes.this,android.R.layout.simple_list_item_1,listaModeloSolicitudes);
        milista.setAdapter(solAdapter);
        Toast.makeText(this, "Se ha actualizado la lista", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent in ) {
        // TODO Auto-generated method stub
        if( requestCode == 0 ){
            if( resultCode == RESULT_OK ){
                //use to get scan result
                String contents = in.getStringExtra( "SCAN_RESULT" );
                String format =  in.getStringExtra( "SCAN_RESULT_FORMAT" ) ;
                Toast toast = Toast.makeText( this, "Codigo :" + contents + "IdUnico: "  + itemSelected.idUnico , Toast.LENGTH_LONG );
                toast.show();
            }
        }
    }

    public class modeloSolicitudes {
        int Id;
        int idLineaArribo;
        int idEstacionArribo;
        int idLineaDescenso;
        int idEstacionDescenso;
        int idUnico;
        boolean esArribo;
        boolean esDestino;
        @Override
        public String toString() {
            String nomEstacionArribo = "";
            String nomEstacionDescenso="";
            switch (idEstacionArribo) {
                case 1:
                    nomEstacionArribo = "Talleres";
                    break;
                case 2:
                    nomEstacionArribo = "San Bernabe";
                    break;
                case 3:
                    nomEstacionArribo = "Unidad Modelo";
                    break;
                case 4:
                    nomEstacionArribo = "Aztlan";
                    break;
                case 5:
                    nomEstacionArribo = "Penitenciaria";
                    break;
                case 6:
                    nomEstacionArribo = "Alfonso Reyes";
                    break;
                case 7:
                    nomEstacionArribo = "Mitras";
                    break;
                case 8:
                    nomEstacionArribo = "Simon Bolivar";
                    break;
                case 9:
                    nomEstacionArribo = "Hospital";
                    break;
                case 10:
                    nomEstacionArribo = "Edison";
                    break;
                case 11:
                    nomEstacionArribo = "Central";
                    break;
                case 12:
                    nomEstacionArribo = "Cuauhtemoc";
                    break;
                case 13:
                    nomEstacionArribo = "Del Golfo";
                    break;
                case 14:
                    nomEstacionArribo = "Felix U. Gomez";
                    break;
                case 15:
                    nomEstacionArribo = "Parque Fundidora";
                    break;
                case 16:
                    nomEstacionArribo = "Y Griega";
                    break;
                case 17:
                    nomEstacionArribo = "Eloy Cavazos";
                    break;
                case 18:
                    nomEstacionArribo = "Lerdo De Tejada";
                    break;
                case 19:
                    nomEstacionArribo = "Exposicion";
                    break;
            }
            switch (idEstacionDescenso) {
                case 1:
                    nomEstacionDescenso = "Sendero";
                    break;
                case 2:
                    nomEstacionDescenso = "Santiago Tapia";
                    break;
                case 3:
                    nomEstacionDescenso = "San Nicolas";
                    break;
                case 4:
                    nomEstacionDescenso = "Anahuac";
                    break;
                case 5:
                    nomEstacionDescenso = "Universidad";
                    break;
                case 6:
                    nomEstacionDescenso = "Niños Heroes";
                    break;
                case 7:
                    nomEstacionDescenso = "Regina";
                    break;
                case 8:
                    nomEstacionDescenso = "General Anaya";
                    break;
                case 9:
                    nomEstacionDescenso = "Cuauhtemoc";
                    break;
                case 10:
                    nomEstacionDescenso = "Alameda";
                    break;
                case 11:
                    nomEstacionDescenso = "Fundadores";
                    break;
                case 12:
                    nomEstacionDescenso = "Padre Mier";
                    break;
                case 13:
                    nomEstacionDescenso = "General Zaragoza";
                    break;
            }

            return "No. " + idUnico + " - Sube en: " + nomEstacionArribo + " | Baja en: " + nomEstacionDescenso ;
        }

    }
}