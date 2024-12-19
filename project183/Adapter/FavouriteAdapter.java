package com.example.project183.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project183.Domain.Foods;
import com.example.project183.R;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private ArrayList<Foods> favouriteList;

    public FavouriteAdapter(ArrayList<Foods> favouriteList) {
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = favouriteList.get(position);

        holder.titleTxt.setText(food.getTitle());
        holder.priceTxt.setText("$" + food.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(food.getImagePath())
                .into(holder.foodPic);

        holder.removeBtn.setOnClickListener(v -> {
            favouriteList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favouriteList.size());
        });
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt;
        ImageView foodPic, removeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            foodPic = itemView.findViewById(R.id.foodPic);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
}

