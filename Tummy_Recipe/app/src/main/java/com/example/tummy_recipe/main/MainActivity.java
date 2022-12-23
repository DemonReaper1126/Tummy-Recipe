package com.example.tummy_recipe.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.filter.FilterChoices;
import com.example.tummy_recipe.search.SearchActivity;
import com.example.tummy_recipe.seen.SeenActivity;
import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.adapter.RecyclerViewMainAdapter;
import com.example.tummy_recipe.adapter.ViewPagerHeaderAdapter;
import com.example.tummy_recipe.authentication.LoginActivity;
import com.example.tummy_recipe.category.CategoryActivity;
import com.example.tummy_recipe.detail.DetailActivity;
import com.example.tummy_recipe.model.Categories;
import com.example.tummy_recipe.model.Meals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{

    /* VARIABLE INITIALIZATION */
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DETAIL = "detail";

    /* USING BUTTERKNIFE METHOD AS IT SAVES LINES OF CODE SUCH AS findViewById */
    /* @SuppressLint("NonConstantResourceId") AS IT IS BEING REPLACED BY ViewBinding */
    /* SAID BY JAKE WHARTON HIMSELF : https://github.com/JakeWharton/butterknife/#readme */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.viewPagerHeader)
    ViewPager viewPagerMeal;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerCategory)
    RecyclerView recyclerViewCategory;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.searchEditText)
    EditText editText;

    MainPresenter presenter;
    private long lastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* BIND ButterKnife TO THIS ACTIVITY TO BE ABLE TO USE IT'S FUNCTIONS */
        ButterKnife.bind(this);

        searchFunction();

        /* SET presenter WITH MainPresenter */
        presenter = new MainPresenter(this);

        /* CALL presenter FUNCTIONS getMeals() & getCategories() */
        presenter.getMeals();
        presenter.getCategories();
    }

    /* CREATING SEARCH FUNCTION */
    private void searchFunction() {
        editText.setOnKeyListener((v, keyCode, event) -> {

            /* IF EVENT IS A KEY ACTION DOWN EVENT ALONG WITH "ENTER" */
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                // Perform action on key press
                Toast.makeText(this, "Enter working! ", Toast.LENGTH_SHORT).show();

                String searchText = editText.getText().toString();

                /* CREATES NEW INTENT AND SENDS TO SearchActivity */
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);

                /* PASS mealName INTO KEY SEARCH */
                intent.putExtra("SEARCH", searchText);

                /* START NEW ACTIVITY WITH PASSED intent VARIABLE */
                startActivity(intent);
                return true;
            }
            return false;
        });
    }


    /* WHEN USER PRESSES BACK BUTTON TWICE, CLOSES APPLICATION */
    @Override
    public void onBackPressed() {

        if (lastClickTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            this.finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        lastClickTime = System.currentTimeMillis();
    }

    /* CREATES ONCLICK FUNCTION AFTER USER CLICKS ON "SEEN" BUTTON, GOES TO SeenActivity */
    public void onLastSeen(View view) {

        /* PREVENTING MULTI CLICKS */
        /* INTERVAL OF 1 SECONDS BEFORE NEXT CLICK CAN BE REGISTERED */
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        /* GOES TO SeenActivity */
        Intent intent = new Intent(getApplicationContext(), SeenActivity.class);
        startActivity(intent);

    }

    /* CREATES ONCLICK FUNCTION AFTER USER CLICKS ON "SEEN" BUTTON, GOES TO FilterChoices */
    public void onFilterClick(View view) {

        /* PREVENTING MULTI CLICKS */
        /* INTERVAL OF 1 SECONDS BEFORE NEXT CLICK CAN BE REGISTERED */
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        /* GOES TO FilterChoices */
        Intent intent = new Intent(getApplicationContext(), FilterChoices.class);
        startActivity(intent);
    }

    /* CREATES ONCLICK FUNCTION AFTER USER CLICKS ON "LOGOUT" BUTTON, SIGNS OUT AND RETURNS TO LoginActivity */
    public void onLogoutClick(View view) {

        /* PREVENTING MULTI CLICKS */
        /* INTERVAL OF 1 SECONDS BEFORE NEXT CLICK CAN BE REGISTERED */
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /* ALL BELOW FUNCTIONS ARE CALLED IN MainView */

    /* CREATE showLoading FUNCTION */
    /* SETTING shimmerMeal(ViewPager) & shimmerCategory TO VISIBLE */
    @Override
    public void showLoading() {
        findViewById(R.id.shimmerMeal).setVisibility(View.VISIBLE);
        findViewById(R.id.shimmerCategory).setVisibility(View.VISIBLE);
    }

    /* CREATE hideLoading FUNCTION */
    /* SETTING shimmerMeal(ViewPager) & shimmerCategory TO GONE */
    @Override
    public void hideLoading() {
        findViewById(R.id.shimmerMeal).setVisibility(View.GONE);
        findViewById(R.id.shimmerCategory).setVisibility(View.GONE);
    }

    /* CREATE FUNCTION setMeal WHICH LISTS MEAL FOR ViewPager */
    /* meal PARAMETER ALREADY GOTTEN FROM presenter.getMeals() */
    @Override
    public void setMeal(List<Meals.Meal> meal) {

        /* INITIALIZE ViewPagerHeaderAdapter AND PASS IN meal */
        ViewPagerHeaderAdapter headerAdapter = new ViewPagerHeaderAdapter(meal, this);

        /* SET headerAdapter TO viewPagerMeal VARIABLE */
        viewPagerMeal.setAdapter(headerAdapter);

        /* FORMATTING */
        viewPagerMeal.setPadding(20, 0, 150, 0);

        /* NOTIFY/ REFRESH headerAdapter THAT DATA IN meal HAS BEEN SET  */
        headerAdapter.notifyDataSetChanged();

        /* headerAdapter ITEMS ON CLICK */
        headerAdapter.setOnitemClickListener((v, position) -> {

            /* CREATES NEW TextView & PASS IN mealName FROM item_view_pager_header.xml */
            TextView mealName = v.findViewById(R.id.mealName);

            /* CREATES mealNameStr WHICH TAKES mealName CONVERTS TO STRING */
            String mealNameStr = mealName.getText().toString();

            /* CREATES FirebaseDatabase INSTANCE CALLED database WITH THE LINK TO THE FIREBASE REALTIME DATABASE */
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://tummyrecipe-98459-default-rtdb.asia-southeast1.firebasedatabase.app/");
            /* CREATES DatabaseReference INSTANCE CALLED myRef WHICH GETS THE database AND REFERENCE CHILD "message" */
            DatabaseReference myRef = database.getReference("message");

            /* PASSING mealNameStr VALUE TO "message" IN FIREBASE REALTIME DATABASE */
            myRef.push().setValue(mealNameStr);

            /* CREATES NEW INTENT AND SENDS TO DetailActivity */
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

            /* PASS mealName INTO KEY EXTRA_DETAIL */
            intent.putExtra(EXTRA_DETAIL, mealNameStr);

            /* START NEW ACTIVITY WITH PASSED intent VARIABLE */
            startActivity(intent);
        });
    }

    /* CREATE FUNCTION setMeal WHICH SETS MEAL CATEGORY FOR ViewPager */
    /* category PARAMETER ALREADY GOTTEN FROM presenter.getCategories() */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setCategory(List<Categories.Category> category) {
        /* INITIALIZE ViewPagerHeaderAdapter AND PASS IN category */
        RecyclerViewMainAdapter mainAdapter = new RecyclerViewMainAdapter(category, this);

        /* SET mainAdapter TO recyclerViewCategory VARIABLE */
        recyclerViewCategory.setAdapter(mainAdapter);

        /* FORMAT LAYOUT USING GridLayoutManager WITH SPAN OF 3 */
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);

        /* SET layoutManager(GridLayoutManager) into recyclerViewCategory */
        recyclerViewCategory.setLayoutManager(layoutManager);

        /* SET NESTED SCROLLING = TRUE */
        recyclerViewCategory.setNestedScrollingEnabled(true);

        /* NOTIFY/ REFRESH mainAdapter THAT DATA IN category HAS BEEN SET  */
        mainAdapter.notifyDataSetChanged();

        /* headerAdapter ITEMS ON CLICK */
        mainAdapter.setOnItemClickListener((view, position) -> {

            /* CREATES NEW INTENT AND SENDS TO CategoryActivity */
            Intent intent = new Intent(this, CategoryActivity.class);

            /* PASS category INTO KEY EXTRA_CATEGORY */
            /* (Serializable) AS category implements Serializable in class Categories.java */
            intent.putExtra(EXTRA_CATEGORY, (Serializable) category);

            /* PASS position(POSITION OF CATEGORY) INTO KEY EXTRA_POSITION */
            intent.putExtra(EXTRA_POSITION, position);

            /* START NEW ACTIVITY WITH PASSED intent VARIABLE */
            startActivity(intent);
        });
    }

    /* CREATE FUNCTION IF ERROR ON LOAD, SHOWS A DIALOG MESSAGE ABOUT THE ERROR */
    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this, "Title", message);
    }

}