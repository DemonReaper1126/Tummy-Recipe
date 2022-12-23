package com.example.tummy_recipe.seen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.detail.DetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class SeenActivity extends AppCompatActivity{

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen);

        /* GET INSTANCE AND REFERENCE("message") FROM FIREBASE */
        /* VARIABLE INITIALIZATION */
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://tummyrecipe-98459-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        /* CREATING QUERY TO PASS IN CHILD "message" & LIMIT TO LAST 10 LISTED IN DATABASE */
        Query lastQuery = mDatabase.child("message").orderByKey().limitToLast(10);

        /* SET REFERENCE FOR VARIABLES */
        ListView listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.toolbar);

        /* INITIALIZE TOOLBAR */
        initActionBar();

        /* CREATING NEW ARRAY */
        ArrayList<String> list = new ArrayList<>();

        /* CREATE ADAPTER AND PASS IN LAYOUT AND CREATED ARRAY "list" */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, list);

        /* SET adapter to VARIABLE listView */
        listView.setAdapter(adapter);

        /* GETTING DATA FROM FIREBASE */
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /* CLEAR THE LIST EVERY RUN INITIALLY */
                list.clear();

                /* LOOP THROUGH AND ADD DATA FROM FIREBASE INTO listView VARIABLE */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(Objects.requireNonNull(snapshot.getValue()).toString());
                }
                /* REVERSE THE LIST TO SHOW LATEST FIRST */
                Collections.reverse(list);

                /* NOTIFY THE ADAPTER THAT DATA IN listView HAS BEEN CHANGED */
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SeenActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        /* CLICKING ON MEALS IN listView WILL GET CORRECT POSITION AND SEND TO NEXT ACTIVITY */
        listView.setOnItemClickListener((adapterView, view, position, l) -> {

            /* CREATES STRING CALLED selectedText AND GETS THE STRING VALUE BASED ON THE POSITION IN listView*/
            String selectedText = (String) adapterView.getItemAtPosition(position);

            /* CREATES AN INTENT FROM SeenActivity to DetailActivity */
            Intent intent = new Intent(SeenActivity.this, DetailActivity.class);

            /* PASS selectedText INTO KEY "MEAL" */
            intent.putExtra("MEAL", selectedText);

            /* START NEW ACTIVITY WITH PASSED intent VARIABLE */
            startActivity(intent);
        });
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