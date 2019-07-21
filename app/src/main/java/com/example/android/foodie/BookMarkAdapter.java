package com.example.android.foodie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.BookMarkViewHolder> {

    //Declaring itemclicklistener
    ItemClickListener itemClickListener;
    private LayoutInflater inflater;
    private List<BookMark> bookMarkList = new ArrayList<>();

    public BookMarkAdapter(Context context, ItemClickListener clickListener) {
        inflater = LayoutInflater.from(context);
        itemClickListener = clickListener;
    }

    @NonNull
    @Override
    public BookMarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bookmark_list, parent, false);
        return new BookMarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookMarkViewHolder holder, int position) {

        if (bookMarkList != null) {
            holder.textView.setText(bookMarkList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        if (bookMarkList != null) {
            return bookMarkList.size();
        }
        return 0;
    }

    public BookMark getBookMark(int position) {
        if (bookMarkList != null) {
            return bookMarkList.get(position);
        }
        return null;
    }

    List<BookMark> getBookMarkList() {
        if (!bookMarkList.isEmpty()) {
            return bookMarkList;
        } else {
            return null;
        }
    }

    public void removeItem(int position){
        bookMarkList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,bookMarkList.size());
    }

    void setBookMarkList(List<BookMark> bookMarkList) {
        this.bookMarkList = bookMarkList;
        notifyDataSetChanged();
    }

    public BookMark getBookMarkPosition(int position) {
        return bookMarkList.get(position);
    }


    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class BookMarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;

        public BookMarkViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.bookmark_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            itemClickListener.onItemClick(position);

        }
    }
}
