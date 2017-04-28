package com.sorar.myapplication.dao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sorar.myapplication.activity.MessageActivity;
import com.sorar.myapplication.adapter.MessageAdapter;
import com.sorar.myapplication.listener.OnUpdateListener;
import com.sorar.myapplication.model.Message;
import com.sorar.myapplication.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DaoMessageController implements ValueEventListener {
    private final Context context;
    private final MessageActivity messageActivity;
    private RecyclerView recyclerView;
    private DatabaseReference myRef ;
    private RecyclerView.Adapter adapter;
    private boolean firstLoad,sendedmessage,show_notification;
    private ArrayList<Message> messages=new ArrayList<>();
    private ArrayList<User> usersSenders=new ArrayList<>();
    private OnUpdateListener updateLisener;
    private int numberChildAdded;

    public DaoMessageController(RecyclerView recyclerView,String chatRoom, Context context){
        sendedmessage=false;
        numberChildAdded=0;
        usersSenders=new ArrayList<>();
        updateLisener=new OnUpdateListener();
        this.context=context;
        this.recyclerView=recyclerView;
        this.messageActivity =(MessageActivity)context;
        show_notification=true;
        firstLoad=true;
        recyclerView.setAdapter(adapter);
        myRef=FirebaseDatabase.getInstance().getReference().child("Chat Rooms").child(chatRoom);
        myRef.addValueEventListener(this);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                show_notification=false;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(context,"Loading..",Toast.LENGTH_SHORT).show();
    }

    public void sendMessage(String messageBody){
        SimpleDateFormat sdf;
        Date now = new Date();
        sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS",Locale.ENGLISH);
        String key = sdf.format(now);
        sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String date = sdf.format(now);
        sendedmessage=true;
        DatabaseReference tempRef = myRef.child(key);
        tempRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(sendedmessage){
                    numberChildAdded=numberChildAdded+1;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tempRef.child("sender").setValue(messageActivity.userModel.getUserName());
        tempRef.child("uidSender").setValue(messageActivity.userModel.getUserID());
        tempRef.child("messageBody").setValue(messageBody);
        tempRef.child("date").setValue(date);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot!=null){
            messages.clear();
            usersSenders.clear();
            Message message;
            String messageBody,sender, uidSender, date;
            for(DataSnapshot messageFb:dataSnapshot.getChildren()){
                if(messageFb.child("sender").exists()&& messageFb.child("messageBody").exists()
                        &&messageFb.child("uidSender").exists()&& messageFb.child("date").exists()){

                    messageBody=messageFb.child("messageBody").getValue(String.class);
                    sender=messageFb.child("sender").getValue(String.class);
                    uidSender=messageFb.child("uidSender").getValue(String.class);
                    date=messageFb.child("date").getValue(String.class);
                    message = new Message(messageBody,sender,uidSender,date);
                    messages.add(message);
                }
            }
            getListOfUserSenders();
            if(firstLoad){
                Toast.makeText(context,"Loaded",Toast.LENGTH_SHORT).show();
                firstLoad=false;
            }else if(show_notification){
                if(sendedmessage){
                    if(numberChildAdded==4){
                        checkAndNotify(usersSenders);
                        sendedmessage=false;
                        numberChildAdded=0;
                    }
                }else{
                    checkAndNotify(usersSenders);
                }
            }
            show_notification=true;
            adapter=new MessageAdapter(messages,context);
            recyclerView.setAdapter(adapter);
        }
    }

    private void checkAndNotify(ArrayList<User> usersSenders) {
        int i;
        boolean not_fund=true;
        for(i=0;i<usersSenders.size();i++){
            boolean check=(usersSenders.get(i).getUserID().equals(messageActivity.userModel.getUserID())
                    ||messageActivity.userModel.getUserID().equals(messageActivity.ChatRoomCreatorID));
             if(not_fund&&check){
                 if(!messages.get(messages.size()-1).getUidSender().equals(messageActivity.userModel.getUserID()))
                     updateLisener.updateForOther(messages.get(messages.size()-1));
                 else
                     updateLisener.updateForMe();
                 not_fund=false;
            }
        }
    }

    private void getListOfUserSenders() {
        int i;
        for(i=0;i<messages.size();i++){
            User tempUser=new User(messages.get(i).getSender(),messages.get(i).getUidSender());
            if(!usersSenders.contains(tempUser)){
                usersSenders.add(tempUser);
            }
        }
    }


    public void setUpdateLisener(OnUpdateListener updateLisener) {
        this.updateLisener = updateLisener;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}
}
