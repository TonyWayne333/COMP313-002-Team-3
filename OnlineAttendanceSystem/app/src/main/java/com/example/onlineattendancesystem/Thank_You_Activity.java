package com.example.onlineattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Thank_You_Activity extends AppCompatActivity {

    TextView studentName;
    private String studentFullName;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        exit = findViewById(R.id.exit);
        studentName = findViewById(R.id.studentName);

        studentFullName = getIntent().getStringExtra("name");
        Log.e("1", "Student Name: " + studentFullName);

        studentName.setText(studentFullName);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}