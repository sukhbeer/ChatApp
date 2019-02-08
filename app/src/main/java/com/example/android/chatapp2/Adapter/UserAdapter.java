package com.example.android.chatapp2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.chatapp2.MessageActivity;
import com.example.android.chatapp2.R;
import com.example.android.chatapp2.Model.Users;
import com.squareup.picasso.Picasso;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> user;
    private boolean isChat;

    public UserAdapter(Context context, List<Users> user, boolean isChat) {
        this.context = context;
        this.user = user;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_list,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Users user1=user.get(i);
        viewHolder.mName.setText(user1.getName());
        if(user1.getImage().equals("default")){
            viewHolder.mImage.setImageResource(R.mipmap.ic_launcher);
        }else {
            Picasso.get().load(user1.getImage()).into(viewHolder.mImage);
        }

        if(isChat){
            if(user1.getStatus().equals("online")){
                viewHolder.uStatus.setVisibility(View.VISIBLE);
                viewHolder.uStatus.setVisibility(View.GONE);
            }
            else{
                viewHolder.uStatus.setVisibility(View.GONE);
                viewHolder.uStatus.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MessageActivity.class);
                intent.putExtra("id",user1.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName;
        ImageView mImage;
        ImageView uStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.txtUser);
            mImage=itemView.findViewById(R.id.imgUser);
            uStatus=itemView.findViewById(R.id.userStatus);
        }
    }

}
