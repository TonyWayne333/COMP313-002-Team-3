package com.example.onlineattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Prof_Reg_Activity extends AppCompatActivity {

    EditText pFirstName, pLastName, pPassword, pEmail, pPhone;
    TextView pLoginText;
    Button pRegister;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    String profId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_reg);

        pFirstName = findViewById(R.id.profFirstName);
        pLastName = findViewById(R.id.profLastName);
        pPassword = findViewById(R.id.profPassword);
        pEmail = findViewById(R.id.profEmail);
        pPhone = findViewById(R.id.profPhone);

        pRegister = findViewById(R.id.profRegister);
        pLoginText = findViewById(R.id.profLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Prof_Home_Activity.class));
            finish();
        }

        pRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = pFirstName.getText().toString().trim();
                String lastName = pLastName.getText().toString().trim();
                String email = pEmail.getText().toString().trim();
                String password = pPassword.getText().toString().trim();
                String phone = pPhone.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    pFirstName.setError("This field is required!!");
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    pLastName.setError("This field is required!!");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    pEmail.setError("This field is required!!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    pPassword.setError("Must enter the password.");
                    return;
                }

                if (password.length() < 8) {
                    pPassword.setError("Password must be greater than or equal to 8 characters.");
                }

                if (TextUtils.isEmpty(phone)) {
                    pPhone.setError("This field is required!!");
                    return;
                }

                // Register Professor in FireBase
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Prof_Reg_Activity.this,
                                    "User Registered Successfully.",
                                    Toast.LENGTH_LONG).show();

                            // Getting the user ID for the register professor.
                            profId = firebaseAuth.getCurrentUser().getUid();

                            // Storing the data into collection.
                            DocumentReference reference = fireStore.collection("professor").document(profId);

                            // HashMap will store the data in (key, data) format.
                            Map<String, Object> professor = new HashMap<>();

                            // Inserting Data into HashMap.
                            professor.put("First Name", firstName);
                            professor.put("Last Name", lastName);
                            professor.put("Email ID", email);
                            professor.put("Phone Number", phone);
                            professor.put("Password", password);

                            reference.set(professor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: User Profile is created for Professor ID: " + profId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: " + e.toString());
                                }
                            });

                            // Start the Register activity.
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(Prof_Reg_Activity.this,
                                    "Error occurred." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        pLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}