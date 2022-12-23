package com.example.tummy_recipe.api;

import com.example.tummy_recipe.model.Categories;
import com.example.tummy_recipe.model.Meals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {

    /* INITIALIZE CALL get--- INTERFACE */
    /* ALL CALL FUNCTION IN PRESENTERS(MainPresenter, CategoryPresenter, DetailPresenter) */
    @GET("latest.php")
    Call<Meals> getMeal();

    @GET("categories.php")
    Call<Categories> getCategories();

    @GET("filter.php")
    Call<Meals> getMealsByCategory(@Query("c") String category);

    @GET("search.php")
    Call<Meals> getMealsByName(@Query("s") String mealName);

    @GET("filter.php")
    Call<Meals> getMealsByFilter(@Query("i") String mealName);



}
