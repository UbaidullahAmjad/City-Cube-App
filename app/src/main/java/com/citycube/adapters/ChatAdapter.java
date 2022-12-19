package com.citycube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.citycube.R;
import com.citycube.databinding.ChatItemBinding;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 18,March,2021
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    ChatItemBinding binding;

   private Context context;
   String myId;

 public ChatAdapter(Context context,String myId)
 {
     this.context = context;
     this.myId = myId;
 }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_item, parent, false);
        return new ChatViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {

        public ChatViewHolder(ChatItemBinding adapterChatBinding) {
            super(adapterChatBinding.getRoot());
        }
    }

}
