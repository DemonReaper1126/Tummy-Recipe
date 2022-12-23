package com.example.tummy_recipe.filter;

import com.example.tummy_recipe.model.Meals;

import java.util.List;

public interface FilterView {
    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meal);
    void onErrorLoading(String message);
}
