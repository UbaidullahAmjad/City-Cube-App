package com.citycube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.citycube.R;
import com.citycube.databinding.ItemVehicleBinding;
import com.citycube.listener.onVanSelectedListener;
import com.citycube.model.VehicleModel;


import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> {
    Context context;
    ArrayList<VehicleModel>arrayList;
    onVanSelectedListener listener;
    //DialogDes dialogDes;


    public VehicleAdapter(Context context, ArrayList<VehicleModel> arrayList,onVanSelectedListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVehicleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_vehicle,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvVehicleName.setText(arrayList.get(position).getVehicleName());
        holder.binding.tvVehicleSize.setText(arrayList.get(position).getVehicleSize());
        holder.binding.tvVehicleWeight.setText(arrayList.get(position).getWeight());

        if(arrayList.get(position).getVehicleId().equals("1")) {
            if (arrayList.get(position).isChk() == true) {
                holder.binding.rlVehicle.setBackgroundColor(context.getColor(R.color.selected_color));
                holder.binding.tvVehicleName.setTextColor(context.getColor(R.color.white));
                holder.binding.ivVehicle.setImageDrawable(context.getDrawable(R.drawable.small_selected));
            }
            else {
                holder.binding.rlVehicle.setBackgroundColor(context.getColor(R.color.white));
                holder.binding.tvVehicleName.setTextColor(context.getColor(R.color.selected_color));
                holder.binding.ivVehicle.setImageDrawable(context.getDrawable(R.drawable.small_unselected));
            }
        }
        if(arrayList.get(position).getVehicleId().equals("2")) {
            if (arrayList.get(position).isChk() == true) {
                holder.binding.rlVehicle.setBackgroundColor(context.getColor(R.color.selected_color));
                holder.binding.tvVehicleName.setTextColor(context.getColor(R.color.white));
                holder.binding.ivVehicle.setImageDrawable(context.getDrawable(R.drawable.classic_selected));
            }
            else {
                holder.binding.rlVehicle.setBackgroundColor(context.getColor(R.color.white));
                holder.binding.tvVehicleName.setTextColor(context.getColor(R.color.selected_color));
                holder.binding.ivVehicle.setImageDrawable(context.getDrawable(R.drawable.classic_unselected));
            }
        }

        if(arrayList.get(position).getVehicleId().equals("3")) {
            if (arrayList.get(position).isChk() == true) {
                holder.binding.rlVehicle.setBackgroundColor(context.getColor(R.color.selected_color));
                holder.binding.tvVehicleName.setTextColor(context.getColor(R.color.white));
                holder.binding.ivVehicle.setImageDrawable(context.getDrawable(R.drawable.large_selected));
            }
            else {
                holder.binding.rlVehicle.setBackgroundColor(context.getColor(R.color.white));
                holder.binding.tvVehicleName.setTextColor(context.getColor(R.color.selected_color));
                holder.binding.ivVehicle.setImageDrawable(context.getDrawable(R.drawable.large_unselected));
            }
        }

        if(arrayList.get(position).getVehicleId().equals("4")) {
            if (arrayList.get(position).isChk() == true) {
                holder.binding.rlVehicle.setBackgroundColor(context.getColor(R.color.selected_color));
                holder.binding.tvVehicleName.setTextColor(context.getColor(R.color.white));
                holder.binding.ivVehicle.setImageDrawable(context.getDrawable(R.drawable.jumbo));
            }
            else {
                holder.binding.rlVehicle.setBackgroundColor(context.getColor(R.color.white));
                holder.binding.tvVehicleName.setTextColor(context.getColor(R.color.selected_color));
                holder.binding.ivVehicle.setImageDrawable(context.getDrawable(R.drawable.jumbo));
            }
        }




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemVehicleBinding binding;
        public MyViewHolder(@NonNull ItemVehicleBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.rlVehicle.setOnClickListener(v -> {
                for (int i=0;i<arrayList.size();i++){
                    arrayList.get(i).setChk(false);
                }
                arrayList.get(getAdapterPosition()).setChk(true);
                listener.onVan(getAdapterPosition());
                notifyDataSetChanged();

                //dialogDes = new DialogDes();

            });




        }

    }
}
