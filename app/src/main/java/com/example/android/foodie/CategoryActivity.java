package com.example.android.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.ItemClickListener {


    RecyclerView recyclerView;
    ProgressBar progressBar;
    CategoryAdapter categoryAdapter;
    String TAG = "MainActivity";
    FoodViewModel foodViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Log.d(TAG, "onCreate");

        progressBar = findViewById(R.id.loading);

        //Initializing recyclerView
        recyclerView = findViewById(R.id.category_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Getting category name from the intent
        Intent intent = getIntent();
        Food food = intent.getParcelableExtra("name");

        //Change the value of category as per item
        final String category = food.getFoodName();

        //set title of the activity
        setTitle(category);

        //Initializing foodViewModel and getting list of food item as per category
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        foodViewModel.getFoodCategory(category).observe(this, new Observer<List<FoodCategory>>() {
            @Override
            public void onChanged(List<FoodCategory> foodCategories) {
                if (!foodCategories.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    categoryAdapter = new CategoryAdapter(CategoryActivity.this, foodCategories, CategoryActivity.this);
                    recyclerView.setAdapter(categoryAdapter);

                    Log.e(TAG, "Category Name: " + category);
                } else {
                    Toast.makeText(CategoryActivity.this, "empty list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed");
        finish();
    }

    @Override
    public void onItemClick(int item) {

        //Intent to send the item user clicked to FoodDetailsActivity
        Intent details = new Intent(CategoryActivity.this, FoodDetailsActiviy.class);
        details.putExtra("id", categoryAdapter.getFoodCategories(item));
        startActivity(details);
    }
}
