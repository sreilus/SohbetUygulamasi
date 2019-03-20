package com.example.muhammet.sohbetuygulamasi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.muhammet.sohbetuygulamasi.Fragment.AnaSayfaFragment;
import com.example.muhammet.sohbetuygulamasi.Fragment.BildirimFragment;
import com.example.muhammet.sohbetuygulamasi.Fragment.KullaniciProfilFragment;
import com.example.muhammet.sohbetuygulamasi.R;
import com.example.muhammet.sohbetuygulamasi.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnaActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    String userId;

    private ChangeFragment changeFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment.change(new AnaSayfaFragment());
                    return true;
                case R.id.navigation_dashboard:
                    changeFragment.change(new BildirimFragment());
                    return true;
                case R.id.navigation_profil:
                    changeFragment.change(new KullaniciProfilFragment());
                    return true;
                case R.id.navigation_exit:
                    cik();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tanimla();
        kontrol();
        changeFragment = new ChangeFragment(AnaActivity.this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void tanimla()
    {
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        //userId=user.getUid().toString();
    }
    public void kontrol()
    {
        if (user==null)
        {
            Intent intent=new Intent(AnaActivity.this,GirisActivity.class);
            startActivity(intent);
            finish();//geri tuşuna basınca mainActivity'e gitmesini engeller
        }
        else
        {
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference reference=firebaseDatabase.getReference().child("Kullanicilar").child(user.getUid());
            reference.child("state").setValue(true);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        stateDegistir(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        stateDegistir(true);
    }

    public void cik()
    {

        auth.signOut();
        Intent intent=new Intent(AnaActivity.this,GirisActivity.class);
        startActivity(intent);
        finish();
        stateDegistir(false);
    }

    public void stateDegistir(Boolean state)
    {
        userId=user.getUid();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanicilar").child(userId);
        reference.child("state").setValue(state);
    }

}
