package com.agno3.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> mMsgList;

    MsgAdapter(List<Msg> mMsgList) {
        this.mMsgList = mMsgList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_left;
        LinearLayout layout_right;

        TextView left_msg;
        TextView right_msg;

        public ViewHolder(View itemView) {
            super(itemView);
            layout_left = itemView.findViewById(R.id.left_layout);
            layout_right = itemView.findViewById(R.id.right_layout);
            left_msg = itemView.findViewById(R.id.left_msg);
            right_msg = itemView.findViewById(R.id.right_msg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        View.OnLongClickListener clickListener = v -> false;
        holder.right_msg.setOnLongClickListener(clickListener);
        holder.right_msg.setOnLongClickListener(clickListener);

        if(msg.getType() == Msg.TYPE_RECEIVED)
        {
            holder.layout_right.setVisibility(View.GONE);
            holder.layout_left.setVisibility(View.VISIBLE);
            holder.left_msg.setText(msg.getContent());
        }else if (msg.getType() == Msg.TYPE_SEND){
            holder.layout_right.setVisibility(View.VISIBLE);
            holder.layout_left.setVisibility(View.GONE);
            holder.right_msg.setText(msg.getContent());
        }
    }


    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
