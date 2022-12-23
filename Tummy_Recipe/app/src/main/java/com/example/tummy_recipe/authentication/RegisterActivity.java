package com.example.tummy_recipe.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tummy_recipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    /* VARIABLE INITIALIZATION */
    private EditText etName, etEmail, etMobile, etPassword;
    private FirebaseAuth fAuth;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /* SETTING REFERENCES TO VARIABLES CREATED */
        etName = findViewById(R.id.editTextName);
        etEmail = findViewById(R.id.editTextEmail);
        etMobile = findViewById(R.id.editTextMobile);
        etPassword = findViewById(R.id.editTextPassword);
        Button mRegisterButton = findViewById(R.id.cirRegisterButton);

        fAuth = FirebaseAuth.getInstance();

        /* IF THERE IS A USER, GO TO EmailVerifyActivity */
        if (fAuth.getCurrentUser() != null ){
            startActivity(new Intent(getApplicationContext(), EmailVerifyActivity.class));
            finish();
        }

        /* WHEN USER CLICKS ON REGISTER BUTTON */
        mRegisterButton.setOnClickListener(view -> {

            /* CREATES STRING email & password */
            /* PASS INPUT TEXT etEmail & etPassword TO email AND password RESPECTIVELY */
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            /* CREATES NAME AND MOBILE */
            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();

            /* IF email OR password OR name OR mobile IS NULL OR EMPTY */
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email) || TextUtils.isEmpty(mobile)) {

                /* CREATES FirebaseDatabase INSTANCE CALLED database WITH THE LINK TO THE FIREBASE REALTIME DATABASE */
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://tummyrecipe-98459-default-rtdb.asia-southeast1.firebasedatabase.app/");
                /* CREATES DatabaseReference INSTANCE CALLED myRef WHICH GETS THE database AND REFERENCE CHILD "message" */
                DatabaseReference myRef = database.getReference("users");

                /* INITIALIZE UserHelperClass */
                UserHelperClass userHelperClass = new UserHelperClass(name, email, mobile);

                myRef.push().setValue(userHelperClass);

                /* IF email IS NULL OR EMPTY */
                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email is a required field!");
                }

                /* IF password IS NULL OR EMPTY */
                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Password is a required field!");
                }

                /* IF name IS NULL OR EMPTY */
                if (TextUtils.isEmpty(name)) {
                    etName.setError("Name is a required field!");
                }

                /* IF name IS NULL OR EMPTY */
                if (TextUtils.isEmpty(mobile)) {
                    etMobile.setError("Mobile Number is a required field!");
                }

                /* IF password IS LESS THAN 6 (ALPHABETS, SYMBOLS, NUMBERS) */
                if (password.length() < 6) {
                    etPassword.setError("Password must be 6 or more characters!");
                }
            }

            /* IF THERE IS NO ERROR AND ALL FIELDS ARE FILLED AND CORRECT */
            else {
                /* PREVENTING MULTI CLICKS */
                /* INTERVAL OF 1 SECONDS BEFORE NEXT CLICK CAN BE REGISTERED */
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();


                /* REGISTERS USER IN FIREBASE AUTHENTICATION */
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    /* IF SUCCESSFULLY SENDS DATA TO FIREBASE AUTHENTICATION */
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User Created!", Toast.LENGTH_SHORT).show();
                        /* GO TO EmailVerifyActivity */
                        startActivity(new Intent(getApplicationContext(), EmailVerifyActivity.class));
                    }

                    /* IF UNABLE TO SEND DATA TO FIREBASE AUTHENTICATION */
                    else {
                        /* OUTPUT ERROR MESSAGE */
                        Toast.makeText(RegisterActivity.this, "Error :" + Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /* FUNCTION ANIMATION SLIDE FROM THIS ACTIVITY TO LoginActivity */
    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
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
}