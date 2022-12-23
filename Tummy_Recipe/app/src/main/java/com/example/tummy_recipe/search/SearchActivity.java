package com.example.tummy_recipe.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.detail.DetailActivity;
import com.example.tummy_recipe.model.Meals;
import com.example.tummy_recipe.seen.SeenActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView{

    /* INITIALIZE VARIABLES & REFERENCES */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.listView)
    ListView listView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* BIND ButterKnife TO THIS ACTIVITY TO BE ABLE TO USE IT'S FUNCTIONS */
        ButterKnife.bind(this);

        /* INITIALIZE TOOLBAR */
        initActionBar();

        /* CREATE BUNDLE TO GET SEARCH DATA  */
        Bundle extras = getIntent().getExtras();
        String searchResult = extras.getString("SEARCH");

        /* INITIALIZE SearchPresenter */
        SearchPresenter presenter = new SearchPresenter(this);

        /* IF USER PASS IN EMPTY searchResult */
        if(TextUtils.isEmpty(searchResult)) {
            Toast.makeText(this, "No Input!", Toast.LENGTH_SHORT).show();
        }
        /* IF searchResult HAS DATA */
        else{
            /* GET MEALS BY SEARCH */
            presenter.getMeals(searchResult);
        }
    }

    /* CREATE TOOLBAR FUNCTION */
    private void initActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /* CREATES A BOOLEAN WHEREBY ADDS FUNCTION TO THE initActionBar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        /* WHEN PRESSED THE BACK ARROW KEY ON initActionbar */
        if (item.getItemId() == android.R.id.home) {
            /* INITIATE onBackPressed */
            /* onBackPressed is a built in function to simulate back button functionality */
            onBackPressed();
        }
        return true;
    }

    /* CREATE showLoading FUNCTION */
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /* CREATE hideLoading FUNCTION */
    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    /* CREATE FUNCTION setMeal WHICH LISTS MEALS INTO listView */
    @Override
    public void setMeals(List<Meals.Meal> meal) {
            /* CREATING NEW STRING ARRAY LIST */
            ArrayList<String> list = new ArrayList<>();

            /* IF MEAL HAS DATA */
            if(meal != null) {
                /* LOOP THROUGH ALL MEAL RESULTS AND ADD INTO list */
                for (Meals.Meal mealResult : meal) {
                    list.add(mealResult.getStrMeal());
                }

                /* CREATE ADAPTER AND PASS IN LAYOUT AND CREATED ARRAY "list" */
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, list);

                /* SET adapter to VARIABLE listView */
                listView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            /* IF MEAL IS NULL/ NO SUCH MEAL EXIST BASED ON searchResult ENTERED */
            else
                Toast.makeText(this, "No result obtained!", Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener((adapterView, view, position, l) -> {

            /* CREATES STRING CALLED selectedText AND GETS THE STRING VALUE BASED ON THE POSITION IN listView*/
            String selectedText = (String) adapterView.getItemAtPosition(position);

            /* CREATES FirebaseDatabase INSTANCE CALLED database WITH THE LINK TO THE FIREBASE REALTIME DATABASE */
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://tummyrecipe-98459-default-rtdb.asia-southeast1.firebasedatabase.app/");
            /* CREATES DatabaseReference INSTANCE CALLED myRef WHICH GETS THE database AND REFERENCE CHILD "message" */
            DatabaseReference myRef = database.getReference("message");

            /* PASSING mealNameStr VALUE TO "message" IN FIREBASE REALTIME DATABASE */
            myRef.push().setValue(selectedText);

            /* CREATES AN INTENT FROM SeenActivity to DetailActivity */
            Intent intent = new Intent(this, DetailActivity.class);

            /* PASS selectedText INTO KEY "MEAL" */
            intent.putExtra("MEAL", selectedText);

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