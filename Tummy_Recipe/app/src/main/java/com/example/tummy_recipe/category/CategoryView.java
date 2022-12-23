package com.example.tummy_recipe.category;

import com.example.tummy_recipe.model.Meals;

import java.util.List;

/* CREATES INTERFACE WHICH ARE BE IMPLEMENTED IN CategoryFragment */
public interface CategoryView {
    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meals);
    void onErrorLoading(String message);
}
