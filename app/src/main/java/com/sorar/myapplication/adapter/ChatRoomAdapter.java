package com.sorar.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorar.myapplication.Dialogs.DeleteChatRoomDialog;
import com.sorar.myapplication.Dialogs.ProceedToChatRoomDialog;
import com.sorar.myapplication.R;
import com.sorar.myapplication.activity.ChatRoomActivity;
import com.sorar.myapplication.model.ChatRoom;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>{
    private ArrayList<ChatRoom> chatRooms =new ArrayList<>();
    private Context context;
    private ChatRoomActivity chatRoomActivity;

    public ChatRoomAdapter(ArrayList<ChatRoom> chatRooms, Context context){
        this.context=context;
        this.chatRoomActivity =(ChatRoomActivity)context;
        this.chatRooms =chatRooms;
    }

    @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_chatroom_layout,parent,false);
        return new ChatRoomViewHolder(view, chatRoomActivity);
    }

    @Override
    public void onBindViewHolder(ChatRoomViewHolder holder,int position) {
        final int finalPosition=position;
        final ProceedToChatRoomDialog proceedToChatRoomDialog
                =new ProceedToChatRoomDialog(chatRoomActivity,chatRooms.get(finalPosition));
        holder.tv_private.setText(chatRooms.get(position).isPrivateToString());
        holder.title.setText(chatRooms.get(position).getTitle());
        holder.sender.setText(chatRooms.get(position).getCreator());
        if(chatRoomActivity.userModel.getUserID().equals(chatRooms.get(position).getCreatorID())){
            holder.chatRoomHolder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteDialog(finalPosition);
                    return true;
                }
            });
        }
        holder.chatRoomHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkToGo(finalPosition,proceedToChatRoomDialog);
            }
        });
    }

    public void checkToGo(int Position,ProceedToChatRoomDialog proceedToChatRoomDialog){
        chatRoomActivity.vibrate(1);
        if(chatRooms.get(Position).isPrivate()){
            proceedToChatRoomDialog.show();
        }else{
            proceedToChatRoomDialog.goToChatRoom();
        }
    }

    public void showDeleteDialog(int Position){
        chatRoomActivity.vibrate(1);
        DeleteChatRoomDialog deleteChatRoomDialog=new DeleteChatRoomDialog(chatRoomActivity,
                chatRooms.get(Position));
        deleteChatRoomDialog.show();
    }
    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_private,title,sender;
        CardView chatRoomHolder;
        ChatRoomActivity chatRoomActivity;
        public ChatRoomViewHolder(View itemView, ChatRoomActivity chatRoomActivity) {
            super(itemView);
            this.chatRoomActivity = chatRoomActivity;
            chatRoomHolder =(CardView) itemView.findViewById(R.id.card_view_chat_room);
            tv_private=(TextView) itemView.findViewById(R.id.tv_private);
            title =(TextView) itemView.findViewById(R.id.tv_chatroom_title);
            sender=(TextView) itemView.findViewById(R.id.tv_sender);
            chatRoomHolder.setClickable(true);
            chatRoomHolder.setFocusable(true);
            chatRoomHolder.setFocusableInTouchMode(true);
            chatRoomHolder.setLongClickable(true);
        }
    }
}
