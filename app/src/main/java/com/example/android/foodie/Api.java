package com.example.android.foodie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String JsonUrl = "https://www.themealdb.com/";

    String drinkUrl = "https://www.thecocktaildb.com/";

    //Get food by area
    @GET("/api/json/v1/1/filter.php?")
    Call<String> getResponse(@Query("a")String area);

    //Get a random meal
    @GET("/api/json/v1/1/random.php")
    Call<String>getRandomMill();

    //Get food categories
    @GET("/api/json/v1/1/categories.php")
    Call<String> getResponse();

    //Get food by category
    @GET("/api/json/v1/1/filter.php?")
    Call<String>getCategory(@Query("c")String category);

    //Get detail about a food
    @GET("/api/json/v1/1/lookup.php?")
    Call<String>getDetails(@Query("i")int id);

    //Get ordinary drinks
    @GET("/api/json/v1/1/filter.php?c=Ordinary_Drink")
    Call<String> getOrdinaryDrinks();

    //Get cocktail drinks
    @GET("/api/json/v1/1/filter.php?c=Cocktail")
    Call<String>getCockTails();

    //Get drinks details by id
    @GET("/api/json/v1/1/lookup.php?")
    Call<String>getDrinkDetails(@Query("i") int id);
}
