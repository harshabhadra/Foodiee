package com.example.android.foodie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookMarkActivity extends AppCompatActivity implements BookMarkAdapter.ItemClickListener {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    BookMarkViewModel bookMarkViewModel;
    BookMarkAdapter bookMarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        progressBar = findViewById(R.id.loading_indicator);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.bookmark_recycler);

        //Initializing bookMark adapter
        bookMarkAdapter = new BookMarkAdapter(this, this);

        //Attach bookmark adapter with recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookMarkAdapter);

        //Helper method to delete an item on swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                BookMark bookMark = bookMarkAdapter.getBookMarkPosition(position);
                bookMarkViewModel.deleteBookMark(bookMark);

            }
        }).attachToRecyclerView(recyclerView);

        //Initializing ViewModel and getting BookMarks user stored in Room
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel.class);
        bookMarkViewModel.getBookMarkList().observe(this, new Observer<List<BookMark>>() {
            @Override
            public void onChanged(List<BookMark> bookMarks) {
                if (!bookMarks.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    bookMarkAdapter.setBookMarkList(bookMarks);
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"No BooksMarks", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_bookmarks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.delete_all) {

            //Delete All BookMark
            if (bookMarkAdapter.getBookMarkList() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete All Bookmarks?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookMarkViewModel.delteAll();
                        recyclerView.setVisibility(View.GONE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(BookMarkActivity.this, BookMarkActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(intent);
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                Toast.makeText(getApplicationContext(), "No Bookmarks",Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        //Intent to open details about bookmarked item
        Intent intent = new Intent(BookMarkActivity.this, FoodDetailsActiviy.class);
        intent.putExtra("bookmark",bookMarkAdapter.getBookMark(position));
        startActivity(intent);
    }
}
