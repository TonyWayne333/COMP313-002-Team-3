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

public class Stud_Reg_Activity extends AppCompatActivity {

    EditText sId, sFirstName, sLastName, sEmail, sPhone, sImage;
    Button sRegister;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    String studId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_reg);

        sId = findViewById(R.id.studId);
        sFirstName = findViewById(R.id.studFirstName);
        sLastName = findViewById(R.id.studLastName);
        sEmail = findViewById(R.id.studEmail);
        sPhone = findViewById(R.id.studPhone);

        sRegister = findViewById(R.id.studRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        sRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = sId.getText().toString().trim();
                String firstName = sFirstName.getText().toString().trim();
                String lastName = sLastName.getText().toString().trim();
                String email = sEmail.getText().toString().trim();
                String phone = sPhone.getText().toString().trim();

                if (TextUtils.isEmpty(studentId)) {
                    sId.setError("This field is required!!");
                    return;
                }

                if (TextUtils.isEmpty(firstName)) {
                    sFirstName.setError("This field is required!!");
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    sLastName.setError("This field is required!!");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    sEmail.setError("This field is required!!");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    sPhone.setError("This field is required!!");
                    return;
                }

                // Register Professor in FireBase
                firebaseAuth.createUserWithEmailAndPassword(email, studentId).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Stud_Reg_Activity.this,
                                    "Student Is Registered Successfully.",
                                    Toast.LENGTH_LONG).show();

                            // Getting the user ID for the register student.
                            studId = firebaseAuth.getCurrentUser().getUid();

                            // Storing the data into collection.
                            DocumentReference reference = fireStore.collection("student").document(studId);

                            // HashMap will store the data in (key, data) format.
                            Map<String, Object> student = new HashMap<>();

                            // Inserting Data into HashMap.
                            student.put("Student ID", studentId);
                            student.put("First Name", firstName);
                            student.put("Last Name", lastName);
                            student.put("Email ID", email);
                            student.put("Phone Number", phone);

                            reference.set(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: User Profile is created for Professor ID: " + studId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: " + e.toString());
                                }
                            });

                            // Start the Register activity.
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            Toast.makeText(Stud_Reg_Activity.this,
                                    "Error occurred." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}