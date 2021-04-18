package com.example.metrobusmovilidad;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class menuagente extends AppCompatActivity {


    Button btn1,btnSol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuagente);
        btn1 = findViewById(R.id.btn_registro);
        btnSol= findViewById(R.id.btn_solicitantes);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
                Intent btn1 = new Intent(menuagente.this, registro.class);
                startActivity(btn1);
            }
        });

        btnSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnSol = new Intent(menuagente.this,solicitudes.class);
                startActivity(btnSol);
            }
        });

    }
}
