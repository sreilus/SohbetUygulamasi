package com.example.muhammet.sohbetuygulamasi.Activity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity {

    EditText input_layout_email,input_layout_password;
    Button registerButon;
    FirebaseAuth auth;
    TextView hesapVarText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,referenceKayit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        setTitle("Hello StackOverFlow!");
        tanimla();
    }

    public void tanimla()
    {
        input_layout_email=(EditText) findViewById(R.id.input_email);
        input_layout_password=(EditText) findViewById(R.id.input_password);
        registerButon=findViewById(R.id.registerButon);
        hesapVarText=findViewById(R.id.hesapVarText);
        auth=FirebaseAuth.getInstance();
        registerButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=input_layout_email.getText().toString();
                String pass=input_layout_password.getText().toString();
                if(!email.equals("")&&!pass.equals(""))
                {
                    input_layout_email.setText("");
                    input_layout_password.setText("");
                    kayitOl(email,pass);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Bilgileri Boş Giremezsiniz!",Toast.LENGTH_LONG).show();
                }
            }
        });
        hesapVarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KayitOlActivity.this,GirisActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void kayitOl(final String email,final String pass)
    {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    reference=firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());
                    Map map=new HashMap();
                    map.put("resim","null");
                    map.put("isim","null");
                    map.put("egitim","null");
                    map.put("dogumTarih","null");
                    map.put("hakkimda","null");
                    map.put("state",false);
                    reference.setValue(map);
                    referenceKayit=firebaseDatabase.getReference().child("KullaniciKayit").child(auth.getUid());
                    Map mapKayit=new HashMap();
                    mapKayit.put("mail",email);
                    mapKayit.put("pass",pass);
                    referenceKayit.setValue(mapKayit);
                    Intent intent=new Intent(KayitOlActivity.this,AnaActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Kayıt olma esnasında bir problem yaşandı!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
