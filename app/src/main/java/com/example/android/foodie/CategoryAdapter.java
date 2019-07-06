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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private LayoutInflater inflater;

    //List to store categories
    private List<FoodCategory> foodCategories;
    private ItemClickListener clickListener;

    //Interface to implement click listener to recycler view
    public interface ItemClickListener{
        void onItemClick(int item);
    }

    public CategoryAdapter(Context context, List<FoodCategory> foodCategories, ItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.foodCategories = foodCategories;
        clickListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.categoty_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Picasso.get().load(foodCategories.get(position).getFoodImage())
                .placeholder(R.mipmap.icon)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(foodCategories.get(position).getFoodName());
    }

    public FoodCategory getFoodCategories(int index){
        if (!foodCategories.isEmpty()){
            return foodCategories.get(index);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return foodCategories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            textView = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onItemClick(position);
        }
    }
}
