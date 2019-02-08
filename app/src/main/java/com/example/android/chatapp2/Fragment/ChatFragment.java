package com.example.android.chatapp2.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.chatapp2.Adapter.UserAdapter;
import com.example.android.chatapp2.Model.Chat;
import com.example.android.chatapp2.Model.ChatId;
import com.example.android.chatapp2.Model.Users;
import com.example.android.chatapp2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> mUser;

    FirebaseUser fUser;
    DatabaseReference reference;

    private List<ChatId> userList;

    public ChatFragment() {
        // Required empty public constructor
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_chat,container,false);
        recyclerView =view.findViewById(R.id.recyclerViewChat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        userList= new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("ChatId").child(fUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatId chatId=snapshot.getValue(ChatId.class);
                    userList.add(chatId);
                }
                chat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void chat(){
        mUser=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users=snapshot.getValue(Users.class);
                    for(ChatId chatId : userList){
                        if(users.getId().equals(chatId.getId())){
                            mUser.add(users);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),mUser,false);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
