package com.example.onlineattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ViewInfoActivity extends AppCompatActivity {

    TextView firstName, lastName, email, phone;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    String profID;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);

        firstName = findViewById(R.id.profFName);
        lastName = findViewById(R.id.profLName);
        email = findViewById(R.id.profEmail);
        phone = findViewById(R.id.profPhone);

        logout = findViewById(R.id.logout);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        profID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference reference = fireStore.collection("professor").document(profID);

        reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                firstName.setText(documentSnapshot.getString("First Name"));
                lastName.setText(documentSnapshot.getString("Last Name"));
                email.setText(documentSnapshot.getString("Email ID"));
                phone.setText(documentSnapshot.getString("Phone Number"));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}