package com.example.tummy_recipe.search;

import androidx.annotation.NonNull;

import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.model.Meals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {
    private final SearchView view;

    public SearchPresenter(SearchView view) {
        this.view = view;
    }

    void getMeals(String mealName){
        view.showLoading();
        /* Making Request to server */
        Utils.getApi().getMealsByName(mealName)
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
