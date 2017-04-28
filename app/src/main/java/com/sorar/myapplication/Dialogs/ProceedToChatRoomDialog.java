package com.sorar.myapplication.Dialogs;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sorar.myapplication.R;
import com.sorar.myapplication.activity.ChatRoomActivity;
import com.sorar.myapplication.activity.MessageActivity;
import com.sorar.myapplication.model.ChatRoom;


public class ProceedToChatRoomDialog extends Dialog implements View.OnClickListener {
    private final ChatRoomActivity activity;
    private final ChatRoom chatRoom;
    private EditText etPassword;

    public ProceedToChatRoomDialog(ChatRoomActivity activity, ChatRoom chatRoom) {
        super(activity);
        this.activity=activity;
        this.chatRoom=chatRoom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.proceed_to_chat_room_dialog);
        Button next = (Button) findViewById(R.id.btn_dialog_next);
        Button cancel = (Button) findViewById(R.id.btn_dialog_cancel);
        TextView title=(TextView) findViewById(R.id.text_dialog);
        title.setText("Enter the password for Chat Room: "+chatRoom.getTitle());
        next.setOnClickListener(this);
        cancel.setOnClickListener(this);
        etPassword = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_next:
                String password = etPassword.getText().toString();
                if (chatRoom.AuthAccess(password)) {
                        goToChatRoom();
                } else {
                    Toast.makeText(activity, "Password Don't Match", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_dialog_cancel:
                dismiss();
                break;
        }

    }

    /**
     * go from Main to the chat room
     **/
    public void goToChatRoom(){
        Intent intent=new Intent(activity, MessageActivity.class);
        intent.putExtra("UserName",activity.userModel.getUserName());
        intent.putExtra("UserID",activity.userModel.getUserID());
        intent.putExtra("ChatRoom",chatRoom.getTitle());
        intent.putExtra("ChatRoomCreator",chatRoom.getCreatorID());

        activity.startActivity(intent);
        dismiss();
    }

}
