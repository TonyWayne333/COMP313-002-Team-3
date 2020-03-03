package com.example.onlineattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class Prof_Home_Activity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;

    RecyclerView view;
    ArrayList<UploadImage> list;
    ImageAdapter adapter;

    TextView firstName, lastName, id;
    String studID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_home);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        id = findViewById(R.id.stdId);

        //view = findViewById(R.id.recyclerView);
        //view.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<UploadImage>();

        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        studID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference reference = fireStore.collection("professor").document(studID);
        //databaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                firstName.setText(documentSnapshot.getString("First Name"));
                lastName.setText(documentSnapshot.getString("Last Name"));
                id.setText(documentSnapshot.getString("Student ID"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}