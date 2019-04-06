package com.example.muhammet.sohbetuygulamasi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammet.sohbetuygulamasi.Activity.ChatActivity;
import com.example.muhammet.sohbetuygulamasi.Models.Kullanicilar;
import com.example.muhammet.sohbetuygulamasi.R;
import com.example.muhammet.sohbetuygulamasi.Utils.ShowToastMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherProfileFragment extends Fragment {
    View view;
    String otherId,userId,kontrol="",begeniKontrol="";
    TextView userProfileNameText,userProfilEgitimText,txtBegeni,txtArkadasIstek,userProfileDogumText,userProfileHakkindaText,userProfileTakipText,userProfileArkadasText,userProfileNameText2;
    ImageView userProfileArkadasImage,userProfileMesajImage,userProfileTakipImage;
    CircleImageView userProfileProfileImage;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference,reference_Arkadaslik;
    ShowToastMessage showToastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        tanimla();
        action();
        return view;
    }

    public void tanimla() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userId=user.getUid();
        reference=firebaseDatabase.getReference();

        otherId=getArguments().getString("userid");
        reference_Arkadaslik=firebaseDatabase.getReference().child("Arkadaslik_Istek");
        userProfileNameText=(TextView)view.findViewById(R.id.userProfileNameText);
        userProfileNameText2=(TextView)view.findViewById(R.id.userProfileNameText2);
        userProfilEgitimText=(TextView)view.findViewById(R.id.userProfilEgitimText);
        userProfileDogumText=(TextView)view.findViewById(R.id.userProfileDogumText);
        userProfileHakkindaText=(TextView)view.findViewById(R.id.userProfileHakkindaText);
        userProfileTakipText=(TextView)view.findViewById(R.id.userProfileTakipText);
        userProfileArkadasText=(TextView)view.findViewById(R.id.userProfileArkadasText);
        userProfileArkadasImage=(ImageView)view.findViewById(R.id.userProfileArkadasImage);
        userProfileMesajImage=(ImageView)view.findViewById(R.id.userProfileMesajImage);
        userProfileTakipImage=(ImageView)view.findViewById(R.id.userProfileTakipImage);
        userProfileProfileImage=(CircleImageView)view.findViewById(R.id.userProfileProfileImage);
        txtArkadasIstek=view.findViewById(R.id.txtArkadasIstek);
        txtBegeni=view.findViewById(R.id.txtBegeni);

        getBegeniText();
        getArkadasText();

        reference_Arkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId))
                {
                    kontrol="istek";
                    userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle_off);
                }
                else
                {
                    userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle_on);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Arkadaslar").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(otherId))
                {
                    kontrol="arkadas";
                    userProfileArkadasImage.setImageResource(R.drawable.deletinguser);
                    txtArkadasIstek.setText("Arkadaşlıktan Çıkar");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId    ))
                {
                    begeniKontrol="begendi";
                    userProfileTakipImage.setImageResource(R.drawable.takip_off);
                    txtBegeni.setText("Begeniyi Kaldır");
                }
                else
                {
                    userProfileTakipImage.setImageResource(R.drawable.takip_ok);
                    txtBegeni.setText("Begen");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showToastMessage=new ShowToastMessage(getContext());
        userProfileMesajImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userName",userProfileNameText2.getText().toString());
                intent.putExtra("id",otherId);
                startActivity(intent);
                stateDegistir(true);
            }
        });

    }

    private void arkadasTablosundanCikar(final String otherId,final String userId) {
        reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle_on);
                        showToastMessage.showToast("Arkadaşlıktan Çıkarıldı!");
                        txtArkadasIstek.setText("Arkadaş Ekle");
                        getArkadasText();
                    }
                });
            }
        });
    }

    public void action()
    {
        reference.child("Kullanicilar").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanicilar kl=dataSnapshot.getValue(Kullanicilar.class);
                if (!kl.getIsim().equals("null"))
                {
                    userProfileNameText.setText("İsim : "+kl.getIsim());
                }
                if (!kl.getIsim().equals("null"))
                {
                    userProfileNameText2.setText(kl.getIsim());
                }
                if (!kl.getEgitim().equals("null"))
                {
                    userProfilEgitimText.setText("Eğitim : "+kl.getEgitim());
                }
                if (!kl.getDogumTarih().equals("null"))
                {
                    userProfileDogumText.setText("Doğum Tarihi : "+kl.getDogumTarih());
                }
                if (!kl.getHakkimda().equals("null"))
                {
                    userProfileHakkindaText.setText("Hakkımda : "+kl.getHakkimda());
                }
                try {
                    if (!kl.getResim().equals("null"))
                    {
                        Picasso.get().load(kl.getResim()).into(userProfileProfileImage);
                    }

                }catch (Exception e)
                {
                    showToastMessage.showToast("Resim Yüklenemedi!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userProfileArkadasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("arkadas",kontrol);
                if (kontrol.equals("istek"))
                {
                    arkadasIptalEt(otherId,userId);
                }
                else if (kontrol.equals("arkadas"))
                {
                    arkadasTablosundanCikar(otherId,userId);
                }
                else
                {
                    arkadasEkle(otherId,userId);
                }
            }
        });

        userProfileTakipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("begenmek",begeniKontrol);
                if (begeniKontrol.equals("begendi"))
                {
                    begeniIptal(userId,otherId);
                }
                else
                {
                    begen(userId,otherId);
                }
            }
        });
    }

    private void begeniIptal(String userId, String otherId) {
        reference.child("Begeniler").child(otherId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userProfileTakipImage.setImageResource(R.drawable.takip_ok);
                begeniKontrol="";
                showToastMessage.showToast("Beğenmekten Vazgeçildi");
                txtBegeni.setText("Beğen");
                getBegeniText();
            }
        });
    }

    public void arkadasEkle(final String otherId, final String userId)
    {
        reference_Arkadaslik.child(userId).child(otherId).child("tip").setValue("gonderme").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    reference_Arkadaslik.child(otherId).child(userId).child("tip").setValue("alındı").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                kontrol="istek";
                                showToastMessage.showToast("Arkadaşlık isteği başarıyla gönderildi");
                                txtArkadasIstek.setText("İsteği İptal Et");
                                userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle_off);
                            }
                            else
                            {
                                showToastMessage.showToast("Bir sorun oluştu!");
                            }
                        }
                    });
                }
                else
                {
                    showToastMessage.showToast("Bir sorun oluştu!");
                }
            }
        });
    }

    public void arkadasIptalEt(final String otherId, final String userId)
    {
        reference_Arkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference_Arkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfileArkadasImage.setImageResource(R.drawable.arkadas_ekle_on);
                        showToastMessage.showToast("Arkadaşlık isteği iptal edildi");
                        txtArkadasIstek.setText("Arkadaş Ekle");
                        getArkadasText();
                    }
                });
            }
        });
    }

    public void begen (String userId,String otherId)
    {
        reference.child("Begeniler").child(otherId).child(userId).child("tip").setValue("begendi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        showToastMessage.showToast("Profili beğendiniz");
                        userProfileTakipImage.setImageResource(R.drawable.takip_off);
                        txtBegeni.setText("Beğenmekten Vazgeç");
                        begeniKontrol="begendi";
                        getBegeniText();
                    }
            }
        });
    }

    public void getBegeniText()
    {
        //getChildrenCount için addListenerForSingleValueEvent kullanılması önerilir.
        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfileTakipText.setText(dataSnapshot.getChildrenCount()+" Beğeni");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getArkadasText()
    {
        //getChildrenCount için addListenerForSingleValueEvent kullanılması önerilir.
        reference.child("Arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfileArkadasText.setText(dataSnapshot.getChildrenCount()+" Arkadaş");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void stateDegistir(Boolean state)
    {
        userId=user.getUid();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference();
        reference.child("Kullanicilar").child(userId).child("state").setValue(state);
        if (state==false)
        {
            String strDate="dsfssd";
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            strDate=dateFormat.format(date);
            reference.child("Kullanicilar").child(userId).child("last_seen").setValue(strDate);
        }
    }
}
