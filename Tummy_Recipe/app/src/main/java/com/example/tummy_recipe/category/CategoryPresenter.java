package com.example.tummy_recipe.category;

import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.model.Meals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;

/* REFER TO MainPresenter FOR COMMENTS */

public class CategoryPresenter {
    private final CategoryView view;

    public CategoryPresenter(CategoryView view) {
        this.view = view;
    }

    void getMealByCategory(String category){
        view.showLoading();
        Call<Meals> mealsCall = Utils.getApi().getMealsByCategory(category);
        mealsCall.enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(@NonNull Call<Meals> call, @NonNull Response<Meals> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null){
                    view.setMeals(response.body().getMeals());
                }
                else{
                    view.onErrorLoading(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Meals> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}
