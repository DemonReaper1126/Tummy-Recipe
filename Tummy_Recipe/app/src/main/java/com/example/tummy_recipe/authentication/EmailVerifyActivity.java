package com.example.tummy_recipe.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerifyActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        /* SETTING REFERENCES TO VARIABLES CREATED */
        /* VARIABLE INITIALIZATION */
        toolbar = findViewById(R.id.toolbar);

        initActionBar();

        Button mVerifyButton = findViewById(R.id.cirVerifyButton);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = fAuth.getCurrentUser();

        /* IF USER IS VERIFIED, GO TO MainActivity */
        assert user != null;
        if (user.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        /* IS USER IS NOT VERIFIED */
        if (!user.isEmailVerified()){
            mVerifyButton.setOnClickListener(view -> {
                /* SEND ANOTHER VERIFICATION LINK TO EMAIL */
                user.sendEmailVerification().addOnSuccessListener(unused -> Toast.makeText(view.getContext(),
                        "Verification Email sent to your email", Toast.LENGTH_SHORT)
                        .show()).addOnFailureListener(e -> Log.d("TAG", "onFailure: Verification Email not sent!" +
                        e.getMessage()));
                onBackPressed();
            });

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
    /* WHEN USER PRESSES BACK BUTTON TWICE, CLOSES APPLICATION */
    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}