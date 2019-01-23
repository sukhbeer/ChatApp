package com.example.android.chatapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.chatapp2.Adapter.MessageAdapter;
import com.example.android.chatapp2.Model.Chat;
import com.example.android.chatapp2.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profileImg;
    TextView userName;
    FirebaseUser fUser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText editText;

    MessageAdapter messageAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView=findViewById(R.id.recyclerViewMsg);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager ll=new LinearLayoutManager(getApplicationContext());
        ll.setStackFromEnd(true);
        recyclerView.setLayoutManager(ll);

        profileImg=findViewById(R.id.userImage);
        userName=findViewById(R.id.userName);
        btn_send=findViewById(R.id.btn_Send);
        editText=findViewById(R.id.msgEditText);

        Toolbar toolbar=findViewById(R.id.msgToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent=getIntent();
        final String username=intent.getStringExtra("id");
        fUser= FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=editText.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fUser.getUid(),username,msg);
                }else{
                    Toast.makeText(MessageActivity.this,"You can't send empty message",Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
            }
        });
        reference= FirebaseDatabase.getInstance().getReference("Users").child(username);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                userName.setText(users.getName());
                profileImg.setImageResource(R.mipmap.ic_launcher);
                readMsg(fUser.getUid(),username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender,String receiver,String message){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMsg(final String currId, final String userId){
        chats=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat mChat=snapshot.getValue(Chat.class);
                    if(mChat.getReceiver().equals(currId) && mChat.getSender().equals(userId) ||
                            mChat.getReceiver().equals(userId) && mChat.getSender().equals(currId)){
                        chats.add(mChat);
                    }
                }
                messageAdapter=new MessageAdapter(MessageActivity.this,chats);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
