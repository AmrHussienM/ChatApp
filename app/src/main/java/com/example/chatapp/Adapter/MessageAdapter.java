package com.example.chatapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Model.Msg;
import com.example.chatapp.Model.User;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int Msg_Type_left=0;
    public static final int Msg_Type_Right=1;
    private List<Msg> listOfMessages;
    private Context context;
    private View view;
    private String imageUrl;
    private FirebaseUser fUser;

    public MessageAdapter(List<Msg> listOfMessages, Context context, String imageUrl) {
        this.listOfMessages = listOfMessages;
        this.context = context;
        this.imageUrl=imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==Msg_Type_Right) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, viewGroup, false);
            return new ViewHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, viewGroup, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Msg msg=listOfMessages.get(i);
        viewHolder.showMessage.setText(msg.getMessage());
        if (imageUrl==null){
            viewHolder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(imageUrl).into(viewHolder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        if (listOfMessages==null){return 0;}
        return listOfMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public ImageView profileImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage=itemView.findViewById(R.id.showMessage);
            profileImage=itemView.findViewById(R.id.profile_image_MessageChat);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if (listOfMessages.get(position).getSender().equals(fUser.getUid())){
            return Msg_Type_Right;
        }else {
            return Msg_Type_left;
        }
    }
}
