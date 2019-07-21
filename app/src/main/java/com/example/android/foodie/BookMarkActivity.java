package com.example.android.foodie;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookMarkActivity extends AppCompatActivity implements BookMarkAdapter.ItemClickListener {

    RecyclerView bookMarkRecyclerView;
    ProgressBar progressBar;
    BookMarkViewModel bookMarkViewModel;
    BookMarkAdapter bookMarkAdapter;
    Paint paint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.loading_indicator);
        progressBar.setVisibility(View.VISIBLE);

        bookMarkRecyclerView = findViewById(R.id.bookmark_recycler);

        //Initializing bookMark adapter
        bookMarkAdapter = new BookMarkAdapter(this, this);

        //Attach bookmark adapter with bookMarkRecyclerView
        bookMarkRecyclerView.setHasFixedSize(true);
        bookMarkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookMarkRecyclerView.setAdapter(bookMarkAdapter);

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
                bookMarkAdapter.removeItem(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = itemView.getBottom() - itemView.getTop();
                    float width = height / 3;

                    paint.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() +
                            dX, (float) itemView.getTop(), (float)
                            itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, paint);

                    icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_delete);

                    RectF icon_dest = new RectF((float) recyclerView.getRight() - 2 * width, (float) itemView.getTop() + width,
                            (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, paint);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }).attachToRecyclerView(bookMarkRecyclerView);

        //Initializing ViewModel and getting BookMarks user stored in Room
        bookMarkViewModel = ViewModelProviders.of(this).get(BookMarkViewModel.class);
        bookMarkViewModel.getBookMarkList().observe(this, new Observer<List<BookMark>>() {
            @Override
            public void onChanged(List<BookMark> bookMarks) {
                if (!bookMarks.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    bookMarkAdapter.setBookMarkList(bookMarks);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "No BooksMarks", Toast.LENGTH_SHORT).show();
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
                        bookMarkRecyclerView.setVisibility(View.GONE);
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
            } else {
                Toast.makeText(getApplicationContext(), "No Bookmarks", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        //Intent to open details about bookmarked item
        Intent intent = new Intent(BookMarkActivity.this, FoodDetailsActiviy.class);
        intent.putExtra("bookmark", bookMarkAdapter.getBookMark(position));
        startActivity(intent);
    }
}
