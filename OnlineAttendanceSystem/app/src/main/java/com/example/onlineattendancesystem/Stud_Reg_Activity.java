package com.example.onlineattendancesystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Stud_Reg_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri studImageUri;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private StorageTask uploadTask;

    EditText sId, sFirstName, sLastName, sEmail, sPhone;
    Button sRegister, chooseImage;
    ImageView studentImage;
    String studId, studFName, studLName;

    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_reg);

        sId = findViewById(R.id.studId);
        sFirstName = findViewById(R.id.studFirstName);
        sLastName = findViewById(R.id.studLastName);
        sEmail = findViewById(R.id.studEmail);
        sPhone = findViewById(R.id.studPhone);

        chooseImage = findViewById(R.id.uploadBtn);
        studentImage = findViewById(R.id.studImage);

        sRegister = findViewById(R.id.studRegister);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        //storageRef = FirebaseStorage.getInstance().getReference().child("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        databaseRef = database.getReference("student");

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

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
                        if (uploadTask != null && uploadTask.isInProgress()) {
                            Toast.makeText(Stud_Reg_Activity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadImage();
                        }

                        if (task.isSuccessful()) {
                            Toast.makeText(Stud_Reg_Activity.this,
                                    "Student Registered Successfully.",
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
                                    Log.d("TAG", "onSuccess: User Profile is created for Student ID: " + studId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: " + e.toString());
                                }
                            });

                            // Start the Register activity.
                            Intent i = new Intent(Stud_Reg_Activity.this, ThankYou_Activity.class);

                            studFName = sFirstName.getText().toString().trim();
                            studLName = sLastName.getText().toString().trim();

                            i.putExtra("first name", studFName);
                            i.putExtra("last name", studLName);

                            startActivity(i);
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

    // This method is used to get the extension(.jpeg, .jpg, .png) from our selected image.
    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(resolver.getType(uri));
    }

    // Used to upload the selected image using openFileChooser() method.
    private void uploadImage() {
        if (studImageUri != null) {
            StorageReference imageReference;
            imageReference = storageRef.child(sId.getText().toString().trim() + "." + getFileExtension(studImageUri));

            uploadTask = imageReference.putFile(studImageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Stud_Reg_Activity.this, "Upload Successful", Toast.LENGTH_LONG).show();

                            // Stores student's data in Realtime database
                            Student student;
                            student = new Student(
                                    sId.getText().toString(),
                                    sFirstName.getText().toString(),
                                    sLastName.getText().toString(),
                                    sEmail.getText().toString(),
                                    sPhone.getText().toString(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            //databaseRef.child("registered_student").push().setValue(student);

                            String imageId = databaseRef.push().getKey();
                            databaseRef.child(imageId).setValue(student);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Stud_Reg_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_LONG).show();
        }
    }

    // Used to select image from the user's storage.
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            studImageUri = data.getData();

            //Picasso.with(this).load(studImageUri).into(studentImage);
            studentImage.setImageURI(studImageUri);
        }
    }

    // Prevents user to go back to the previous activity.
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}