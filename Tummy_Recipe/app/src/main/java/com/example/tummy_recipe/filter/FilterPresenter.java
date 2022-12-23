package com.example.tummy_recipe.filter;

import androidx.annotation.NonNull;

import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.detail.DetailView;
import com.example.tummy_recipe.model.Categories;
import com.example.tummy_recipe.model.Meals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterPresenter {

    private final FilterView view;

    public FilterPresenter(FilterView view) {
        this.view = view;
    }

    void getMealsByFilter(String mealName){
        view.showLoading();

        /* Making Request to server */
        Utils.getApi().getMealsByFilter(mealName)
                .enqueue(new Callback<Meals>() {
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
