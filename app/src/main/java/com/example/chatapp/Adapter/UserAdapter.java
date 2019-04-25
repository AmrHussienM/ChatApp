package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.MessageActivity;
import com.example.chatapp.Model.Msg;
import com.example.chatapp.Model.User;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> listOfUsers;
    private Context context;
    private String  isChat;
    private String theLastMessage;

    public UserAdapter(List<User> listOfUsers, Context context,String isChat) {
        this.listOfUsers = listOfUsers;
        this.context = context;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user=listOfUsers.get(i);
        viewHolder.userName.setText(user.getUsername());
        if (user.getImageURL()==null){
            viewHolder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(user.getImageURL()).into(viewHolder.profileImage);
        }

        if (isChat.equals("online")){
            LastMessage(user.getId(),viewHolder.lastMessage);
        }else {
            viewHolder.lastMessage.setVisibility(View.GONE);
        }

        if (isChat.equals("online")){
            if (user.getStatus().equals("online")){
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            }
            else{
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
            }

        }else {
            viewHolder.img_on.setVisibility(View.GONE);
            viewHolder.img_off.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listOfUsers==null){return 0;}
        return listOfUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView profileImage;
        private ImageView img_on;
        private ImageView img_off;
        private TextView lastMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            userName=itemView.findViewById(R.id.username_user);
            profileImage=itemView.findViewById(R.id.profile_image_user);
            img_on=itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);
        }
    }

    //check the last Message
    private void LastMessage(final String userid, final TextView lastMessage){
        theLastMessage="default";

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Msg msg=snapshot.getValue(Msg.class);
                    if (msg.getReceiver().equals(firebaseUser.getUid()) && msg.getSender().equals(userid) ||
                    msg.getReceiver().equals(userid) && msg.getSender().equals(firebaseUser.getUid())){

                        theLastMessage=msg.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        lastMessage.setText("No Message");
                        break;

                    default:
                        lastMessage.setText(theLastMessage);
                        break;
                }

                theLastMessage="default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
