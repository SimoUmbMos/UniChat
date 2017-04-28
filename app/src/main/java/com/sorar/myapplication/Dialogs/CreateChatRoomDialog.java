package com.sorar.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.sorar.myapplication.R;
import com.sorar.myapplication.activity.ChatRoomActivity;


public class CreateChatRoomDialog extends Dialog implements View.OnClickListener {
    private final ChatRoomActivity activity;
    private EditText et_chatRoomTitle,password;
    private CheckBox chatRoomPrivate;

    public CreateChatRoomDialog(Context activity) {
        super(activity);
        this.activity=(ChatRoomActivity)activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_chat_room_dialog);
        Button next = (Button) findViewById(R.id.btn_dialog_next);
        Button cancel = (Button) findViewById(R.id.btn_dialog_cancel);
        next.setOnClickListener(this);
        cancel.setOnClickListener(this);
        et_chatRoomTitle = (EditText) findViewById(R.id.et_thread_name);
        password = (EditText) findViewById(R.id.et_thread_password);
        chatRoomPrivate = (CheckBox) findViewById(R.id.cb_private);
        chatRoomPrivate.setOnClickListener(this);
        password.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dialog_next:
                String chatRoomTitle= this.et_chatRoomTitle.getText().toString();
                if(!chatRoomTitle.isEmpty()){
                    if(chatRoomPrivate.isChecked()){
                        String chatRoomPassword = password.getText().toString().trim().replaceAll(" +","");
                        if(chatRoomPassword.length()>0){
                            createNewThread(chatRoomPassword,chatRoomTitle);
                        }else{
                            Toast.makeText(activity,"Password must have 1 or more character", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        createNewThread("",chatRoomTitle);
                    }
                }else{
                    Toast.makeText(activity,"You don't set the name of the Chat Room", Toast.LENGTH_LONG).show();
                }
            break;
            case R.id.btn_dialog_cancel:
                dismiss();
            break;
            case R.id.cb_private:
                if(!chatRoomPrivate.isChecked()){
                    password.setEnabled(false);
                }else{
                    password.setEnabled(true);
                }
                break;
        }
    }

    private void createNewThread(String Password,String chatRoomTitle){
        if(Password.equals("")) {
            activity.daoChatRoomController.createChatRoom(chatRoomTitle);
        }else {
            activity.daoChatRoomController.createChatRoom(chatRoomTitle,Password);
        }
        dismiss();
    }
}
