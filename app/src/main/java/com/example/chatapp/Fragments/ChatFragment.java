package com.example.chatapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.Adapter.UserAdapter;
import com.example.chatapp.Model.Chatlist;
import com.example.chatapp.Model.User;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private FirebaseUser fUser;
    private UserAdapter adapter;
    private List<User> mUsers;
    private List<Chatlist> usersList;
    private DatabaseReference reference;
    private LinearLayoutManager linearLayoutManager;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        fUser= FirebaseAuth.getInstance().getCurrentUser();
        usersList=new ArrayList<Chatlist>();

        reference=FirebaseDatabase.getInstance().getReference("Chatlist").child(fUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chatlist chatlist=snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);

                }
                chatlist();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//Delete this also
/*
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Msg msg=snapshot.getValue(Msg.class);
                    assert msg != null;
                    if (msg.getSender().equals(fUser.getUid())){
                        usersList.add(msg.getReceiver());
                    }
                    if (msg.getReceiver().equals(fUser.getUid())){
                        usersList.add(msg.getSender());
                    }
                }


                //readChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/

        return view;
    }

    private void chatlist()
    {
        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    for (Chatlist chatlist:usersList){
                        if (user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                adapter=new UserAdapter(mUsers, getActivity(),"online");
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//Delete readChat Method...
/*
    private void readChats()
    {
        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    for (String id:usersList){
                        assert user != null;
                        if (user.getId().equals(id)){
                            if (mUsers.size() != 0){
                                for (User user1 : mUsers){
                                    if (!user.getId().equals(user1.getId())){
                                        mUsers.add(user);
                                    }
                                }
                            }else {
                                mUsers.add(user);
                            }
                        }
                    }
                }

                adapter=new UserAdapter(mUsers,getContext(),"online");
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
*/


}
