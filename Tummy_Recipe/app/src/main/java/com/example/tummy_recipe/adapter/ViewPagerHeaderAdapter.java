package com.example.tummy_recipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.model.Meals;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerHeaderAdapter extends PagerAdapter {

    /* VARIABLE INITIALIZATION */
    private final List<Meals.Meal> meals;
    private final Context context;
    private static ClickListener clickListener;

    /* CREATE CONSTRUCTOR */
    public ViewPagerHeaderAdapter(List<Meals.Meal> meals, Context context) {
        this.meals = meals;
        this.context = context;
    }

    /* CREATING setOnItemClickListener TO ENABLE ViewPagerHeaderAdapter FOR CLICK LISTENER */
    public void setOnitemClickListener(ClickListener clickListener){
        ViewPagerHeaderAdapter.clickListener = clickListener;
    }

    /* GET THE TOTAL NUMBER OF MEALS */
    @Override
    public int getCount() {
        return meals.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_view_pager_header,
                container,
                false
        );

        /* SETTING REFERENCES ON MainActivity mealThumb & mealName */
        ImageView mealThumb = view.findViewById(R.id.mealThumb);
        TextView mealName = view.findViewById(R.id.mealName);

        /* CREATE NEW VARIABLE WHICH TAKES MEAL THUMBNAIL(getStrMealThumb()) FROM meals MODEL */
        /* AS MEAL THUMBNAIL IS PRESENTED WITH A URL, THAT'S THE REASON FOR CASTING A STRING INTO IMAGEVIEW */
        String strMealThumb = meals.get(position).getStrMealThumb();

        /* PASS THE CREATED STRING INTO REFERENCED mealThumb */
        Picasso.get().load(strMealThumb).into(mealThumb);

        /* CREATE NEW VARIABLE WHICH TAKES MEAL NAME(getStrMeal()) FROM meals MODEL */
        String strMealName = meals.get(position).getStrMeal();

        /* PASS THE CREATED STRING INTO REFERENCED mealName */
        mealName.setText(strMealName);

        view.setOnClickListener(v -> clickListener.onClick(v, position));

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View)object);
    }

    public interface ClickListener{
        void onClick(View v, int position);
    }
}
