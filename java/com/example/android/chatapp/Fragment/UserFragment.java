package com.example.android.chatapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.chatapp.Adapter.UAdapter;
import com.example.android.chatapp.Model.User;
import com.example.android.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {

    RecyclerView recyclerView;
    UAdapter uAdapter;
    List<User> kUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_user,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        kUser=new ArrayList<>();



        return view;
    }

   private void readUser(){
       final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
       final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               kUser.clear();
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   User user = dataSnapshot.getValue(User.class);

                   assert user != null;
                   assert firebaseUser != null;
                   if (!user.getId().equals(firebaseUser.getUid())) {
                       kUser.add(user);
                   }
               }
               uAdapter=new UAdapter(getContext(),kUser);
               recyclerView.setAdapter(uAdapter);

           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }
}

