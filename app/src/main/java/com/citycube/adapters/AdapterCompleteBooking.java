package com.citycube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.citycube.R;
import com.citycube.databinding.ItemCompleteBookingBinding;
import com.citycube.model.BookingModel;

import java.util.ArrayList;

public class AdapterCompleteBooking extends RecyclerView.Adapter<AdapterCompleteBooking.MyViewHolder> {
    Context context;
    ArrayList<BookingModel.Result> arrayList;

    public AdapterCompleteBooking(Context context, ArrayList<BookingModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCompleteBookingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_complete_booking,parent,false);
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
        //holder.binding.tvTruck.setText(arrayList.get(position).vanSize);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCompleteBookingBinding binding;
        public MyViewHolder(@NonNull ItemCompleteBookingBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
