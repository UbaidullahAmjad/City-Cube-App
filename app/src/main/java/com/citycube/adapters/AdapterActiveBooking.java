package com.citycube.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.citycube.R;
import com.citycube.activities.LiveTrackingActivity;
import com.citycube.databinding.ItemActiveBookingBinding;
import com.citycube.model.BookingModel;

import java.util.ArrayList;

public class AdapterActiveBooking extends RecyclerView.Adapter<AdapterActiveBooking.MyViewHolder> {
    Context context;
    ArrayList<BookingModel.Result> arrayList;

    public AdapterActiveBooking(Context context,ArrayList<BookingModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActiveBookingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_active_booking,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).driverDetails.userName);
        holder.binding.tvEmail.setText(arrayList.get(position).driverDetails.email);
        holder.binding.tvDeparture.setText(arrayList.get(position).picuplocation);
        holder.binding.tvArrival.setText(arrayList.get(position).dropofflocation);
        holder.binding.tvTime.setText("Time : "+arrayList.get(position).picklatertime);
        holder.binding.tvDate.setText("Date : "+arrayList.get(position).picklaterdate);
        holder.binding.tvBookingname.setText("Name : "+arrayList.get(position).firstName);
        holder.binding.tvPhone.setText("Phone Number : "+arrayList.get(position).mobile);
        holder.binding.tvPrice.setText("€"+arrayList.get(position).fare);
        holder.binding.tvCompleted.setText(arrayList.get(position).status);
        holder.binding.tvTruck.setText(arrayList.get(position).vanSize);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemActiveBookingBinding binding;
        public MyViewHolder(@NonNull ItemActiveBookingBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.llMain.setOnClickListener(v -> {
              context.startActivity(new Intent(context, LiveTrackingActivity.class)
              .putExtra("request_id",arrayList.get(getAdapterPosition()).id));
            });
        }
    }
}

