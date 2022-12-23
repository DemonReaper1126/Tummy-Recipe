package com.example.tummy_recipe.filter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.detail.DetailActivity;
import com.example.tummy_recipe.model.Meals;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity implements FilterView{

    /* INITIALIZE VARIABLES AND SET REFERENCES */
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
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        /* CREATE NEW STRING ARRAY LIST */
        ArrayList<String> list;

        /* GET INTENT */
        Intent intent = getIntent();

        /* PASS DATA SENT FROM FilterChoices TO list */
        list = intent.getStringArrayListExtra("FILTERED");

        /* CREATE STRING BUILDER */
        StringBuilder sb = new StringBuilder();

        /* LOOP THROUGH LIST TO ADD INTO STRING BUILDER */
        for (String s : list)
        {
            sb.append(s);
            sb.append(",");
        }
        /* PASS STRING BUILDER RESULT TO A NEW STRING */
        /* EG: chicken_breast,garlic, */
        String str = sb.toString();

        /* CHECK IF LAST CHARACTER OF STRING IS "," */
        /* IF YES, DELETE THE "," */
        /* EG: chicken_breast,garlic */
        if(str.endsWith(",")) {
            sb.deleteCharAt(sb.length() - 1);
            str = sb.toString();
        }

        /* DEBUG OUTPUT FROM FilterChoices */
        Log.d("TEST", str);

        /* FUNCTION CALL */
        initActionBar();

        /* INITIALIZE FilterPresenter */
        FilterPresenter presenter = new FilterPresenter(this);

        /* GET MEALS BY INGREDIENTS */
        presenter.getMealsByFilter(str);

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

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setMeals(List<Meals.Meal> meal) {

        /* CREATING NEW STRING ARRAY LIST */
        ArrayList<String> filteredList = new ArrayList<>();

        /* IF MEAL HAS DATA */
        if(meal != null){
            /* LOOP THROUGH ALL MEAL RESULTS AND ADD INTO list */
            for (Meals.Meal mealResult : meal) {
                filteredList.add(mealResult.getStrMeal());
            }

            /* CREATE ADAPTER AND PASS IN LAYOUT AND CREATED ARRAY "filteredList" */
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, filteredList);

            /* SET adapter to VARIABLE listView */
            listView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

        }
        /* IF MEAL IS NULL/ NO SUCH MEAL EXIST BASED ON searchResult ENTERED */
        else
            Toast.makeText(this, "No result obtained!", Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {

            /* CREATES STRING CALLED selectedText AND GETS THE STRING VALUE BASED ON THE POSITION(i) IN listView*/
            String selectedText = (String) adapterView.getItemAtPosition(i);

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

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this, "Title", message);
    }
}