package com.example.muhammet.sohbetuygulamasi.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.muhammet.sohbetuygulamasi.Fragment.OtherProfileFragment;
import com.example.muhammet.sohbetuygulamasi.Models.Kullanicilar;
import com.example.muhammet.sohbetuygulamasi.R;
import com.example.muhammet.sohbetuygulamasi.Utils.ChangeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<String> userKeyList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public UserAdapter(List<String> userKeyList, Activity activity, Context context) {
        this.userKeyList = userKeyList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }


    //layout tanımlama yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userlayout, parent, false);
        return new ViewHolder(view);
    }

    //viewlara setlemeler yapılacak
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
        // holder.usernameTextView.setText(userKeyList.get(position).toString());
        reference.child("Kullanicilar").child(userKeyList.get(position).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanicilar kl = dataSnapshot.getValue(Kullanicilar.class);

                Picasso.get().load(kl.getResim()).into(holder.userimage);
                holder.usernameTextView.setText(kl.getIsim());
                Boolean stateUser = Boolean.parseBoolean(dataSnapshot.child("state").getValue().toString());
                if (stateUser==true)
                {
                    holder.user_state_img.setImageResource(R.drawable.online_icon);
                }
                else
                {
                    holder.user_state_img.setImageResource(R.drawable.offline_icon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        holder.userAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment fragment = new ChangeFragment(context);
                fragment.changeWithParameter(new OtherProfileFragment(), userKeyList.get(position));
            }
        });

    }

    //adapterı oluşturulacak listenin size'ı
    @Override
    public int getItemCount() {
        return userKeyList.size();
    }

    //viewların tanımlama işlemleri yapılacak
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        CircleImageView userimage, user_state_img;
        LinearLayout userAnaLayout;

        ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.usernameTextView);
            userimage = (CircleImageView) itemView.findViewById(R.id.userimage);
            userAnaLayout = (LinearLayout) itemView.findViewById(R.id.userAnaLayout);
            user_state_img = (CircleImageView) itemView.findViewById(R.id.user_state_img);
        }
    }
}