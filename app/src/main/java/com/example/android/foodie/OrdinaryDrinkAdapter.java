package com.example.android.foodie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrdinaryDrinkAdapter extends RecyclerView.Adapter<OrdinaryDrinkAdapter.DrinkViewHolder> {

    LayoutInflater inflater;
    List<FoodCategory> ordinaryDrinkList = new ArrayList<>();
    OnDrinkItemClickListener drinkItemClickListener;
    int id;

    public interface OnDrinkItemClickListener {
        void onChickenItemClick(int position);
    }

    public OrdinaryDrinkAdapter(Context context, OnDrinkItemClickListener listener, List<FoodCategory> ordinaryDrinkList) {
        inflater = LayoutInflater.from(context);
        drinkItemClickListener = listener;
        this.ordinaryDrinkList = ordinaryDrinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.ordinary_drink_item, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {

        Picasso.get().load(ordinaryDrinkList.get(position).getFoodImage())
                .placeholder(R.mipmap.icon)
                .resize(4000,6000)
                .onlyScaleDown()
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(ordinaryDrinkList.get(position).getFoodName());
        id = ordinaryDrinkList.get(position).getFoodId();
    }

    @Override
    public int getItemCount() {
        return ordinaryDrinkList.size();
    }

    public FoodCategory getDrink(int position){

        if (ordinaryDrinkList != null){
            return ordinaryDrinkList.get(position);
        }
        else {
        return null;}
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.chicken_image);
            textView = itemView.findViewById(R.id.chicken_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            drinkItemClickListener.onChickenItemClick(position);
        }
    }
}
