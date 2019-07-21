package com.example.android.foodie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FoodViewModel extends ViewModel {

    private Repository repository;

    public FoodViewModel (){
        repository = Repository.getInstance();
    }

    //Get list of food categories
    LiveData<List<Food>> getFoodList(){
        return repository.getListCategories();
    }

    //Get food list of a single category
    LiveData<List<FoodCategory>>getFoodCategory(String category){
        return repository.getFoodCatergory(category);
    }

    //Get details of a food item
    LiveData<FoodCategory>getFoodDetails(int id){
      return   repository.getFoodDetails(id);
    }

    //Get food items by area
    LiveData<List<FoodCategory>> getFoodByArea(String area){
        return repository.getFoodByArea(area);
    }

    //Get a random meal
    LiveData<FoodCategory>getRandom(){
        return repository.getRandom();
    }

    //Get Chicken items list
    LiveData<List<FoodCategory>> getOrdinaryDrinkItems(){
        return repository.getDrinkListItem();
    }

    //Get details about a single drink
    LiveData<FoodCategory>getDrinkDetails(int id){
        return repository.getDrinkDetails(id);
    }

    //Get cocktails drinks list
    LiveData<List<FoodCategory>>getCockTailsList(){
        return repository.getCocktailsList();
    }
}
