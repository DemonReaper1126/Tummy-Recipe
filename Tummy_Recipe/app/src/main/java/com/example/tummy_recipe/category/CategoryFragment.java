package com.example.tummy_recipe.category;

import static com.example.tummy_recipe.main.MainActivity.EXTRA_DETAIL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.adapter.RecyclerViewMealByCategory;
import com.example.tummy_recipe.detail.DetailActivity;
import com.example.tummy_recipe.model.Meals;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryFragment extends Fragment implements CategoryView{

    /* INITIALIZE VARIABLES AND REFERENCES */
    /* WHY @SuppressLint("NonConstantResourceId") CAN REFER TO MainActivity.java */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imageCategory)
    ImageView imageCategory;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imageCategoryBg)
    ImageView imageCategoryBg;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.textCategory)
    TextView textCategory;

    AlertDialog.Builder descDialog;

    /* onCreateView FROM FRAGMENT LIBRARY */
    /* TAKES IN fragment_category */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        /* ARGUMENTS WAS SET IN ViewPagerCategoryAdapter.java LINE 46 */
        /* HERE, GETTING THE ARGUMENTS THAT WAS SET FROM THERE */

        if (getArguments() != null){

            /* GET EACH CATEGORY FROM "EXTRA_DATA_DESC" AND SET INTO textCategory IN VIEWPAGER OF fragment_category.xml */
            textCategory.setText(getArguments().getString("EXTRA_DATA_DESC"));
            Picasso.get()
                    .load(getArguments().getString("EXTRA_DATA_IMAGE"))
                    .into(imageCategory);
            Picasso.get()
                    .load(getArguments().getString("EXTRA_DATA_IMAGE"))
                    .into(imageCategoryBg);

            /* BUILD AN ALERT DIALOG THAT SHOWS CATEGORY NAME AND CATEGORY DESCRIPTION */
            /* requireActivity basically returns FragmentActivity from associated Fragment(extended from this class) */
            descDialog = new AlertDialog.Builder(requireActivity())
                    .setTitle(getArguments().getString("EXTRA_DATA_NAME"))
                    .setMessage(getArguments().getString("EXTRA_DATA_DESC"));

            /* INITIALIZE CategoryPresenter */
            CategoryPresenter presenter = new CategoryPresenter(this);
            /* VALUE PASSED FROM FoodApi filter.php(c=) */
            presenter.getMealByCategory(getArguments().getString("EXTRA_DATA_NAME"));
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setMeals(List<Meals.Meal> meals) {
        /* INITIALIZE RecyclerViewMealByCategory AND PASS IN meals */
        RecyclerViewMealByCategory adapter = new RecyclerViewMealByCategory(getActivity(), meals);

        /* FORMAT LAYOUT USING GridLayoutManager WITH SPAN OF 2 */
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        /* SET PADDING TO CLIP ON THE ENDS OF ITEMS ONLY */
        recyclerView.setClipToPadding(false);

        /* SET adapter TO recyclerView VARIABLE */
        recyclerView.setAdapter(adapter);

        /* NOTIFY/ REFRESH adapter THAT DATA IN meals HAS BEEN SET INTO ADAPTER */
        adapter.notifyDataSetChanged();

        /* ONCLICK ON MEAL, TAKE THE VIEW AND POSITION */
        adapter.setOnItemClickListener((view, position) -> {

            /* INITIALIZE FIREBASE REALTIME DATABASE */
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://tummyrecipe-98459-default-rtdb.asia-southeast1.firebasedatabase.app/");

            /* REFERENCE CHILD "message" FROM FIREBASE REALTIME DATABASE */
            DatabaseReference myRef = database.getReference("message");

            /* GET TEXTVIEW OF MEAL NAME ACCORDING TO THE POSITION CLICKED */
            TextView mealName = view.findViewById(R.id.mealName);

            /* CONVERT THE TEXTVIEW INTO STRING */
            String mealNameStr = mealName.getText().toString();

            /* PASS THE CONVERTED STRING INTO FIREBASE REALTIME DATABASE */
            myRef.push().setValue(mealNameStr);


            /* CREATE INTENT FROM CURRENT ACTIVITY TO DetailActivity */
            Intent intent = new Intent(getActivity(), DetailActivity.class);

            /* SENDING CONVERTED STRING TO DetailActivity WITH KEY: EXTRA_DETAIL */
            intent.putExtra(EXTRA_DETAIL, mealNameStr);

            /* START NEW ACTIVITY WITH PASSED intent VARIABLE */
            startActivity(intent);
        });
    }

    /* CREATE FUNCTION IF ERROR ON LOAD, SHOWS A DIALOG MESSAGE ABOUT THE ERROR */
    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(getActivity(), "Error ", message);
    }

    /* ONCLICK ON VIEWPAGER */
    /* A CLOSE BUTTON WILL APPEAR AND ONCLICK WILL CLOSE IT */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.cardCategory)
    public void onClick(){
        descDialog.setPositiveButton("CLOSE", (dialog, which) -> dialog.dismiss());
        descDialog.show();
    }

}