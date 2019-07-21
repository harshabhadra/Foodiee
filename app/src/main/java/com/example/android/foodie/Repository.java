package com.example.android.foodie;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Repository {

    //Store list of categories
    private MutableLiveData<List<Food>> listOfCategories;

    //Store list of food item as per category
    private MutableLiveData<List<FoodCategory>> categoryData = new MutableLiveData<>();

    //Store details about one Food item
    private MutableLiveData<FoodCategory> detailsData = new MutableLiveData<>();

    //Store list of food items as per area
    private MutableLiveData<List<FoodCategory>> foodByArea = new MutableLiveData<>();

    //Store details of a random food
    private MutableLiveData<FoodCategory> random = new MutableLiveData<>();

    //Store Chicken items data
    private MutableLiveData<List<FoodCategory>> ordinaryDrinkListDATA = new MutableLiveData<>();

    //Store drink details
    private MutableLiveData<FoodCategory>drinkDetails = new MutableLiveData<>();

    //Store cocktail drinks list
    private MutableLiveData<List<FoodCategory>>cocktailDrinksList = new MutableLiveData<>();

    private FoodDao foodDao;

    private LiveData<List<BookMark>> liveData;

    private Repository() {

    }

    Repository(Application application) {
        FoodDatabase database = FoodDatabase.getDatabase(application);
        foodDao = database.foodDao();
        liveData = foodDao.getBookMarkList();
    }

    public static Repository getInstance() {
        return new Repository();
    }

    //Get All BookMarks from database
    LiveData<List<BookMark>> getAllBookMarks() {
        return liveData;
    }

    //This method will insert bookmark into room
    public void insertBookMark(BookMark bookMark) {

        new insertAsyncTask(foodDao).execute(bookMark);
    }

    //This method will delete all bookmarks
    public void deleteAllBookMarks() {

        new deleteAllAsyncTask(foodDao).execute();
    }

    //This method will delete a single bookmark from room
    public void deleteBookMark(BookMark bookMark) {

        new deleteAsyncTask(foodDao).execute(bookMark);
    }

    //Method to get list of food categories
    LiveData<List<Food>> getListCategories() {

        if (listOfCategories == null) {
            listOfCategories = new MutableLiveData<>();

            loadFood();
        }

        return listOfCategories;
    }

    //Get list of foods by area
    LiveData<List<FoodCategory>> getFoodByArea(String area) {

        loadFoodByArea(area);
        return foodByArea;
    }

    //Get a random meal
    LiveData<FoodCategory> getRandom() {
        loadRandom();
        return random;
    }

    //Get details about a food item
    LiveData<FoodCategory> getFoodDetails(int id) {
        loadDetails(id);
        return detailsData;
    }

    //Get details of a drink
    LiveData<FoodCategory>getDrinkDetails(int id){
        loadDrinkDetails(id);
        return  drinkDetails;
    }

    //Get list of foods by category
    LiveData<List<FoodCategory>> getFoodCatergory(String category) {
        loadFoodByCategory(category);
        return categoryData;
    }

    //Get list of ordinary drinks
    LiveData<List<FoodCategory>> getDrinkListItem(){
        loadDrinksListItem();
        return ordinaryDrinkListDATA;
    }

    //Get list of cocktail drinks
    LiveData<List<FoodCategory>>getCocktailsList(){
        loadCocktailDrinks();
        return  cocktailDrinksList;
    }


    //Network call to get cocktails drinks list
    private void loadCocktailDrinks(){

        final List<FoodCategory>cocktailList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.drinkUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<String>call = api.getCockTails();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body()!= null){
                    String json = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("drinks");

                        for (int i =0; i<jsonArray.length(); i++){
                            JSONObject chickenObj = jsonArray.getJSONObject(i);
                            String name = chickenObj.getString("strDrink");
                            String imgUrl = chickenObj.getString("strDrinkThumb");
                            int id = chickenObj.getInt("idDrink");

                            FoodCategory foodCategory = new FoodCategory(name,imgUrl,id);
                            cocktailList.add(foodCategory);
                            cocktailDrinksList.setValue(cocktailList);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    Log.e("Repository","empty drink list");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //Network call to get drink items
    private void loadDrinksListItem(){
        final List<FoodCategory>oridinaryDrinkList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.drinkUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<String>call = api.getOrdinaryDrinks();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body()!= null){
                    String json = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("drinks");

                        for (int i =0; i<jsonArray.length(); i++){
                            JSONObject chickenObj = jsonArray.getJSONObject(i);
                            String name = chickenObj.getString("strDrink");
                            String imgUrl = chickenObj.getString("strDrinkThumb");
                            int id = chickenObj.getInt("idDrink");

                            FoodCategory foodCategory = new FoodCategory(name,imgUrl,id);
                            oridinaryDrinkList.add(foodCategory);
                            ordinaryDrinkListDATA.setValue(oridinaryDrinkList);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    Log.e("Repository","empty drink list");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    //Network call to get list of food by area
    private void loadFoodByArea(String area) {
        final List<FoodCategory> foodCategoryList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.JsonUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<String> call = api.getResponse(area);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String json = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("meals");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject foodObj = jsonArray.getJSONObject(i);
                            String image = foodObj.getString("strMealThumb");
                            String name = foodObj.getString("strMeal");
                            int id = foodObj.getInt("idMeal");
                            FoodCategory foodCategory = new FoodCategory(name, image, id);
                            foodCategoryList.add(foodCategory);
                            foodByArea.setValue(foodCategoryList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.e("Repository","empty meal list");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //Network call to get a random meal
    private void loadRandom() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.JsonUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.getRandomMill();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String json = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("meals");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("strMeal");
                            String image = object.getString("strMealThumb");
                            int id = object.getInt("idMeal");

                            Log.e("Repository", "Food Name: " + name);
                            FoodCategory foodCategory = new FoodCategory(name, image, id);
                            random.setValue(foodCategory);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //Network call to get drink's details
    private void loadDrinkDetails(int id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.drinkUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<String>call = api.getDrinkDetails(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null){
                    String json = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("drinks");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String details = object.getString("strInstructions");
                            String image = object.getString("strDrinkThumb");

                            FoodCategory dDetails = new FoodCategory(image,details);
                            drinkDetails.setValue(dDetails);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //Network call to get details about a food
    private void loadDetails(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.JsonUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.getDetails(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String json = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("meals");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String details = object.getString("strInstructions");
                            String videoUrl = object.getString("strYoutube");
                            String image = object.getString("strMealThumb");

                            FoodCategory foodDetails = new FoodCategory(details, videoUrl, image);

                            detailsData.setValue(foodDetails);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //Network call to get list of foods by category
    private void loadFoodByCategory(String s) {
        final List<FoodCategory> foodCategories = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.JsonUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.getCategory(s);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String json = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("meals");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("strMeal");
                            String image = object.getString("strMealThumb");
                            int id = object.getInt("idMeal");

                            Log.e("Repository", "Food Name: " + name);
                            FoodCategory foodCategory = new FoodCategory(name, image, id);
                            foodCategories.add(foodCategory);
                            categoryData.setValue(foodCategories);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //Network call to get a list of food categories
    private void loadFood() {

        final List<Food> foodList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.JsonUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<String> call = api.getResponse();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String jsonResponse = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("categories");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject foodObj = jsonArray.getJSONObject(i);
                            String image_url = foodObj.getString("strCategoryThumb");
                            String name = foodObj.getString("strCategory");
                            Food food = new Food(image_url, name);
                            foodList.add(food);
                            listOfCategories.setValue(foodList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.d("Repository", "Unable to fetch listOfCategories");
            }
        });
    }

    //AsyncTask which will insert bookmark in background
    private static class insertAsyncTask extends AsyncTask<BookMark, Void, Void> {

        FoodDao foodDao;

        insertAsyncTask(FoodDao dao) {
            foodDao = dao;
        }

        @Override
        protected Void doInBackground(BookMark... bookMarks) {

            foodDao.insertBookMark(bookMarks[0]);
            return null;
        }
    }

    //This method will delete all bookmarks in background
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        FoodDao foodDao;

        deleteAllAsyncTask(FoodDao foodDao) {
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            foodDao.deleteAll();
            return null;
        }
    }

    //AsyncTask to delete a single bookmark
    private static class deleteAsyncTask extends AsyncTask<BookMark, Void, Void> {

        FoodDao foodDao;

        deleteAsyncTask(FoodDao foodDao) {
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(BookMark... bookMarks) {

            foodDao.deleteBookMark(bookMarks[0]);
            return null;
        }
    }
}
