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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {


    public FoodItemClickListener clickListener;
    private LayoutInflater inflater;
    private List<Food> foodList;

    public FoodAdapter(Context context, FoodItemClickListener listener, List<Food> foodList) {
        this.foodList = foodList;
        clickListener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

        Picasso.get().load(foodList.get(position).getImageUrl())
                .placeholder(R.drawable.ic_search)
                .resize(3000,4000)
                .onlyScaleDown()
                .centerInside()
                .into(holder.foodImage);
        holder.foodName.setText(foodList.get(position).getFoodName());
    }

    @Override
    public int getItemCount() {
        if (foodList == null) {
            return 0;
        }
        return foodList.size();
    }

    public Food getFood(int position) {
        if (foodList != null && foodList.size()>0){

            return foodList.get(position);
        }
        return null;
    }

    public interface FoodItemClickListener {
        void onItemClick(int item);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView foodImage;
        TextView foodName;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.iv_food_image);
            foodName = itemView.findViewById(R.id.tv_food_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onItemClick(position);
        }
    }
}
