package com.citycube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.citycube.R;
import com.citycube.databinding.ItemSizeBinding;
import com.citycube.listener.onHandlerSelectedListener;
import com.citycube.listener.onVanSelectedListener;
import com.citycube.model.HandlerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravindra Birla on 06,April,2021
 */
public class HandlingServiceAdapter extends RecyclerView.Adapter<HandlingServiceAdapter.HandlingServiceViewHolder> {

    private Context context;
    ArrayList<HandlerModel>arrayList;
    onHandlerSelectedListener listener;
    String ChkVeriable;

    public HandlingServiceAdapter(Context context, ArrayList<HandlerModel> arrayList,onHandlerSelectedListener listener,String ChkVeriable)
    {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        this.ChkVeriable = ChkVeriable;
        for (int i=0;i<arrayList.size();i++){
            arrayList.get(i).setChk(false);
        }
        arrayList.get(0).setChk(true);
        listener.onHandler(0,"");
    }



    @NonNull
    @Override
    public HandlingServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSizeBinding   binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_size,parent,false);
        return new HandlingServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HandlingServiceViewHolder holder, int position) {
        holder.binding.tvHandlerName.setText(arrayList.get(position).getHandlerName());
       //holder.binding.ivService.setImageDrawable(arrayList.get(position).getHandlerImage());
        Glide.with(context).load(arrayList.get(position).getHandlerImage())
                .into(holder.binding.ivService);
        if(arrayList.get(position).getHandlerId().equals("1")) {
            if (arrayList.get(position).isChk() == true) {
                holder.binding.rlHandler.setBackgroundColor(context.getColor(R.color.selected_color));
                holder.binding.tvHandlerName.setTextColor(context.getColor(R.color.white));
                holder.binding.tvHandlerType.setText(arrayList.get(position).getHandlerType());
            }
            else {
                holder.binding.rlHandler.setBackgroundColor(context.getColor(R.color.white));
                holder.binding.tvHandlerName.setTextColor(context.getColor(R.color.selected_color));
                holder.binding.tvHandlerType.setText(arrayList.get(position).getHandlerType());
            }
        }
        if(arrayList.get(position).getHandlerId().equals("2")) {
            if (arrayList.get(position).isChk() == true) {
                holder.binding.rlHandler.setBackgroundColor(context.getColor(R.color.selected_color));
                holder.binding.tvHandlerName.setTextColor(context.getColor(R.color.white));
                holder.binding.tvHandlerType.setText(arrayList.get(position).getHandlerType());
            }
            else {
                holder.binding.rlHandler.setBackgroundColor(context.getColor(R.color.white));
                holder.binding.tvHandlerName.setTextColor(context.getColor(R.color.selected_color));
                holder.binding.tvHandlerType.setText(arrayList.get(position).getHandlerType());
            }
        }

        if(arrayList.get(position).getHandlerId().equals("3")) {
            if (arrayList.get(position).isChk() == true) {


                if(ChkVeriable.equals("1")){
                    holder.binding.rlHandler.setFocusable(false);
                    holder.binding.rlHandler.setBackgroundColor(context.getColor(R.color.light_gray));
                    holder.binding.tvHandlerName.setTextColor(context.getColor(R.color.selected_color));
                    holder.binding.tvHandlerType.setTextColor(context.getColor(R.color.orange));
                    holder.binding.tvHandlerType.setText(arrayList.get(position).getHandlerType());
                }else {
                    holder.binding.rlHandler.setFocusable(true);
                    holder.binding.rlHandler.setBackgroundColor(context.getColor(R.color.selected_color));
                    holder.binding.tvHandlerName.setTextColor(context.getColor(R.color.white));
                    holder.binding.tvHandlerType.setTextColor(context.getColor(R.color.black));
                    holder.binding.tvHandlerType.setText(context.getString(R.string.max_90_kg_per_object));
                }

            }
            else {
                holder.binding.rlHandler.setBackgroundColor(context.getColor(R.color.white));
                holder.binding.tvHandlerName.setTextColor(context.getColor(R.color.selected_color));
                if(ChkVeriable.equals("1")){
                    holder.binding.rlHandler.setFocusable(false);
                    holder.binding.tvHandlerType.setTextColor(context.getColor(R.color.orange));
                    holder.binding.tvHandlerType.setText(arrayList.get(position).getHandlerType());
                }else {
                    holder.binding.rlHandler.setFocusable(true);
                    holder.binding.tvHandlerType.setTextColor(context.getColor(R.color.black));
                    holder.binding.tvHandlerType.setText(context.getString(R.string.max_90_kg_per_object));
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HandlingServiceViewHolder extends RecyclerView.ViewHolder
    {
        ItemSizeBinding binding;
        public HandlingServiceViewHolder(ItemSizeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.rlHandler.setOnClickListener(v -> {
                for (int i=0;i<arrayList.size();i++){
                    arrayList.get(i).setChk(false);
                }
                arrayList.get(getAdapterPosition()).setChk(true);
                if(!context.getString(R.string.option_not_available_with_selected_vehicle).equals(binding.tvHandlerType.getText().toString()))
                listener.onHandler(getAdapterPosition(),binding.tvHandlerType.getText().toString());
                notifyDataSetChanged();
            });
        }
    }

}
