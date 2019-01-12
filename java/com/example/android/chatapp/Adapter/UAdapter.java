package com.example.android.chatapp.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.chatapp.Model.User;
import com.example.android.chatapp.R;

import java.util.List;

public class UAdapter extends RecyclerView.Adapter<UAdapter.ViewHolder> {

    private Context context;
    private List<User> mUser;

    public UAdapter(Context context, List<User> mUser) {
        this.context = context;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.user_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user= mUser.get(i);
        viewHolder.userName.setText(user.getUsername());
        if(user.getImageUrl().equals("default")){
            viewHolder.profileImg.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageUrl()).into(viewHolder.profileImg);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public ImageView profileImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.username);
            profileImg=itemView.findViewById(R.id.proImg);
        }
    }
}
