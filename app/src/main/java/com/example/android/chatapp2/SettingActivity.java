package com.example.android.chatapp2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {


    private static final int GALLERY_PICK = 1;
    CircleImageView circleImageView;
    TextView mName;
    Button img_Btn;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private StorageReference img_storageRef;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        circleImageView = findViewById(R.id.disImg);
        mName = findViewById(R.id.disName);
        img_Btn = findViewById(R.id.imgBtn);

        toolbar=findViewById(R.id.sToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_storageRef = FirebaseStorage.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                final String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                mName.setText(name);

                if(image.equals("default")){
                    circleImageView.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).into(circleImageView);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        img_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUrl = data.getData();

            CropImage.activity(imageUrl)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                String currentUser_id = firebaseUser.getUid();
                final StorageReference ref = img_storageRef.child("profile_img").child(currentUser_id + ".jpg");
                StorageTask uploadTask = ref.putFile(resultUri);

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String mUrl = downloadUri.toString();
                            reference.child("image").setValue(mUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SettingActivity.this, "Image Loaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SettingActivity.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                if (result != null) {
                    Exception error = result.getError();
                }
            }
        }
    }
}
