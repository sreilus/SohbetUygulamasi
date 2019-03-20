package com.example.muhammet.sohbetuygulamasi.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muhammet.sohbetuygulamasi.Adapters.UserAdapter;
import com.example.muhammet.sohbetuygulamasi.Models.Kullanicilar;
import com.example.muhammet.sohbetuygulamasi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AnaSayfaFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> userKeyList;
    RecyclerView userListRecyclerView;
    View view;
    UserAdapter userAdapter;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        tanimla();
        kullaniciGetir();
        return view;
    }
    public void tanimla()
    {
        userKeyList=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        userListRecyclerView=view.findViewById(R.id.userListRecyclerView);
        RecyclerView.LayoutManager mng=new GridLayoutManager(getContext(),2);
        userListRecyclerView.setLayoutManager(mng);
        userAdapter=new UserAdapter(userKeyList,getActivity(),getContext());
        userListRecyclerView.setAdapter(userAdapter);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }
    public void kullaniciGetir()
    {
        reference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                reference.child("Kullanicilar").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);

                        if (!kl.getIsim().equals("null") && !dataSnapshot.getKey().equals(user.getUid()))
                        {
                            if (userKeyList.indexOf(dataSnapshot.getKey())==-1)
                            {
                                userKeyList.add(dataSnapshot.getKey());
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
