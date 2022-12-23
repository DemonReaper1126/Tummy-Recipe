package com.example.tummy_recipe.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tummy_recipe.category.CategoryFragment;
import com.example.tummy_recipe.model.Categories;

import java.util.List;

public class ViewPagerCategoryAdapter extends FragmentPagerAdapter {

    /* VARIABLE INITIALIZATION */
    private final List<Categories.Category> categories;

    /* CREATE CONSTRUCTOR */
    public ViewPagerCategoryAdapter(@NonNull FragmentManager fm, List<Categories.Category> categories) {
        super(fm);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {

        /* INITIALIZE CategoryFragment */
        /* LINKS CategoryFragment.java WITH viewPagerCategoryAdapter */
        CategoryFragment fragment = new CategoryFragment();

        /* CREATE BUNDLE TO PASS STRINGS TO ANOTHER ACTIVITY */
        Bundle args = new Bundle();

        /* PUT STRING CATEGORY(getStrCategory()) INTO KEY "EXTRA_DATA_NAME" */
        args.putString("EXTRA_DATA_NAME", categories.get(i).getStrCategory());
        /* PUT STRING CATEGORY(getStrCategoryDescription()) INTO KEY "EXTRA_DATA_DESC" */
        args.putString("EXTRA_DATA_DESC", categories.get(i).getStrCategoryDescription());
        /* PUT STRING CATEGORY(getStrCategoryThumb()) INTO KEY "EXTRA_DATA_IMAGE" */
        args.putString("EXTRA_DATA_IMAGE", categories.get(i).getStrCategoryThumb());

        /* TRANSFER BUNDLE INTO FRAGMENT */
        fragment.setArguments(args);
        return fragment;
    }

    /* GET THE TOTAL NUMBER OF CATEGORIES */
    @Override
    public int getCount() {
        return categories.size();
    }

    /* SETTING tabLayout(activity_category.xml) TITLE WHICH CORRESPONDS TO EACH CATEGORY */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return categories.get(position).getStrCategory();
    }

}
