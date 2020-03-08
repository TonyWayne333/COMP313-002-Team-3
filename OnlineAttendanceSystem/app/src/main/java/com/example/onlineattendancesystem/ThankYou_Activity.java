package com.example.onlineattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThankYou_Activity extends AppCompatActivity {

    private String studentFirstName, studentLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        TextView firstName = (TextView) findViewById(R.id.firstName);
        TextView lastName = (TextView) findViewById(R.id.lastName);
        Button exit = (Button) findViewById(R.id.exit);

        studentFirstName = getIntent().getStringExtra("first name");
        Log.e("1", "First Name: " + studentFirstName);

        studentLastName = getIntent().getStringExtra("last name");
        Log.e("2", "Last Name: " + studentLastName);

        firstName.setText(studentFirstName);
        lastName.setText(studentLastName);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    // Prevents user to go back to the previous activity.
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}