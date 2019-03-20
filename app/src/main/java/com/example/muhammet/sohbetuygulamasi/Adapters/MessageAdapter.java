package com.example.muhammet.sohbetuygulamasi.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammet.sohbetuygulamasi.Models.MessageModel;
import com.example.muhammet.sohbetuygulamasi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<String> userKeyList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userId;
    List<MessageModel> messageModelList;
    Boolean state;
    int view_type_sent = 1, view_type_recieved = 2;

    public MessageAdapter(List<String> userKeyList, Activity activity, Context context, List<MessageModel> messageModelList) {
        this.userKeyList = userKeyList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();
        this.messageModelList = messageModelList;
        state = false;
    }


    //layout tanımlama yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == view_type_sent) {
            view = LayoutInflater.from(context).inflate(R.layout.message_sent_layout, parent, false);
            return new ViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(context).inflate(R.layout.message_received_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    //viewlara setlemeler yapılacak
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
        holder.messageText.setText(messageModelList.get(position).getText());
        holder.txtMsgDate.setText(messageModelList.get(position).getTime().substring(11,16));
    }


    //adapterı oluşturulacak listenin size'ı
    @Override
    public int getItemCount() {
        return messageModelList.size();
    }


    //viewların tanımlama işlemleri yapılacak
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView txtMsgDate;
        CircleImageView friend_state_img, friend_image;

        ViewHolder(View itemView) {
            super(itemView);
            if (state==true)
            {
                messageText = (TextView) itemView.findViewById(R.id.message_send_text);
                txtMsgDate=(TextView) itemView.findViewById(R.id.txtMsgDate_sent);
            }
            else
            {
                messageText = (TextView) itemView.findViewById(R.id.message_received_text);
                txtMsgDate=(TextView) itemView.findViewById(R.id.txtMsgDate_receive);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.i("id",userId);
        if (messageModelList.get(position).getFrom().equals(userId)) {
            state = true;
            return view_type_sent;
        } else {
            state = false;
            return view_type_recieved;
        }
    }
}