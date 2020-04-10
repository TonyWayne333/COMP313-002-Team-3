package com.example.onlineattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    EditText logEmail, logPassword;
    TextView txtSignup, forgotPass;
    FirebaseAuth firebaseAuth;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = findViewById(R.id.logEmail);
        logPassword = findViewById(R.id.logPass);

        loginBtn = findViewById(R.id.login);

        txtSignup = findViewById(R.id.signup);
        forgotPass = findViewById(R.id.forgotPwd);

        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = logEmail.getText().toString().trim();
                String password = logPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    logEmail.setError("This field is required!!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    logPassword.setError("Must enter the password.");
                    return;
                }

                if (password.length() < 8) {
                    logPassword.setError("Password must be greater than or equal to 8 characters.");
                }

                // Login User in FireBase
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Login_Activity.this,
                                    "Login Successfully",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Prof_Home_Activity.class));
                        } else {
                            Toast.makeText(Login_Activity.this,
                                    "Login Failed. " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Prof_Reg_Activity.class));
            }
        });
    }

    // Prevents user to go back to the previous activity.
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}