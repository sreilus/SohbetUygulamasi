package com.example.muhammet.sohbetuygulamasi.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhammet.sohbetuygulamasi.Models.Kullanicilar;
import com.example.muhammet.sohbetuygulamasi.R;
import com.example.muhammet.sohbetuygulamasi.Utils.ChangeFragment;
import com.example.muhammet.sohbetuygulamasi.Utils.RandomName;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class KullaniciProfilFragment extends Fragment {

    String yol="null";
    String imageUrl;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    View view;
    EditText kullaniciIsmi, input_egitim, input_dogumTarih, input_hakkinda;
    CircleImageView profile_image;
    Button bilgiGuncelleButon,bilgiIstekButon,bilgiArkadasButon;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_kullanici_profil, container, false);
        tanimla();
        bilgileriGetir();
        return view;
    }

    public void tanimla() {
        kullaniciIsmi = (EditText) view.findViewById(R.id.kullaniciIsmi);
        input_egitim = view.findViewById(R.id.input_egitim);
        input_dogumTarih = view.findViewById(R.id.input_dogumTarih);
        input_hakkinda = view.findViewById(R.id.input_hakkinda);
        profile_image = view.findViewById(R.id.profile_image);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        bilgiGuncelleButon = view.findViewById(R.id.bilgiGuncelleButon);
        bilgiIstekButon = view.findViewById(R.id.bilgiIstekButon);
        bilgiArkadasButon = view.findViewById(R.id.bilgiArkadasButon);
        bilgiGuncelleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanicilar").child(user.getUid());

        bilgiArkadasButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new ArkadaslarFragment());
            }
        });
        bilgiIstekButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new BildirimFragment());
            }
        });

    }

    private void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            final Uri filePath = data.getData();
            StorageReference ref = storageReference.child("kullaniciresimleri").child("ahmet4.jpg");
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Resim başarıyla güncellendi...", Toast.LENGTH_LONG).show();
                        String isim = kullaniciIsmi.getText().toString();
                        String egitim = input_egitim.getText().toString();
                        String dogum = input_dogumTarih.getText().toString();
                        String hakkimda = input_hakkinda.getText().toString();
                        reference = database.getReference().child("Kullanicilar").child(auth.getUid());
                        Map map = new HashMap();
                        map.put("isim", isim);
                        map.put("egitim", egitim);
                        map.put("dogumTarih", dogum);
                        map.put("hakkimda", hakkimda);
                       // map.put("resim",storageReference.getDownloadUrl().getResult().toString());
                        map.put("resim",Uri.)
                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ChangeFragment fragment = new ChangeFragment(getContext());
                                    fragment.change(new KullaniciProfilFragment());
                                    Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi...", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Bilgiler güncellenemedi...", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Resim güncellenemedi!...", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    } */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == 5) {
            Uri filePath = data.getData();
            final StorageReference ref = storageReference.child("kullaniciresimleri").child(RandomName.getSaltString()+".jpg");
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("asas", "onSuccess: uri= "+ uri.toString());
                            Toast.makeText(getContext(), "Resim Yükleme başarılı.", Toast.LENGTH_SHORT).show();
                            String isim = kullaniciIsmi.getText().toString();
                            String egitim = input_egitim.getText().toString();
                            String dogum = input_dogumTarih.getText().toString();
                            String hakkinda = input_hakkinda.getText().toString();

                            reference = database.getReference().child("Kullanicilar").child(user.getUid());
                            Map map = new HashMap();
                            map.put("isim", isim);
                            map.put("egitim", egitim);
                            map.put("dogumtarihi", dogum);
                            map.put("hakkinda", hakkinda);
                            map.put("resim",uri.toString());

                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Veriler Güncellendi..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public void bilgileriGetir() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);
                kullaniciIsmi.setText(kl.getIsim());
                input_egitim.setText(kl.getEgitim());
                input_dogumTarih.setText(kl.getDogumTarih());
                input_hakkinda.setText(kl.getHakkimda());
                imageUrl = kl.getResim();
                try{
                    if (!kl.getResim().equals("null")) {
                        Picasso.get().load(kl.getResim()).into(profile_image);
                    }
                }
                catch (Exception c)
                {
                  //  Toast.makeText(getContext(), "Resim Yüklenemedi...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void guncelle() {
        String isim = kullaniciIsmi.getText().toString();
        String egitim = input_egitim.getText().toString();
        String dogum = input_dogumTarih.getText().toString();
        String hakkimda = input_hakkinda.getText().toString();
        reference = database.getReference().child("Kullanicilar").child(auth.getUid());
        Map map = new HashMap();
        map.put("isim", isim);
        map.put("egitim", egitim);
        map.put("dogumTarih", dogum);
        map.put("hakkimda", hakkimda);
        if (imageUrl.equals("null")) {
            map.put("resim", "null");
        } else {
            map.put("resim", imageUrl);
        }
        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ChangeFragment fragment = new ChangeFragment(getContext());
                    fragment.change(new KullaniciProfilFragment());
                    Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Bilgiler güncellenemedi...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
