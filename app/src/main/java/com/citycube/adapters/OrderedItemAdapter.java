package com.citycube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.citycube.R;
import com.citycube.activities.ContactInfo;
import com.citycube.databinding.ItemProgressBinding;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 07,April,2021
 */
public class OrderedItemAdapter extends RecyclerView.Adapter<OrderedItemAdapter.OrderedItemViewHolder> {

    ItemProgressBinding binding;
    private Context context;
    List<String> strings = new LinkedList<>();

    public OrderedItemAdapter(Context context,List<String> strings)
    {
        this.context = context;
        this.strings = strings;
    }


    @NonNull
    @Override
    public OrderedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_progress,parent,false);
        return new OrderedItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedItemViewHolder holder, int position) {

        if(strings.get(position).equalsIgnoreCase("Delivered"))
        {
            binding.tvDone.setTextColor(context.getColor(R.color.green));
        } else if(strings.get(position).equalsIgnoreCase("Cancelled"))
        {
            binding.tvDone.setTextColor(context.getColor(R.color.dark_red));
        }
        binding.tvDone.setText(strings.get(position));


    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class OrderedItemViewHolder extends RecyclerView.ViewHolder {
        public OrderedItemViewHolder(ItemProgressBinding binding1) {
            super(binding1.getRoot());
        }
    }


}
