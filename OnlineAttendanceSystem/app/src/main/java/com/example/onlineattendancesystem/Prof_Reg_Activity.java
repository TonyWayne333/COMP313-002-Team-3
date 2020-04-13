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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Prof_Reg_Activity extends AppCompatActivity {

    EditText pName, pEmail, pPhone, pPassword;
    Button registerBtn;
    TextView loginLink;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    FirebaseDatabase firebaseDatabase;

    String profId;
    Professor professor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_reg);

        pName = findViewById(R.id.profName);
        pEmail = findViewById(R.id.profEmail);
        pPassword = findViewById(R.id.profPwd);
        pPhone = findViewById(R.id.profPhone);

        loginLink = findViewById(R.id.login);

        registerBtn = findViewById(R.id.profReg);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Professor_Registration");
        //databaseReference = firebaseDatabase.getReference("Professor");

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        professor = new Professor();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = pName.getText().toString().trim();
                final String email = pEmail.getText().toString().trim();
                final String password = pPassword.getText().toString().trim();
                final String phone = pPhone.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    pName.setError("Please Enter Full Name.");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    pEmail.setError("Please Enter Valid Email Address.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    pPassword.setError("Please Enter Valid Password.");
                    return;
                }

                if (password.length() < 8) {
                    pPassword.setError("Password must be greater than or equal to 8 characters.");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    pPhone.setError("Please Enter Phone Number.");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Prof_Reg_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Prof_Reg_Activity.this,
                                            "You are Successfully Registered.",
                                            Toast.LENGTH_LONG).show();

                                    profId = firebaseAuth.getCurrentUser().getUid();

                                    DocumentReference reference = fireStore.collection("Professor").document(profId);

                                    Map<String, Object> professor = new HashMap<>();

                                    professor.put("Professor Name", name);
                                    professor.put("Email ID", email);
                                    professor.put("Password", password);
                                    professor.put("Phone Number", phone);

                                    reference.set(professor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "onSuccess: User Profile is created for Professor ID: " + profId);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", "onFailure: Error Occurred !" + e.toString());
                                        }
                                    });

                                    Intent i = new Intent(Prof_Reg_Activity.this, Login_Activity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(Prof_Reg_Activity.this,
                                            "Error Occurred" + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }
        });
    }

}