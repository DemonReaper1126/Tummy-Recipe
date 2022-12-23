package com.example.tummy_recipe.detail;

import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.model.Meals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;

/* REFER TO MainPresenter FOR COMMENTS */

public class DetailPresenter {

    private final DetailView view;

    public DetailPresenter(DetailView view) {
        this.view = view;
    }

    void getMealById(String mealName){
        view.showLoading();

        /* Making Request to server */
        Utils.getApi().getMealsByName(mealName)
                .enqueue(new Callback<Meals>() {
                    @Override
                    public void onResponse(@NonNull Call<Meals> call, @NonNull Response<Meals> response) {
                        view.hideLoading();
                        if (response.isSuccessful() && response.body() != null){
                            view.setMeals(response.body().getMeals().get(0));
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
