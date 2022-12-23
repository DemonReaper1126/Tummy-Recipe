package com.example.tummy_recipe.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.model.Categories;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerViewMainAdapter.RecyclerViewHolder> {

    /* VARIABLE INITIALIZATION */
    private final List<Categories.Category> categories;
    private final Context context;
    private static ClickListener clickListener;

    /* CREATE CONSTRUCTOR */
    public RecyclerViewMainAdapter(List<Categories.Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewMainAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_category,
                viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    /* UPDATE RecyclerView.ViewHolder CONTENTS WITH THE ITEM AT THE GIVEN POSITION i */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMainAdapter.RecyclerViewHolder viewHolder, int i) {
        /* i HERE MEANS POSITION */

        /* CREATE NEW VARIABLE WHICH TAKES CATEGORY THUMBNAIL(getStrCategoryThumb()) FROM categories MODEL */
        String strCategoryThumb = categories.get(i).getStrCategoryThumb();

        /* PASS THE CREATED strCategoryThumb INTO REFERENCED categoryThumb WITH A CIRCLE PLACEHOLDER */
        /* categoryThumb is created and referenced below in RecyclerViewHolder */
        Picasso.get().load(strCategoryThumb).placeholder(R.drawable.ic_circle).into(viewHolder.categoryThumb);

        /* CREATE NEW STRING WHICH TAKES CATEGORY POSITION FROM categories MODEL */
        String strCategoryName = categories.get(i).getStrCategory();

        /* SET TEXT FOR categoryName IN ORDER BASED ON i */
        viewHolder.categoryName.setText(strCategoryName);

    }

    /* GET THE TOTAL NUMBER OF CATEGORIES */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /* CREATE STATIC CLASS THAT IMPLEMENTS OnClickListener TO BE USED IN MainActivity LINE 188*/
    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        /* INITIALIZE VARIABLES AND REFERENCES */
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.categoryThumb)
        ImageView categoryThumb;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.categoryName)
        TextView categoryName;

        RecyclerViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        /* CREATE ONCLICK BASED ON POSITION OF CLICK */
        @Override
        public void onClick(View view) {
            //getBindingAdapterPosition maybe wrong
            //changed from depreciated getAdapterPosition()
            clickListener.onClick(view, getBindingAdapterPosition());
        }
    }

    /* CREATING setOnItemClickListener TO ENABLE RecyclerViewMainAdapter FOR CLICK LISTENER */
    public void setOnItemClickListener(ClickListener clickListener){
        RecyclerViewMainAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }
}
