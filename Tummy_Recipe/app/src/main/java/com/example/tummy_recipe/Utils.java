package com.example.tummy_recipe;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.tummy_recipe.api.FoodApi;
import com.example.tummy_recipe.api.FoodClient;

public class Utils {

    /* INITIALIZE THE FoodApi Interface INTO CLASS */
    public static FoodApi getApi(){
        return FoodClient.getFoodClient().create(FoodApi.class);
    }

    public static void showDialogMessage(Context context, String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).show();
        if (alertDialog.isShowing()){
            alertDialog.cancel();
        }
    }
}
