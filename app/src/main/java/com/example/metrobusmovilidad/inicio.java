package com.example.metrobusmovilidad;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import okhttp3.*;
import okhttp3.OkHttpClient;
import java.io.*;
import java.util.HashMap;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class inicio extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    private static final String TAG = "Response";
    Button Entrar, postReq;
    EditText txtuser, txtpass;
    String pass, user;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser authUser = fAuth.getCurrentUser();
//        if (authUser != null) {
//            redirect();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        txtuser = (EditText) findViewById(R.id.TV_usu);
        txtpass = (EditText) findViewById(R.id.TV_pas);
        progressBar = findViewById(R.id.progressBar);
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
        fAuth = FirebaseAuth.getInstance();
        //user = null;
        //pass = null;
        user = txtuser.getText().toString();
        pass = txtpass.getText().toString();
        if (TextUtils.isEmpty(user)){
            txtuser.setError("Email es requerido");
            return;
        }
        if (TextUtils.isEmpty(pass)){
            txtpass.setError("Contraseña es requerida");
            return;
        }
        if (pass.length() < 6){
            txtpass.setError("La contraseña debe contener almenos 6 caracteres");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        fAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(inicio.this , "Session Iniciada!", Toast.LENGTH_SHORT).show();
                    String authUser = fAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference usersCollection = db.getReference().child("Users").child(authUser);

                    usersCollection.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Model_usuario schema_user = snapshot.getValue(Model_usuario.class);
                            String userType = schema_user.getType();
                            if (userType.equals("Admin")) {
                                startActivity(new Intent(getApplicationContext(), menuagente.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), estaciones.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled: "+ error);
                        }
                    });
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(inicio.this , "Ocurrio un error!! - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
//            MediaType MEDIA_TYPE = MediaType.parse("application/json");
//            String url = "http://tslserver.ddns.net:5959/api/token";
//
//            OkHttpClient client = new OkHttpClient();
//
//            JSONObject postdata = new JSONObject();
//            try {
//                postdata.put("email", "francisco");
//                postdata.put("password", "1234");
//                //postdata.put("email", user);
//                //postdata.put("password", pass);
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .header("Accept", "application/json")
//                    .header("Content-Type", "application/json")
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    String mMessage = e.getMessage().toString();
//                    Log.w("failure Response", mMessage);
//                    //cuando falla el apoi
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                    String mMessage = response.body().string();
//                    //Log.e(TAG, "AQUI--------->" + mMessage);
//
//                    if (!mMessage.contains("500"))
//                    {
//                        if (mMessage.contains("francisco")) {
//                            Intent i = new Intent(inicio.this, menuagente.class);
//                            startActivity(i);
//                        }else{
//
//                            Intent i = new Intent(inicio.this, estaciones.class);
//                            startActivity(i);
//                        }
//
//                    }
//
//                }
//            });

    }

    public void redirect(){

    }
}
