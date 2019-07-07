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

public class ChickenAdapter extends RecyclerView.Adapter<ChickenAdapter.ChickenViewHolder> {

    LayoutInflater inflater;
    List<FoodCategory> chickenItemList = new ArrayList<>();
    OnChickenItemClickListener chickenItemClickListener;
    int id;

    public interface OnChickenItemClickListener{
        void onChickenItemClick(int position);
    }

    public ChickenAdapter(Context context,OnChickenItemClickListener listener, List<FoodCategory> chickenItemList) {
        inflater = LayoutInflater.from(context);
        chickenItemClickListener = listener;
        this.chickenItemList = chickenItemList;
    }

    @NonNull
    @Override
    public ChickenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.chicken_item, parent, false);
        return new ChickenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChickenViewHolder holder, int position) {

        Picasso.get().load(chickenItemList.get(position).getFoodImage())
                .placeholder(R.mipmap.icon)
                .resize(4000,6000)
                .onlyScaleDown()
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(chickenItemList.get(position).getFoodName());
        id = chickenItemList.get(position).getFoodId();
    }

    @Override
    public int getItemCount() {
        return chickenItemList.size();
    }

    public FoodCategory getDrink(int position){

        if (chickenItemList != null){
            return chickenItemList.get(position);
        }
        else {
        return null;}
    }

    public class ChickenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;

        public ChickenViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.chicken_image);
            textView = itemView.findViewById(R.id.chicken_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            chickenItemClickListener.onChickenItemClick(position);
        }
    }
}
