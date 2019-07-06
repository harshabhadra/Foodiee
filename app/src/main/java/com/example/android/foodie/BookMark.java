package com.example.android.foodie;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmark_table")
public class BookMark implements Parcelable {

    @PrimaryKey
    int itemId;

    @ColumnInfo(name = "food_name")
    String name;

    public BookMark(int itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }

    protected BookMark(Parcel in) {
        itemId = in.readInt();
        name = in.readString();
    }

    public static final Creator<BookMark> CREATOR = new Creator<BookMark>() {
        @Override
        public BookMark createFromParcel(Parcel in) {
            return new BookMark(in);
        }

        @Override
        public BookMark[] newArray(int size) {
            return new BookMark[size];
        }
    };

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(name);
    }
}
