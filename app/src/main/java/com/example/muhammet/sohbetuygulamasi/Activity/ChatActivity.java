package com.example.muhammet.sohbetuygulamasi.Activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.muhammet.sohbetuygulamasi.Adapters.MessageAdapter;
import com.example.muhammet.sohbetuygulamasi.Fragment.OtherProfileFragment;
import com.example.muhammet.sohbetuygulamasi.Models.MessageModel;
import com.example.muhammet.sohbetuygulamasi.R;
import com.example.muhammet.sohbetuygulamasi.Utils.ChangeFragment;
import com.example.muhammet.sohbetuygulamasi.Utils.GetDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    TextView chat_username_textview;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    FloatingActionButton sendMessageButton;
    EditText messageTextEdittext;
    List<MessageModel> messageModelList;
    RecyclerView chat_recy_view;
    MessageAdapter messageAdapter;
    List<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tanimla();
        action();
        loadMessage();
    }

    public String getUserName() {
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("userName");
        return userName;
    }

    public String getOtherId() {
        String id = getIntent().getExtras().getString("id").toString();
        return id;
    }

    public void tanimla() {
        chat_username_textview = findViewById(R.id.chat_username_textview);
        chat_username_textview.setText(getUserName());
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        sendMessageButton = (FloatingActionButton) findViewById(R.id.sendMessageButton);
        messageTextEdittext = findViewById(R.id.messageTextEdittext);
        keyList=new ArrayList<>();
        messageModelList=new ArrayList<>();
        chat_recy_view=(RecyclerView)findViewById(R.id.chat_recy_view);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(ChatActivity.this,1);
        chat_recy_view.setLayoutManager(layoutManager);
        messageAdapter=new MessageAdapter(keyList,ChatActivity.this,ChatActivity.this,messageModelList);
        chat_recy_view.setAdapter(messageAdapter);
    }

    public void action() {
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = messageTextEdittext.getText().toString();
                sendMessage(firebaseUser.getUid(), getOtherId(), "text", GetDate.getDate(), false, message);
                messageTextEdittext.setText("");
            }
        });


    }


    public void sendMessage(final String userId, final String otherId, String textType, String date, Boolean seen, String messageText) {
        final String mesajId = reference.child("Mesajlar").child(userId).child(otherId).push().getKey();

        final Map messageMap = new HashMap();
        messageMap.put("type", textType);
        messageMap.put("seen", seen);
        messageMap.put("time", date);
        messageMap.put("text", messageText);
        messageMap.put("from", userId);

        reference.child("Mesajlar").child(userId).child(otherId).child(mesajId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Mesajlar").child(otherId).child(userId).child(mesajId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }

    public void loadMessage()
    {
        reference.child("Mesajlar").child(firebaseUser.getUid()).child(getOtherId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessageModel messageModel=dataSnapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                keyList.add(dataSnapshot.getKey());
                chat_recy_view.scrollToPosition(messageModelList.size()-1);
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
