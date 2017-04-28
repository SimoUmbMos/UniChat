package com.sorar.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sorar.myapplication.Dialogs.CreateChatRoomDialog;
import com.sorar.myapplication.R;
import com.sorar.myapplication.dao.DaoChatRoomController;
import com.sorar.myapplication.model.User;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recView;
    RecyclerView.LayoutManager layoutManager;
    public User userModel;
    public DaoChatRoomController daoChatRoomController;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userModel=new User(user.getDisplayName(),user.getUid());
                } else {
                    // User is signed out
                    Intent intent=new Intent(ChatRoomActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                GoogleSignInOptions gso = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                        .build();
                mGoogleApiClient = new GoogleApiClient.Builder(ChatRoomActivity.this).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                mGoogleApiClient.connect();
                TextView tvUserName = (TextView) findViewById(R.id.tv_username);
                tvUserName.setText(userModel.getUserName());
                recView =(RecyclerView)findViewById(R.id.rv_chat_room_container);
                layoutManager=new LinearLayoutManager(ChatRoomActivity.this);
                recView.setLayoutManager(layoutManager);
                recView.setHasFixedSize(true);
                recView.requestFocus();
                daoChatRoomController=new DaoChatRoomController(recView,ChatRoomActivity.this);
                FloatingActionButton fabAddChat=(FloatingActionButton)findViewById(R.id.fab_add_chatroom);
                fabAddChat.setOnClickListener(ChatRoomActivity.this);
                FloatingActionButton fabLogOut = (FloatingActionButton) findViewById(R.id.fab_logout);
                fabLogOut.setOnClickListener(ChatRoomActivity.this);
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
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fab_add_chatroom){
            vibrate(1);
            CreateChatRoomDialog createChatRoomDialog = new CreateChatRoomDialog(ChatRoomActivity.this);
            createChatRoomDialog.show();
        }else if(v.getId()==R.id.fab_logout){
            vibrate(1);
            logout();
        }
    }

    private void logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
            new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if(status.isSuccess()){
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(ChatRoomActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
    }

    public void vibrate(int i){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
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
        if(i==2){
            r.play();
        }
    }
}
