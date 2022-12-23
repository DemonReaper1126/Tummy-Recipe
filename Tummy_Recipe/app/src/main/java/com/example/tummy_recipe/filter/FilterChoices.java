package com.example.tummy_recipe.filter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tummy_recipe.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterChoices extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /* NOTE: THE REASON FOR DOING THIS IS BECAUSE DATA IS GET FROM theMealDB IN WHICH THEY DO NOT STORE INGREDIENTS CATEGORICALLY */
    /* AS SUCH, DOING CATEGORIZATION BY ITSELF REQUIRES SOME HARDCODING */

    /* INITIALIZE VARIABLES */
    /* TODO Change Initialization, References and Add to list After delete on notepad executed */
    CheckBox chicken, salmon, beef, pork, prawns, tofu
            , basmati_rice, rice, sushi_rice, spaghetti
            , basil, bay_leaf, black_pepper, cardamom, cilantro, cinnamon, garam_masala, ginger, paprika, thyme, cayenne_pepper, salt
            , coconut_milk, eggs, flour, parmesan_cheese
            , brown_sugar, caster_sugar, sugar
            , garlic, broccoli, celery, shallots, mushrooms, onions, potatoes, spinach, cabbage, shiitake_mushrooms
            , aubergine, carrots, orange, tomatoes, banana, peaches, raisins, raspberries, blueberries, strawberries
            , butter, olive_oil, sunflower_oil, vegetable_oil
            , fish_sauce, mustard, oyster_sauce, tomato_ketchup, worcestershire_sauce, mirin, mayonnaise
            , baking_powder, bicarbonate_of_soda, vanilla_extract, corn_flour, yeast;

    /* CREATE CHECKBOX LIST ARRAY */
    List<CheckBox> checkboxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_choices);
        ButterKnife.bind(this);

        /* SETTING REFERENCE */
        chicken = findViewById(R.id.chicken);
        salmon = findViewById(R.id.salmon);
        beef = findViewById(R.id.beef);
        pork = findViewById(R.id.pork);
        prawns = findViewById(R.id.prawns);
        tofu = findViewById(R.id.tofu);

        basmati_rice = findViewById(R.id.basmati_rice);
        rice = findViewById(R.id.rice);
        sushi_rice = findViewById(R.id.sushi_rice);
        spaghetti = findViewById(R.id.spaghetti);


        basil = findViewById(R.id.basil);
        bay_leaf = findViewById(R.id.bay_leaf);
        black_pepper = findViewById(R.id.black_pepper);
        cinnamon = findViewById(R.id.cinnamon);
        cardamom = findViewById(R.id.cardamom);
        cilantro = findViewById(R.id.cilantro);
        garam_masala = findViewById(R.id.garam_masala);
        ginger = findViewById(R.id.ginger);
        paprika = findViewById(R.id.paprika);
        thyme = findViewById(R.id.thyme);
        cayenne_pepper = findViewById(R.id.cayenne_pepper);
        salt = findViewById(R.id.salt);

        coconut_milk = findViewById(R.id.coconut_milk);
        eggs = findViewById(R.id.eggs);
        flour = findViewById(R.id.flour);
        parmesan_cheese = findViewById(R.id.parmesan_cheese);


        brown_sugar = findViewById(R.id.brown_sugar);
        caster_sugar = findViewById(R.id.caster_sugar);
        sugar = findViewById(R.id.sugar);

        garlic = findViewById(R.id.garlic);
        broccoli = findViewById(R.id.broccoli);
        celery = findViewById(R.id.celery);
        shallots = findViewById(R.id.shallots);
        mushrooms = findViewById(R.id.mushrooms);
        onions = findViewById(R.id.onions);
        potatoes = findViewById(R.id.potatoes);
        spinach = findViewById(R.id.spinach);
        cabbage = findViewById(R.id.cabbage);
        shiitake_mushrooms = findViewById(R.id.shiitake_mushrooms);

        aubergine = findViewById(R.id.aubergine);
        carrots = findViewById(R.id.carrots);
        orange = findViewById(R.id.orange);
        tomatoes = findViewById(R.id.tomatoes);
        banana = findViewById(R.id.banana);
        peaches = findViewById(R.id.peaches);
        raspberries = findViewById(R.id.raspberries);
        raisins = findViewById(R.id.raisins);
        blueberries = findViewById(R.id.blueberries);
        strawberries = findViewById(R.id.strawberries);

        butter = findViewById(R.id.butter);
        olive_oil = findViewById(R.id.olive_oil);
        sunflower_oil = findViewById(R.id.sunflower_oil);
        vegetable_oil = findViewById(R.id.vegetable_oil);

        fish_sauce = findViewById(R.id.fish_sauce);
        mustard = findViewById(R.id.mustard);
        oyster_sauce = findViewById(R.id.oyster_sauce);
        tomato_ketchup = findViewById(R.id.tomato_ketchup);
        worcestershire_sauce = findViewById(R.id.worcestershire_sauce);
        mirin = findViewById(R.id.mirin);
        mayonnaise = findViewById(R.id.mayonnaise);

        baking_powder = findViewById(R.id.baking_powder);
        bicarbonate_of_soda = findViewById(R.id.bicarbonate_of_soda);
        vanilla_extract = findViewById(R.id.vanilla_extract);
        corn_flour = findViewById(R.id.corn_flour);
        yeast = findViewById(R.id.yeast);



       /* ADD ALL INGREDIENTS INTO checkboxes LIST ARRAY */

        checkboxes.add(chicken);
        checkboxes.add(salmon);
        checkboxes.add(beef);
        checkboxes.add(pork);
        checkboxes.add(prawns);
        checkboxes.add(tofu);

        checkboxes.add(basmati_rice);
        checkboxes.add(rice);
        checkboxes.add(sushi_rice);
        checkboxes.add(spaghetti);


        checkboxes.add(basil);
        checkboxes.add(bay_leaf);
        checkboxes.add(black_pepper);
        checkboxes.add(cardamom);
        checkboxes.add(garam_masala);
        checkboxes.add(ginger);
        checkboxes.add(paprika);
        checkboxes.add(thyme);
        checkboxes.add(cayenne_pepper);
        checkboxes.add(salt);

        checkboxes.add(coconut_milk);
        checkboxes.add(eggs);
        checkboxes.add(flour);
        checkboxes.add(parmesan_cheese);

        checkboxes.add(brown_sugar);
        checkboxes.add(caster_sugar);
        checkboxes.add(sugar);

        checkboxes.add(garlic);
        checkboxes.add(broccoli);
        checkboxes.add(celery);
        checkboxes.add(shallots);
        checkboxes.add(mushrooms);
        checkboxes.add(onions);
        checkboxes.add(potatoes);
        checkboxes.add(spinach);
        checkboxes.add(cabbage);
        checkboxes.add(shiitake_mushrooms);

        checkboxes.add(aubergine);
        checkboxes.add(carrots);
        checkboxes.add(orange);
        checkboxes.add(tomatoes);
        checkboxes.add(banana);
        checkboxes.add(peaches);
        checkboxes.add(raisins);
        checkboxes.add(raspberries);
        checkboxes.add(blueberries);
        checkboxes.add(strawberries);

        checkboxes.add(butter);
        checkboxes.add(olive_oil);
        checkboxes.add(sunflower_oil);
        checkboxes.add(vegetable_oil);

        checkboxes.add(fish_sauce);
        checkboxes.add(mustard);
        checkboxes.add(oyster_sauce);
        checkboxes.add(tomato_ketchup);
        checkboxes.add(worcestershire_sauce);
        checkboxes.add(mirin);
        checkboxes.add(mayonnaise);

        checkboxes.add(baking_powder);
        checkboxes.add(bicarbonate_of_soda);
        checkboxes.add(vanilla_extract);
        checkboxes.add(corn_flour);
        checkboxes.add(yeast);


        /* CALL TOOLBAR */
        initActionBar();
    }

    /* FILTER BUTTON ONCLICK */
    public void onFilteredClick(View view) {

        /* CREATING NEW ARRAY */
        ArrayList<String> list = new ArrayList<>();

        /* LOOP THROUGH ALL ITEMS IN CHECKBOX */
        /* IF CHECKBOX isChecked(), ADD INTO list */
        for(CheckBox checkBox: checkboxes){
            if (checkBox.isChecked()){
                list.add(checkBox.getText().toString());
            }
        }

        /* PASS THE LIST INTO INTENT TO FilterActivity */
        Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
        intent.putExtra("FILTERED", list);
        startActivity(intent);
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

}