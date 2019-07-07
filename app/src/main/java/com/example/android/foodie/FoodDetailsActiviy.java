package com.example.android.foodie;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class FoodDetailsActiviy extends AppCompatActivity {

    ImageView imageView;
    int id;
    BookMarkViewModel bookMarkViewModel;
    FoodViewModel foodViewModel;

    BookMark bookMark;

    Button videoButton;
    TextView textView;
    String videoUrl;
    String name;
    String imgUrl;
    String details;

    private int CONSTANT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details_activiy);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.how_to);
        videoButton = findViewById(R.id.video_button);
        videoButton.setEnabled(false);
        videoButton.setVisibility(View.GONE);

        //Initializing bookmark view model
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel.class);
        //Initializing foodViewModel and getting details of a particular food item
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        //Getting values of item from the Intent as per activity
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            CONSTANT++;
            FoodCategory foodCategory = intent.getParcelableExtra("id");
            id = foodCategory.getFoodId();
            name = foodCategory.getFoodName();

            setTitle(name);
        } else if (intent.hasExtra("bookmark")) {
            BookMark bookMark = intent.getParcelableExtra("bookmark");
            id = bookMark.getItemId();
            name = bookMark.getName();

            setTitle(name);
        }else if (intent.hasExtra("randomId")){
            CONSTANT++;
            id = intent.getIntExtra("randomId",0);
            name = intent.getStringExtra("name");

            setTitle(name);
        }else if (intent.hasExtra("areaId")){
            CONSTANT++;
            FoodCategory foodCategory = intent.getParcelableExtra("areaId");
            id = foodCategory.getFoodId();
            name = foodCategory.getFoodName();

            setTitle(name);
        }else if (intent.hasExtra("drinkId")){
            CONSTANT++;
            FoodCategory foodCategory = intent.getParcelableExtra("drinkId");
            id = foodCategory.getFoodId();
            name = foodCategory.getFoodName();

            foodViewModel.getDrinkDetails(id).observe(this, new Observer<FoodCategory>() {
                @Override
                public void onChanged(FoodCategory foodCategory) {
                    if (foodCategory != null) {
                        Picasso.get().load(foodCategory.getFoodImage())
                                .resize(4000, 6000)
                                .onlyScaleDown()
                                .centerInside()
                                .into(imageView);
                        textView.setText(foodCategory.getCookingDetails());
                    }else {
                        Toast.makeText(getApplicationContext(), "Empty Drink", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        foodViewModel.getFoodDetails(id).observe(this, new Observer<FoodCategory>() {
            @Override
            public void onChanged(FoodCategory foodCategory) {

                details = foodCategory.getCookingDetails();
                textView.setText(details);
                videoUrl = foodCategory.getVideoUrl();
                Toast.makeText(getApplicationContext(),videoUrl,Toast.LENGTH_SHORT).show();
                imgUrl = foodCategory.getFoodImage();
                Picasso.get().load(imgUrl).placeholder(R.mipmap.icon).into(imageView);
                videoButton.setEnabled(true);
                videoButton.setVisibility(View.VISIBLE);
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(FoodDetailsActiviy.this, YoutubeVideoActivity.class);
                videoIntent.putExtra("name", name);
                videoIntent.putExtra("url",videoUrl);
                videoIntent.putExtra("details",details);
                startActivity(videoIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_details, menu);
        if (CONSTANT == 0){
            menu.findItem(R.id.bookmark).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id1 = item.getItemId();

        //Adding bookmark on a item
        if (id1 == R.id.bookmark) {
            Snackbar.make(findViewById(R.id.coordinator), "Bookmark Added", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new MyUndoListener()).show();
            bookMark = new BookMark(id, name);
            bookMarkViewModel.insertBookMark(bookMark);
            return true;
        }

        //Share food recipe with friends & family
        if (id1 == R.id.share_details) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "How to Cook " + name + "\n"+ videoUrl);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,"Share With"));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            bookMarkViewModel.deleteBookMark(bookMark);
            Snackbar.make(findViewById(R.id.coordinator), "Bookmark Removed", Snackbar.LENGTH_LONG).show();
        }
    }


}
