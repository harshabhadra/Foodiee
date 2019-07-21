package com.example.android.foodie;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

public class FoodDetailsActiviy extends AppCompatActivity {

    ImageView imageView;
    int id;
    BookMarkViewModel bookMarkViewModel;
    FoodViewModel foodViewModel;
    CardView youTubeCard;
    LinearLayout mainContent;
    ProgressBar loadingDetails;
    LinearLayout noInternetLayout;

    BookMark bookMark;
    TextView textView;
    String videoUrl;
    String name;
    String imgUrl;
    String details;
    String videoCode;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    YouTubePlayerSupportFragment fragment;
    YouTubePlayer player;
    private int CONSTANT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details_activiy);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.how_to);
        youTubeCard = findViewById(R.id.youtube_card);
        mainContent = findViewById(R.id.main_content);
        loadingDetails = findViewById(R.id.loading_details);
        noInternetLayout = findViewById(R.id.no_internet_food_details);

        //Checking the Network state of the Device
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        fragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_player_fragment);

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
        } else if (intent.hasExtra("randomId")) {
            CONSTANT++;
            id = intent.getIntExtra("randomId", 0);
            name = intent.getStringExtra("name");

            setTitle(name);
        } else if (intent.hasExtra("areaId")) {
            CONSTANT++;
            FoodCategory foodCategory = intent.getParcelableExtra("areaId");
            id = foodCategory.getFoodId();
            name = foodCategory.getFoodName();

            setTitle(name);
        } else if (intent.hasExtra("drinkId")) {
            CONSTANT++;
            FoodCategory foodCategory = intent.getParcelableExtra("drinkId");
            id = foodCategory.getFoodId();
            name = foodCategory.getFoodName();
            youTubeCard.setVisibility(View.GONE);

            if (networkInfo != null && networkInfo.isConnected()) {
                foodViewModel.getDrinkDetails(id).observe(this, new Observer<FoodCategory>() {
                    @Override
                    public void onChanged(FoodCategory foodCategory) {
                        mainContent.setVisibility(View.VISIBLE);
                        loadingDetails.setVisibility(View.GONE);
                        if (foodCategory != null) {
                            Picasso.get().load(foodCategory.getFoodImage())
                                    .resize(4000, 6000)
                                    .onlyScaleDown()
                                    .centerInside()
                                    .into(imageView);
                            textView.setText(foodCategory.getCookingDetails());
                        } else {
                            Toast.makeText(getApplicationContext(), "Empty Drink", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                loadingDetails.setVisibility(View.GONE);
                noInternetLayout.setVisibility(View.VISIBLE);
            }
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            foodViewModel.getFoodDetails(id).observe(this, new Observer<FoodCategory>() {
                @Override
                public void onChanged(FoodCategory foodCategory) {

                    mainContent.setVisibility(View.VISIBLE);
                    loadingDetails.setVisibility(View.GONE);
                    details = foodCategory.getCookingDetails();
                    textView.setText(details);
                    videoUrl = foodCategory.getVideoUrl();
                    String[] seperator = videoUrl.split("=");
                    String url = seperator[0];
                    videoCode = seperator[1];

                    youTubeCard.setVisibility(View.VISIBLE);
                    imgUrl = foodCategory.getFoodImage();
                    Picasso.get().load(imgUrl).placeholder(R.mipmap.icon)
                            .fit()
                            .centerCrop()
                            .into(imageView);
                    fragment.initialize(Config.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            if (!b) {
                                player = youTubePlayer;
                                player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                                player.loadVideo(videoCode);
                            }
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
                }
            });
        } else {
            loadingDetails.setVisibility(View.GONE);
            noInternetLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_details, menu);
        if (CONSTANT == 0) {
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
            sendIntent.putExtra(Intent.EXTRA_TEXT, "How to Cook " + name + "\n" + videoUrl);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share With"));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public class loadVideoAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            bookMarkViewModel.deleteBookMark(bookMark);
            Snackbar.make(findViewById(R.id.coordinator), "Bookmark Removed", Snackbar.LENGTH_LONG).show();
        }
    }


}
