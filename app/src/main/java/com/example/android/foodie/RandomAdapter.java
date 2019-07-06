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

import java.util.List;

public class RandomAdapter extends RecyclerView.Adapter<RandomAdapter.RandomViewHolder> {

    LayoutInflater inflater;
    List<FoodCategory>randomList;
    AreaItemClickListener areaItemClickListener;

    public interface AreaItemClickListener{
        void onAreaClick(int position);
    }

    public RandomAdapter(Context context,AreaItemClickListener listener, List<FoodCategory> randomList) {

        inflater = LayoutInflater.from(context);
        areaItemClickListener = listener;
        this.randomList = randomList;
    }

    @NonNull
    @Override
    public RandomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.random_food,parent,false);
        return new RandomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomViewHolder holder, int position) {

        Picasso.get().load(randomList.get(position).getFoodImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .resize(300,400)
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(randomList.get(position).getFoodName());
    }

    @Override
    public int getItemCount() {
        return randomList.size();
    }

    public FoodCategory getAreaFood(int index){
        if (randomList!= null){
            return randomList.get(index);
        }
        return null;
    }

    public class RandomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;

        public RandomViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_random_image);
            textView = itemView.findViewById(R.id.tv_random_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            areaItemClickListener.onAreaClick(position);
        }
    }
}
