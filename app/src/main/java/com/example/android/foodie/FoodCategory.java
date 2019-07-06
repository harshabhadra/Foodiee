package com.example.android.foodie;

import android.os.Parcel;
import android.os.Parcelable;


public class FoodCategory implements Parcelable {

    public static final Creator<FoodCategory> CREATOR = new Creator<FoodCategory>() {
        @Override
        public FoodCategory createFromParcel(Parcel in) {
            return new FoodCategory(in);
        }

        @Override
        public FoodCategory[] newArray(int size) {
            return new FoodCategory[size];
        }
    };

    private int foodId;
    private String foodName;
    private String foodImage;
    private String cookingDetails;
    private String videoUrl;

    public FoodCategory(String foodName, String foodImage, int foodId) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodId = foodId;
    }

    public FoodCategory(String cookingDetails, String videoUrl, String foodImage) {
        this.cookingDetails = cookingDetails;
        this.videoUrl = videoUrl;
        this.foodImage = foodImage;
    }

    protected FoodCategory(Parcel in) {
        foodName = in.readString();
        foodImage = in.readString();
        foodId = in.readInt();
    }

    public String getCookingDetails() {
        return cookingDetails;
    }

    public void setCookingDetails(String cookingDetails) {
        this.cookingDetails = cookingDetails;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodName);
        dest.writeString(foodImage);
        dest.writeInt(foodId);
    }
}
