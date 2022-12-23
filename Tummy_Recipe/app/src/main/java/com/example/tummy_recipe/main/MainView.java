package com.example.tummy_recipe.main;

import com.example.tummy_recipe.model.Categories;
import com.example.tummy_recipe.model.Meals;

import java.util.List;

/* CREATES INTERFACE WHICH ARE BE IMPLEMENTED IN MainActivity */
public interface MainView {

    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> meal);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);

}
