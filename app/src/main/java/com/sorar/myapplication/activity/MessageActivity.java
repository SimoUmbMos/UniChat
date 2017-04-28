package com.sorar.myapplication.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.sorar.myapplication.R;
import com.sorar.myapplication.dao.DaoMessageController;
import com.sorar.myapplication.listener.OnSwipeTouchListener;
import com.sorar.myapplication.listener.OnUpdateListener;
import com.sorar.myapplication.model.Message;
import com.sorar.myapplication.model.User;

public class MessageActivity extends AppCompatActivity implements View.OnKeyListener {
    RecyclerView recView;
    RecyclerView.LayoutManager layoutManager;
    DaoMessageController daoMessageController;
    public User userModel;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public String ChatRoom,ChatRoomCreatorID;
    EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        ChatRoom=extras.getString("ChatRoom");
        ChatRoomCreatorID=extras.getString("ChatRoomCreator");
        mAuth = FirebaseAuth.getInstance();mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userModel=new User(user.getDisplayName(),user.getUid());

                } else {
                    // User is signed out
                    Intent intent=new Intent(MessageActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

                recView =(RecyclerView)findViewById(R.id.rv_message_container);
                layoutManager=new LinearLayoutManager(MessageActivity.this);
                recView.setLayoutManager(layoutManager);
                recView.setHasFixedSize(true);
                recView.requestFocus();
                recView.setOnTouchListener(new OnSwipeTouchListener(MessageActivity.this){
                    @Override
                    public void onSwipeRight() {
                        vibrate(1);
                        finish();
                    }
                    @Override
                    public void onSwipeLeft() {}
                    @Override
                    public void onSwipeTop() {}
                    @Override
                    public void onSwipeBottom() {}
                });
                daoMessageController =new DaoMessageController(recView,ChatRoom,MessageActivity.this);
                daoMessageController.setUpdateLisener(new OnUpdateListener(){
                    @Override
                    public void updateForMe(){
                        vibrate(1);
                    }
                    @Override
                    public void updateForOther(Message message){
                        vibrate(2);
                        createNotification(message);
                    }
                });
                etMessage = (EditText) findViewById(R.id.et_message_body_to_send);
                etMessage.setOnKeyListener(MessageActivity.this);
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public boolean onKey(View v, int keyCode, KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    CheckMessage(etMessage.getText().toString());
                    etMessage.setText("");
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    private void CheckMessage(String messageBody) {
        String messageToSend = messageBody.trim().replaceAll(" +", " ");
        if (messageToSend.length() > 0) {
            daoMessageController.sendMessage(messageToSend);
        }
    }

    public void vibrate(int i){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern={0,0,0,0};
        switch (i){
            case(1):
                pattern[1]=100;
                break;
            case(2):
                pattern[1]=100;
                pattern[2]=200;
                pattern[3]=200;
                break;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            v.vibrate(pattern,-1,audioAttributes);
        }else{
            v.vibrate(pattern,-1);
        }
    }

    public void createNotification(Message message){
        int i=0,mid=(int) System.currentTimeMillis();
        String hour="";
        while(message.getDate().charAt(i)!=' '){
            hour=hour+message.getDate().charAt(i);
            i++;
        }
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_chat_black_24dp)
                        .setSound(notification)
                        .setContentTitle("From:"+message.getSender())
                        .setContentText(hour+":"+message.getMessageBody());

        Intent resultIntent = new Intent(this, MessageActivity.class);
        resultIntent.putExtra("ChatRoom",ChatRoom);
        resultIntent.putExtra("UserName", userModel.getUserName());
        resultIntent.putExtra("UserID", userModel.getUserID());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MessageActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mid, mBuilder.build());
    }
}
