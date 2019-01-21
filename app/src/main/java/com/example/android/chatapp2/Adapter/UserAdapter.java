package com.example.android.chatapp2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.chatapp2.R;
import com.example.android.chatapp2.Users;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> user;

    public UserAdapter(Context context, List<Users> user) {
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_list,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Users user1=user.get(i);
        viewHolder.mName.setText(user1.getName());
        viewHolder.mImage.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName;
        ImageView mImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.txtUser);
            mImage=itemView.findViewById(R.id.imgUser);
        }
    }

}
