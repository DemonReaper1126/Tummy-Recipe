package com.example.tummy_recipe.detail;

import com.example.tummy_recipe.model.Meals;

/* CREATES INTERFACE WHICH ARE BE IMPLEMENTED IN DetailActivity */
public interface DetailView {
    void showLoading();
    void hideLoading();
    void setMeals(Meals.Meal meal);
    void onErrorLoading(String message);
}
