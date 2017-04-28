package com.sorar.myapplication.Dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sorar.myapplication.R;
import com.sorar.myapplication.activity.ChatRoomActivity;
import com.sorar.myapplication.model.ChatRoom;


public class DeleteChatRoomDialog extends Dialog implements View.OnClickListener {
    private final ChatRoomActivity activity;
    private EditText ControllET;
    private ChatRoom chatRoom;

    public DeleteChatRoomDialog(ChatRoomActivity activity,ChatRoom chatRoom) {
        super(activity);
        this.activity=activity;
        this.chatRoom = chatRoom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_chat_room_dialog);
        Button next = (Button) findViewById(R.id.btn_dialog_next);
        Button cancel = (Button) findViewById(R.id.btn_dialog_cancel);
        TextView title=(TextView) findViewById(R.id.text_dialog);
        title.setText("Enter \"Sure\" below to Delete: "+ chatRoom.getTitle());
        next.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ControllET = (EditText) findViewById(R.id.et_thread_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_next:
                String Sure = ControllET.getText().toString().toLowerCase();
                if (Sure.equals("sure")) {
                        DeleteThread(chatRoom);
                } else {
                    Toast.makeText(activity, "You don't press Sure", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_dialog_cancel:
                dismiss();
                break;
        }
    }

    /**
     * delete the chat room on firebase
    **/
    private void DeleteThread(ChatRoom chatRoom){
        activity.daoChatRoomController.deleteChatRoom(activity.userModel.getUserID(),chatRoom);
        Toast.makeText(activity,"Chat Room Deleted", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
