package com.example.tummy_recipe.main;

import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.model.Categories;
import com.example.tummy_recipe.model.Meals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;

/* RETROFIT IS A POWERFUL REST CLIENT WHICH HELPS AUTHENTICATE OR INTERACT WITH API ALONG WITH
SENDING NETWORK REQUESTS WITH OKHTTP */

class MainPresenter {

    /* INITIALIZE MainView */
    private final MainView view;

    /* BUILD CONSTRUCTOR THAT PASSES IN MainView */
    public MainPresenter(MainView view) {
        this.view = view;
    }

    /* CREATES FUNCTION getMeals TO GET ALL MEALS FROM Meal Database(theMealDB) */
    void getMeals(){

        /* showLoading function call -> FUNCTION IN MainActivity */
        view.showLoading();

        /* CREATE mealsCall function using retrofit.Call */
        /* GET MEAL FROM API USING FUNCTION FROM Utils.java  */
        Call<Meals> mealsCall = Utils.getApi().getMeal();

        /* enqueue -> send the request and notify callback of its response  */
        mealsCall.enqueue(new Callback<Meals>() {

            /* IF RECEIVES A RESPONSE FROM mealsCall */
            @Override
            public void onResponse(@NonNull Call<Meals> call, @NonNull Response<Meals> response) {
                /* HIDE THE LOADING */
                view.hideLoading();

                /* IF RESPONSE IS SUCCESSFUL AND NOT NULL */
                if (response.isSuccessful() && response.body() != null){

                    /* SET RESPONSE.getMeals() INTO view */
                    view.setMeal(response.body().getMeals());
                }

                /* IF RESPONSE EITHER NOT SUCCESSFUL OR IS NULL */
                else{
                    /* SHOW ERROR MESSAGE */
                    view.onErrorLoading(response.message());
                }

            }

            /* IF RESPONSE FAILURE */
            @Override
            public void onFailure(@NonNull Call<Meals> call, @NonNull Throwable t) {

                /* SHOWS ERROR MESSAGE AND HIDES LOADING */
                view.onErrorLoading(t.getLocalizedMessage());
                view.hideLoading();
            }
        });
    }

    /* CREATES FUNCTION getCategories TO GET ALL MEAL CATEGORIES FROM Meal Database(theMealDB) */
    void getCategories(){

        /* THE REST IS SIMILAR TO getMeals() */
        view.showLoading();
        Call<Categories> categoriesCall = Utils.getApi().getCategories();
        categoriesCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(@NonNull Call<Categories> call, @NonNull Response<Categories> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null){
                    view.setCategory(response.body().getCategories());
                }
                else{
                    view.onErrorLoading(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Categories> call, @NonNull Throwable t) {
                view.onErrorLoading(t.getLocalizedMessage());
                view.hideLoading();
            }
        });
    }

}
