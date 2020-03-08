package com.example.onlineattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Prof_Home_Activity extends AppCompatActivity {

    Button studentList, attendance, editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_home);

        studentList = findViewById(R.id.student_list);
        attendance = findViewById(R.id.attendance);
        editProfile = findViewById(R.id.edit_profile);

        studentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Student_List_Activity.class));
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AttendanceActivity.class));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Edit_Profile_Activity.class));
            }
        });
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}