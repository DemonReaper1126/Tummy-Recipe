package com.example.tummy_recipe.category;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.adapter.ViewPagerCategoryAdapter;
import com.example.tummy_recipe.main.MainActivity;
import com.example.tummy_recipe.model.Categories;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {

    /* VARIABLE INITIALIZATION */
    /* WHY @SuppressLint("NonConstantResourceId") CAN REFER TO MainActivity.java */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        /* BIND ButterKnife TO THIS ACTIVITY TO BE ABLE TO USE IT'S FUNCTIONS */
        ButterKnife.bind(this);

        /* FUNCTION CALLS */
        initActionBar();
        initIntent();
    }

    /* SETTING VIEWPAGER FOR CATEGORY */
    private void initIntent() {

        /* CREATE INTENT TO GET EXTRA_CATEGORY & EXTRA_POSITION PASSED FROM MainActivity */
        Intent intent = getIntent();
        @SuppressWarnings("unchecked")
        List<Categories.Category> categories =
                (List<Categories.Category>) intent.getSerializableExtra(MainActivity.EXTRA_CATEGORY);
        int position = intent.getIntExtra(MainActivity.EXTRA_POSITION, 0);

        /* INITIALIZE ViewPagerCategoryAdapter TO PASS IN categories */
        /* WITHIN ViewPagerCategoryAdapter CLASS INITIALIZED CategoryFragment */
        ViewPagerCategoryAdapter adapter = new ViewPagerCategoryAdapter(
                getSupportFragmentManager(),
                categories);

        /* SET adapter TO viewPager */
        /* NOTE THAT THE viewPager IN DESIGN VIEW IS ACTUALLY EVERYTHING BELOW THE TOOLBAR */
        viewPager.setAdapter(adapter);

        /* SETUP TOOLBAR WITH VIEWPAGER */
        tabLayout.setupWithViewPager(viewPager);

        /* SET viewPager CURRENT ITEM TO position */
        viewPager.setCurrentItem(position, true);

        /* NOTIFY/ REFRESH adapter THAT DATA WITHIN HAS BEEN CHANGED */
        adapter.notifyDataSetChanged();
    }

    /* SETUP TOOLBAR */
    private void initActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /* SETUP ITEMS IN TOOLBAR */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}