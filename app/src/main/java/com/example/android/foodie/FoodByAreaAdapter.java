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

public class FoodByAreaAdapter extends RecyclerView.Adapter<FoodByAreaAdapter.FoodByAreaViewHolder> {

    LayoutInflater inflater;
    List<FoodCategory> FoodByAreaItemList;
    AreaItemClickListener areaItemClickListener;

    public interface AreaItemClickListener{
        void onAreaClick(int position);
    }

    public FoodByAreaAdapter(Context context, AreaItemClickListener listener, List<FoodCategory> FoodByAreaItemList) {

        inflater = LayoutInflater.from(context);
        areaItemClickListener = listener;
        this.FoodByAreaItemList = FoodByAreaItemList;
    }

    @NonNull
    @Override
    public FoodByAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.random_food,parent,false);
        return new FoodByAreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodByAreaViewHolder holder, int position) {

        if (FoodByAreaItemList != null) {
            Picasso.get().load(FoodByAreaItemList.get(position).getFoodImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .resize(3000, 4000)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(holder.imageView);
            holder.textView.setText(FoodByAreaItemList.get(position).getFoodName());
        }
    }

    @Override
    public int getItemCount() {
        return FoodByAreaItemList.size();
    }

    public FoodCategory getAreaFood(int index){
        if (FoodByAreaItemList != null){
            return FoodByAreaItemList.get(index);
        }
        return null;
    }

    public class FoodByAreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;

        public FoodByAreaViewHolder(@NonNull View itemView) {
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
