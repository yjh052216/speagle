package com.hwdp.seagle.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hwdp.seagle.R;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MessageViewHolder> {
    private ArrayList<ChatMessage> mData = null;
    private Context context;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RecycleAdapter(ArrayList<ChatMessage> list) {
        mData = list;
    }

    //context 설정
    public void setContext(Context context){
        this.context = context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecycleAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_room, parent, false);
        RecycleAdapter.MessageViewHolder vh = new RecycleAdapter.MessageViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecycleAdapter.MessageViewHolder holder, int position) {

        ChatMessage item = mData.get(position);

        SharedPreferences pref= context.getSharedPreferences("user", Context.MODE_PRIVATE);

        holder.text_sender.setText(item.getSender());
        holder.text_message.setText(item.getMessage());
        if(holder.text_sender.getText().toString().equals((pref.getString("email",null)))){
            holder.layout.setGravity(Gravity.RIGHT);
        }else{
            holder.layout.setGravity(Gravity.LEFT);
        }
        holder.layout.setGravity(Gravity.RIGHT);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView text_sender;
        ImageView messageImageView;
        TextView text_message;
        LinearLayout layout;

        public MessageViewHolder(View v) {
            super(v);
            layout = itemView.findViewById(R.id.item_layout);
            text_sender = itemView.findViewById(R.id.messageTextView);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            text_message = itemView.findViewById(R.id.messengerTextView);
        }
    }

}