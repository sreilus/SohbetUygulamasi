package com.example.muhammet.sohbetuygulamasi.Activity;

import android.content.Intent;
import android.icu.lang.UCharacterEnums;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammet.sohbetuygulamasi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GirisActivity extends AppCompatActivity {

    private EditText input_email_login, input_password_login;
    private Button loginButon;
    private FirebaseAuth auth;
    TextView kayitOlText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        tanimla();
    }

    public void tanimla() {
        input_email_login = findViewById(R.id.input_email_login);
        input_password_login = findViewById(R.id.input_password_login);
        loginButon = findViewById(R.id.loginButon);
        auth = FirebaseAuth.getInstance();
        kayitOlText=findViewById(R.id.kayitOlText);
        loginButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email_login.getText().toString();
                String pass = input_password_login.getText().toString();
                if (!email.equals("") && !pass.equals("")) {
                    sistemeGiris(email, pass);
                } else {
                    Toast.makeText(getApplicationContext(), "Boş Bırakmayın", Toast.LENGTH_LONG).show();
                }
            }
        });
        kayitOlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GirisActivity.this,KayitOlActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void sistemeGiris(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(GirisActivity.this, AnaActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Hatalı e-mail veya şifre girdiniz!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
