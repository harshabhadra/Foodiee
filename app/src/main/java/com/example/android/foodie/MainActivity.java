package com.example.android.foodie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FoodAdapter.FoodItemClickListener,
        RandomAdapter.AreaItemClickListener,
        AdapterView.OnItemSelectedListener, ChickenAdapter.OnChickenItemClickListener {

    RecyclerView recyclerView;
    RecyclerView areaRecycler;
    RecyclerView chickenRecycler;

    RandomAdapter randomAdapter;
    FoodAdapter foodAdapter;
    ChickenAdapter chickenAdapter;

    ProgressBar progressBar;

    FoodViewModel foodViewModel;

    TextView heading;
    TextView heading2;
    TextView chicken;
    TextView randomFood;
    ImageView randomImage;
    CardView randomCard;

    int randomId;
    String area = "";
    String name;
    Spinner areaSpinner;
    ProgressBar areaLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting up spinner to choose area
        areaSpinner = findViewById(R.id.area_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.area_list, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(arrayAdapter);
        areaSpinner.setOnItemSelectedListener(this);

        areaLoading = findViewById(R.id.area_loading);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        heading = findViewById(R.id.heading);
        heading2 = findViewById(R.id.heading2);
        chicken = findViewById(R.id.chicken);

        randomImage = findViewById(R.id.random_img);
        randomFood = findViewById(R.id.random_food);
        randomCard = findViewById(R.id.random_card);

        //Open FoodDetailsActivity when use click on Foodie's special card
        randomCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodDetailsActiviy.class);
                intent.putExtra("randomId", randomId);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        //Initializing category recyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        //Initializing area recyclerView
        areaRecycler = findViewById(R.id.random_recycler);
        areaRecycler.setHasFixedSize(true);
        areaRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        //Initializing Chicken recyclerView and getting chicken item list
        chickenRecycler = findViewById(R.id.chicken_recycler);
        chickenRecycler.setHasFixedSize(true);
        chickenRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));


        //Initializing foodViewModel and getting list of food categories
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        foodViewModel.getFoodList().observe(MainActivity.this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                if (foods.size() != 0) {
                    progressBar.setVisibility(View.GONE);
                    heading.setVisibility(View.VISIBLE);
                    foodAdapter = new FoodAdapter(MainActivity.this, MainActivity.this, foods);
                    recyclerView.setAdapter(foodAdapter);
                } else {
                    Log.d("MainActivity", "Failed to fetch data");
                    Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_LONG).show();
                }
            }

        });

        //Getting a food item randomly
        foodViewModel.getRandom().observe(this, new Observer<FoodCategory>() {
            @Override
            public void onChanged(FoodCategory foodCategory) {
                if (foodCategory != null) {
                    Picasso.get().load(foodCategory.getFoodImage())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_background)
                            .resize(4000, 6000)
                            .onlyScaleDown()
                            .centerInside()
                            .into(randomImage);
                    name = foodCategory.getFoodName();
                    randomId = foodCategory.getFoodId();
                    randomFood.setText(name);
                    randomCard.setVisibility(View.VISIBLE);
                }

            }
        });

        //Getting chicken items list
        foodViewModel.getChickenItems().observe(this, new Observer<List<FoodCategory>>() {
            @Override
            public void onChanged(List<FoodCategory> foodCategoryList) {
                if (foodCategoryList != null) {
                    chicken.setVisibility(View.VISIBLE);
                     chickenAdapter = new ChickenAdapter(MainActivity.this, MainActivity.this, foodCategoryList);
                    chickenRecycler.setAdapter(chickenAdapter);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.bookmark_menu) {

            //Start BookMarkActivity when user click on bookmark
            Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int item) {

        //Starting CategoryActivity when user click on any category
        Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
        categoryIntent.putExtra("name", foodAdapter.getFood(item));
        startActivity(categoryIntent);
    }

    @Override
    public void onAreaClick(int position) {

        //Starting FoodDetailsActivity when user click on any item from areaRecycler
        Intent detailIntent = new Intent(MainActivity.this, FoodDetailsActiviy.class);
        detailIntent.putExtra("areaId", randomAdapter.getAreaFood(position));
        startActivity(detailIntent);
    }

    @Override
    public void onChickenItemClick(int position) {



        Intent intent = new Intent(MainActivity.this, FoodDetailsActiviy.class);
        intent.putExtra("drinkId", chickenAdapter.getDrink(position));
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Set the value of area as per item selected in spinner
        area = parent.getItemAtPosition(position).toString();

        //Getting list of food item as per area
        foodViewModel.getFoodByArea(area).observe(this, new Observer<List<FoodCategory>>() {
            @Override
            public void onChanged(List<FoodCategory> foodCategoryList) {
                if (foodCategoryList != null) {
                    heading2.setVisibility(View.VISIBLE);
                    areaSpinner.setVisibility(View.VISIBLE);
                    areaLoading.setVisibility(View.GONE);
                    randomAdapter = new RandomAdapter(MainActivity.this, MainActivity.this, foodCategoryList);
                    areaRecycler.setAdapter(randomAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
