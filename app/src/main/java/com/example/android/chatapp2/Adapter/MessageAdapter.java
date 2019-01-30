package com.example.android.chatapp2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.chatapp2.Model.Chat;
import com.example.android.chatapp2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;

    private Context context;
    private List<Chat> chat;

    public MessageAdapter(Context context, List<Chat> chat) {
        this.context = context;
        this.chat = chat;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==MSG_TYPE_RIGHT){
        View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,viewGroup,false);
        return new MessageAdapter.ViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {
       Chat mChat=chat.get(i);
       viewHolder.showMessage.setText(mChat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView showMessage;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage=itemView.findViewById(R.id.showMsg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chat.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
           return MSG_TYPE_LEFT;
        }
    }
}

