package com.sorar.myapplication.dao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sorar.myapplication.activity.ChatRoomActivity;
import com.sorar.myapplication.adapter.ChatRoomAdapter;
import com.sorar.myapplication.model.ChatRoom;

import java.util.ArrayList;

public class DaoChatRoomController implements ValueEventListener {
    private final Context context;
    private final ChatRoomActivity chatRoomActivity;
    private RecyclerView recyclerView;
    private DatabaseReference myRef ;
    private RecyclerView.Adapter adapter;
    private boolean firstLoad;
    private ArrayList<ChatRoom> chatRooms =new ArrayList<>();

    public DaoChatRoomController(RecyclerView recyclerView, Context context){
        this.context=context;
        this.recyclerView=recyclerView;
        this.chatRoomActivity =(ChatRoomActivity)context;
        firstLoad=true;
        recyclerView.setAdapter(adapter);
        myRef= FirebaseDatabase.getInstance().getReference().child("Chat Rooms");
        myRef.addValueEventListener(this);
        Toast.makeText(context,"Loading..",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot!=null){
            chatRooms.clear();
            ChatRoom chatRoom;
            String password,creator, creatorID,title;
            for(DataSnapshot chatRoomFB:dataSnapshot.getChildren()){
                if(chatRoomFB.child("Creator").exists()&&chatRoomFB.child("CreatorID").exists()){
                    title=chatRoomFB.getKey();
                    creator=chatRoomFB.child("Creator").getValue(String.class);
                    creatorID=chatRoomFB.child("CreatorID").getValue(String.class);

                    if(chatRoomFB.child("Password").exists()) {
                        password=chatRoomFB.child("Password").getValue(String.class);
                        chatRoom = new ChatRoom(title,password,creator,creatorID);
                    }else {
                        chatRoom = new ChatRoom(title,creator,creatorID);
                    }

                    chatRooms.add(chatRoom);
                }
            }
            if(firstLoad){
                Toast.makeText(context,"Loaded",Toast.LENGTH_SHORT).show();
                firstLoad=false;
            }
            adapter=new ChatRoomAdapter(chatRooms,context);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}

    public void deleteChatRoom(String userID, ChatRoom chatRoom) {
        if(chatRoom.getCreatorID().equals(userID)){
            myRef.child(chatRoom.getTitle()).setValue(null);
        }
    }

    public void createChatRoom(final String Title,final String Password) {
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference()
                .child("Chat Rooms").child(Title);
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    myRef.child(Title).child("Creator").setValue(chatRoomActivity.userModel.getUserName());
                    myRef.child(Title).child("CreatorID").setValue(chatRoomActivity.userModel.getUserID());
                    myRef.child(Title).child("Password").setValue(Password);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void createChatRoom(final String Title) {
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference()
                .child("Chat Rooms").child(Title);
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    myRef.child(Title).child("Creator").setValue(chatRoomActivity.userModel.getUserName());
                    myRef.child(Title).child("CreatorID").setValue(chatRoomActivity.userModel.getUserID());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
