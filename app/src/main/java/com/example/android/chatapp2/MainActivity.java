package com.example.android.chatapp2;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.chatapp2.Adapter.SectionPageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.example.android.chatapp2.R.*;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    FirebaseUser currentUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        toolbar=findViewById(id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat App");

        viewPager=findViewById(R.id.mainTabPager);
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sectionPageAdapter);
        tabLayout=findViewById(id.mainTab);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onStart() {
        super.onStart();

         currentUser=mAuth.getCurrentUser();

        if(currentUser==null){
            sendToStart();
        }
    }

    private void sendToStart(){
        Intent intent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==id.logoutBtn){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        if(item.getItemId()==id.accSetBtn){
            Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void status(String status){
        reference= FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        HashMap<String, Object>hashMap=new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
