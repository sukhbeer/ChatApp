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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.chatapp2.Adapter.MessageAdapter;
import com.example.android.chatapp2.Model.Chat;
import com.example.android.chatapp2.Model.Users;
import com.example.android.chatapp2.Notification.API;
import com.example.android.chatapp2.Notification.Client;
import com.example.android.chatapp2.Notification.Data;
import com.example.android.chatapp2.Notification.Response;
import com.example.android.chatapp2.Notification.Sender;
import com.example.android.chatapp2.Notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profileImg;
    TextView userName;
    FirebaseUser fUser;
    DatabaseReference reference;

    ScrollView scrollView;
    ImageButton btn_send;
    EditText editText;

    MessageAdapter messageAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    Intent intent;

    API api;

    boolean notify = false;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        recyclerView = findViewById(R.id.recyclerViewMsg);
        LinearLayoutManager ll = new LinearLayoutManager(getApplicationContext());
        ll.setStackFromEnd(true);
        recyclerView.setLayoutManager(ll);


        profileImg = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        btn_send = findViewById(R.id.btn_Send);
        editText = findViewById(R.id.msgEditText);
        scrollView=findViewById(R.id.scrollView);

        Toolbar toolbar = findViewById(R.id.msgToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        api = Client.getClient("https://fcm.googleapis.com/").create(API.class);

        intent = getIntent();
        userid = intent.getStringExtra("id");
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify=true;
                String msg = editText.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fUser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                editText.setText("");

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                userName.setText(users.getName());
                if (users.getImage().equals("default")) {
                    profileImg.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(users.getImage()).into(profileImg);
                }
                readMsg(fUser.getUid(), userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void sendMessage(String sender, final String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatId")
                .child(fUser.getUid())
                .child(receiver);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        reference.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users =  dataSnapshot.getValue(Users.class);
                if (notify) {
                    sendNotificatioiin(receiver,users.getName(),msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));

    }

    private void sendNotificatioiin(String reciver, final String username, final String message){
        final DatabaseReference token=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = token.orderByKey().equalTo(reciver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token tokenM = snapshot.getValue(Token.class);
                    Data data = new Data(fUser.getUid(),R.mipmap.ic_launcher,username+": "+message,"New Message",
                            userid);

                    Sender sender = new Sender(data,tokenM.getToken());

                    api.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    if(response.code()==200){
                                        if(response.body().success !=1){
                                            Toast.makeText(MessageActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMsg(final String currId, final String userId) {
        chats = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat mChat = snapshot.getValue(Chat.class);
                    if (mChat.getReceiver().equals(currId) && mChat.getSender().equals(userId) ||
                            mChat.getReceiver().equals(userId) && mChat.getSender().equals(currId)) {
                        chats.add(mChat);
                        messageAdapter = new MessageAdapter(MessageActivity.this, chats);
                        recyclerView.setAdapter(messageAdapter);

                        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
