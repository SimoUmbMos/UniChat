package com.sorar.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sorar.myapplication.R;
import com.sorar.myapplication.activity.MessageActivity;
import com.sorar.myapplication.model.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private ArrayList<Message> messages=new ArrayList<>();
    private MessageActivity messageActivity;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages,Context context){
        this.context=context;
        this.messageActivity =(MessageActivity)context;
        this.messages=messages;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_message_layout,parent,false);
        return new MessageViewHolder(view, messageActivity);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder holder, int position) {
        holder.messageBody.setText(messages.get(position).getMessageBody());
        holder.dateSended.setText(messages.get(position).getDate());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if(!messages.get(position).getUidSender().equals(messageActivity.userModel.getUserID())){
            holder.sender.setText(messages.get(position).getSender());

            params.gravity=Gravity.LEFT;
            holder.messageHolder.setLayoutParams(params);

        }else{
            holder.sender.setVisibility(View.GONE);

            params.gravity=Gravity.RIGHT;
            holder.messageHolder.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView messageBody,dateSended,sender;
        CardView messageHolder;
        Boolean showDate;
        MessageActivity messageActivity;
        public MessageViewHolder(View itemView, MessageActivity messageActivity) {
            super(itemView);
            showDate=false;
            this.messageActivity = messageActivity;
            messageHolder=(CardView) itemView.findViewById(R.id.card_view_message);
            messageBody=(TextView) itemView.findViewById(R.id.tv_message_body);
            dateSended=(TextView) itemView.findViewById(R.id.tv_date_sended);
            sender=(TextView) itemView.findViewById(R.id.tv_sender);
            messageHolder.setOnLongClickListener(this);
            showDateOnCardView();
        }

        private void showDateOnCardView() {
            if(showDate)
                dateSended.setVisibility(View.VISIBLE);
            else
                dateSended.setVisibility(View.GONE);
        }

        @Override
        public boolean onLongClick(View v) {
            showDate=!showDate;
            showDateOnCardView();
            return true;
        }
    }
}
