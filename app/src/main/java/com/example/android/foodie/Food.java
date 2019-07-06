package com.example.android.foodie;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {

    private String imageUrl;
    private String foodName;

    public Food(String imageUrl, String foodName) {
        this.imageUrl = imageUrl;
        this.foodName = foodName;
    }

    protected Food(Parcel in) {
        imageUrl = in.readString();
        foodName = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(foodName);
    }
}
