package com.example.metrobusmovilidad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.*;
import okhttp3.OkHttpClient;
import java.io.*;
import android.widget.Button;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.EditText;
import android.widget.Toast;

public class inicio extends AppCompatActivity {





    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    private static final String TAG = "Response";
    Button Entrar, postReq;
    EditText txtuser, txtpass;
    String user, pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        txtuser = (EditText) findViewById(R.id.TV_usu);
        txtpass = (EditText) findViewById(R.id.TV_pas);

        postReq = (Button)findViewById(R.id.btn_registrar);

        postReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    postRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    });
}


    public void postRequest() throws IOException {

        //user = null;
        //pass = null;
        user = txtuser.getText().toString();
        pass = txtpass.getText().toString();

        if (!user.equals("") && !pass.equals("")) {


            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            String url = "http://tslserver.ddns.net:5959/api/token";

            OkHttpClient client = new OkHttpClient();

            JSONObject postdata = new JSONObject();
            try {
                postdata.put("email", "francisco");
                postdata.put("password", "1234");
                //postdata.put("email", user);
                //postdata.put("password", pass);
            } catch (JSONException e) {
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

                    String mMessage = response.body().string();
                    //Log.e(TAG, "AQUI--------->" + mMessage);

                    if (!mMessage.contains("500"))
                    {
                        if (mMessage.contains("francisco")) {
                            Intent i = new Intent(inicio.this, menuagente.class);
                            startActivity(i);
                        }else{

                            Intent i = new Intent(inicio.this, estaciones.class);
                            startActivity(i);
                        }

                    }

                }
            });
        }else {
            Toast.makeText(this, "Debe ingresar un usuario valido", Toast.LENGTH_LONG).show();
        }

    }
}
