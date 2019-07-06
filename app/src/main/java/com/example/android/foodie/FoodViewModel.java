package com.example.android.foodie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FoodViewModel extends ViewModel {

    private Repository repository;

    public FoodViewModel (){
        repository = Repository.getInstance();
    }

    LiveData<List<Food>> getFoodList(){
        return repository.getListCategories();
    }

    LiveData<List<FoodCategory>>getFoodCategory(String category){
        return repository.getFoodCatergory(category);
    }

    LiveData<FoodCategory>getFoodDetails(int id){
      return   repository.getFoodDetails(id);
    }

    LiveData<List<FoodCategory>>getRandomFoodList(String page){
        return repository.getFoodByArea(page);
    }
    LiveData<FoodCategory>getRandom(){
        return repository.getRandom();
    }
}
