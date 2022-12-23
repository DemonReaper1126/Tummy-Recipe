package com.example.tummy_recipe.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import androidx.annotation.NonNull;
import com.example.tummy_recipe.R;
import com.example.tummy_recipe.model.Meals;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewMealByCategory extends RecyclerView.Adapter<RecyclerViewMealByCategory.RecyclerViewHolder> {

    /* INITIALIZE VARIABLES */
    private final List<Meals.Meal> meals;
    private final Context context;
    private static ClickListener clickListener;

    /* CREATE CONSTRUCTOR */
    public RecyclerViewMealByCategory(Context context, List<Meals.Meal> meals) {
        this.meals = meals;
        this.context = context;
    }

    /* SET onCreateViewHolder AND INFLATE WITH item_recycler_meal.xml */
    @NonNull
    @Override
    public RecyclerViewMealByCategory.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_meal,
                viewGroup,false);
        return new RecyclerViewHolder(view);
    }

    /* UPDATE RecyclerView.ViewHolder CONTENTS WITH THE ITEM AT THE GIVEN POSITION i */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMealByCategory.RecyclerViewHolder holder, int i) {
        /* i HERE MEANS POSITION */

        /* CREATE NEW VARIABLE WHICH TAKES meals THUMBNAIL(getStrMealThumb()) FROM meals MODEL */
        String strMealThumb = meals.get(i).getStrMealThumb();

        /* PASS THE CREATED strMealThumb INTO REFERENCED mealThumb WITH A SHADOW EFFECT APPLIED */
        /* mealThumb is created and referenced below in RecyclerViewHolder */
        Picasso.get().load(strMealThumb).placeholder(R.drawable.shadow_bottom_to_top).into(holder.mealThumb);

        /* CREATE NEW STRING WHICH TAKES MEAL NAME(getStrMeal()) FROM meals MODEL */
        String strMealName = meals.get(i).getStrMeal();

        /* SET TEXT FOR mealName IN ORDER BASED ON strMealName */
        holder.mealName.setText(strMealName);
    }

    /* GET THE TOTAL NUMBER OF meals */
    @Override
    public int getItemCount() {
        return meals.size();
    }

    /* CREATE STATIC CLASS THAT IMPLEMENTS OnClickListener TO BE USED IN CategoryFragment LINE 113 */
    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* INITIALIZE VARIABLES AND REFERENCES */
        /* WHY @SuppressLint("NonConstantResourceId") CAN REFER TO MainActivity.java */
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.mealThumb)
        ImageView mealThumb;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.mealName)
        TextView mealName;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /* CREATE ONCLICK BASED ON POSITION OF CLICK */
        @Override
        public void onClick(View v) {
            //getBindingAdapterPosition maybe wrong
            //changed from depreciated getAdapterPosition()
            clickListener.onClick(v, getBindingAdapterPosition());
        }
    }

    /* CREATING setOnItemClickListener TO ENABLE RecyclerViewMainAdapter FOR CLICK LISTENER */
    public void setOnItemClickListener(ClickListener clickListener){
        RecyclerViewMealByCategory.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }
}
