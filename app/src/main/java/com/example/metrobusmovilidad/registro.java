package com.example.metrobusmovilidad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class registro extends AppCompatActivity {

    //ImageView imagen;
    Button btnGuardar;
    FirebaseAuth fAuth;
    ProgressBar vLoadBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //imagen= (ImageView) findViewById(R.id.identificacion);
        btnGuardar=findViewById(R.id.btn_registrar);
        fAuth = FirebaseAuth.getInstance();
        vLoadBar = findViewById(R.id.loadBar);
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    guardaUsuario();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @SuppressLint("WrongViewCast")
    public void guardaUsuario() throws IOException{
        final TextView email,nombre,pass,telefono;
        String usuario,nombreUser,password, phone;

        email = findViewById(R.id.TV_usua);
        nombre = findViewById(R.id.TV_usu);
        pass = findViewById(R.id.TV_pas);
        telefono = findViewById(R.id.TV_num);

        usuario = email.getText().toString();
        nombreUser = nombre.getText().toString();
        password = pass.getText().toString();
        phone = telefono.getText().toString();

        if (TextUtils.isEmpty(usuario)){
            email.setError("Email es requerido");
            return;
        }
        if (TextUtils.isEmpty(password)){
            pass.setError("Contraseña es requerida");
            return;
        }
        if (TextUtils.isEmpty(nombreUser)){
            nombre.setError("Nombre es requerido");
            return;
        }
        if (password.length() < 6){
            pass.setError("La contraseña debe contener almenos 6 caracteres");
            return;
        }
        if (phone.length() != 10){
            pass.setError("El telefono debe de tener 10 caracteres");
            return;
        }

        vLoadBar.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(usuario,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(registro.this , "Usuario creado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(registro.this , "Ocurrio un error!! - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
//        MediaType MEDIA_TYPE = MediaType.parse("application/json");
//        String url = "http://tslserver.ddns.net:5959/api/customer";
//
//        OkHttpClient client = new OkHttpClient();
//
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("email", usuario);
//            postdata.put("firstName", nombreUser);
//            postdata.put("lastName", "");
//            postdata.put("roles", "usuario");
//            postdata.put("password", password);
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
//                String mMessage = response.body().string();
//                //Log.e(TAG, "AQUI--------->" + mMessage);
//
//
//                registro.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(registro.this, "Se grabó el registro correctamente", Toast.LENGTH_LONG).show();
//                        email.setText("");
//                        nombre.setText("");
//                        pass.setText("");
//                        telefono.setText("");
//
//
//                    }
//                });
//            }
//        });


    }




   /* public void onClick(View view) {
        cargarImagen ();
    }

    private void cargarImagen() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    intent.setType("image/");
    startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            imagen.setImageURI(path);

        }

    }*/
}
