package com.example.tummy_recipe.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tummy_recipe.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    /* VARIABLE INITIALIZATION */
    private EditText etEmail, etPassword;
    private FirebaseAuth fAuth;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* SETTING REFERENCES TO VARIABLES CREATED */
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        TextView tvForgotPass = findViewById(R.id.tvForgotPass);
        Button mLoginButton = findViewById(R.id.cirLoginButton);

        fAuth = FirebaseAuth.getInstance();

        /* IF THERE IS A USER, GO TO EmailVerifyActivity */
        if (fAuth.getCurrentUser() != null ){
            startActivity(new Intent(getApplicationContext(), EmailVerifyActivity.class));
            finish();
        }

        /* WHEN USER CLICKS ON LOGIN BUTTON */
        mLoginButton.setOnClickListener(view -> {

            /* CREATES STRING email & password */
            /* PASS INPUT TEXT etEmail & etPassword TO email AND password RESPECTIVELY */
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            /* IF email OR password IS NULL OR EMPTY */
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                /* IF email IS NULL OR EMPTY */
                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email is a required field!");
                }

                /* IF password IS NULL OR EMPTY */
                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Password is a required field!");
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


                /* LOGIN USER BY CHECKING email & password */
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    /* IF SUCCESSFULLY MATCH email & password WITH FIREBASE USER email & password */
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                        /* GO TO EmailVerifyActivity */
                        startActivity(new Intent(getApplicationContext(), EmailVerifyActivity.class));
                    }

                    /* IF UNSUCCESSFULLY RETRIEVE USER FROM FIREBASE AUTHENTICATION */
                    else {
                        /* OUTPUT ERROR MESSAGE */
                        Toast.makeText(this, "Error :" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        /* IF CLICK ON FORGET PASSWORD TEXT */
        tvForgotPass.setOnClickListener(view -> {
            EditText resetMail = new EditText(view.getContext());

            /* DISPLAY AlertDialog TO PROMPT USER FOR EMAIL FOR RESET PASSWORD */
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
            passwordResetDialog.setTitle("Reset Password");
            passwordResetDialog.setMessage("Enter email address to receive reset password link:");
            passwordResetDialog.setView(resetMail);

            /* IF USER CLICKS YES */
            passwordResetDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                /* CREATE mail VARIABLE TO GET resetMail CONTENT */
                String mail = resetMail.getText().toString();

                /* SEND RESET LINK TO EMAIL, IF SUCCESSFUL, DISPLAY TOAST MESSAGE */
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(unused ->
                        Toast.makeText(LoginActivity.this, "Reset password link sent to your email.", Toast.LENGTH_SHORT)
                        .show()).addOnFailureListener(e ->
                        Toast.makeText(LoginActivity.this, "Error! Reset link not sent!" + e.getMessage(), Toast.LENGTH_SHORT).show());

            });

            /* IF USER CLICKS NO */
            passwordResetDialog.setNegativeButton("No", (dialogInterface, i) -> {
                /* CLOSE AlertDialog */
            });

            /* DISPLAY THE AlertDialog */
            passwordResetDialog.create().show();

        });

    }

    /* FUNCTION ANIMATION SLIDE FROM THIS ACTIVITY TO RegisterActivity */
    public void onLoginClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
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