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

import com.example.muhammet.sohbetuygulamasi.Adapters.Friend_Req_Adapter;
import com.example.muhammet.sohbetuygulamasi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BildirimFragment extends Fragment {
    View view;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> friend_req_key_list;
    RecyclerView friend_req_recy;
    Friend_Req_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bildirim, container, false);
        tanimla();
        istekler();
        return view;
    }

    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Arkadaslik_Istek");
        friend_req_key_list = new ArrayList<>();
        friend_req_recy = view.findViewById(R.id.friend_req_recy);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        friend_req_recy.setLayoutManager(layoutManager);
        adapter = new Friend_Req_Adapter(friend_req_key_list, getActivity(), getContext());
        friend_req_recy.setAdapter(adapter);
    }

    public void istekler() {
        reference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String kontrol = dataSnapshot.child("tip").getValue().toString();
                    Log.i("logDene",kontrol);
                    if (kontrol.equals("alındı")) {
                        if (friend_req_key_list.indexOf(dataSnapshot.getKey())==-1) {
                            friend_req_key_list.add(dataSnapshot.getKey());
                        }
                        adapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                friend_req_key_list.remove(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
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
