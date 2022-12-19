package com.citycube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.citycube.R;
import com.citycube.activities.NumberOfPassengers;
import com.citycube.databinding.PassengerItemBinding;
import com.citycube.listener.onVanSelectedListener;
import com.citycube.model.NumberPassengerModel;
import com.citycube.model.NumberPassengerModel2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravindra Birla on 06,April,2021
 */
public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder> {

    private Context context;
   // private ArrayList<NumberPassengerModel> arrayList;
   private ArrayList<NumberPassengerModel2.Result> arrayList;
    onVanSelectedListener listener;

    public PassengerAdapter(Context context, ArrayList<NumberPassengerModel2.Result> arrayList/*ArrayList<NumberPassengerModel> arrayList*/,onVanSelectedListener listener)
    {
        this.context = context;
        this.arrayList  = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PassengerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.passenger_item,parent,false);

        return new PassengerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerViewHolder holder, int position) {

     //   holder.binding.tvNumber.setText(arrayList.get(position).getPassengerNumber());

      if(arrayList.get(position).passenger.equals("No"))  holder.binding.tvNumber.setText(context.getString(R.string.no_passenger));

       else if(arrayList.get(position).passenger.equals("1")) holder.binding.tvNumber.setText(context.getString(R.string.one_passenger));

         else if(arrayList.get(position).passenger.equals("2")) holder.binding.tvNumber.setText(context.getString(R.string.two_passenger));


              if(arrayList.get(position).isChk()==true){
            holder.binding.rlPassenger.setBackgroundColor(context.getColor(R.color.selected_color));
            holder.binding.tvNumber.setTextColor(context.getColor(R.color.white));
        }
        else {
            holder.binding.rlPassenger.setBackgroundColor(context.getColor(R.color.white));
            holder.binding.tvNumber.setTextColor(context.getColor(R.color.black));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PassengerViewHolder extends RecyclerView.ViewHolder
    {   PassengerItemBinding binding;
        public PassengerViewHolder(PassengerItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.rlPassenger.setOnClickListener(v -> {
                for (int i=0;i<arrayList.size();i++){
                    arrayList.get(i).setChk(false);
                }
                arrayList.get(getAdapterPosition()).setChk(true);
                listener.onVan(getAdapterPosition());
                notifyDataSetChanged();
            });

        }
    }

}
