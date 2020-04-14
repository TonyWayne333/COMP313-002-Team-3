package com.example.onlineattendancesystem;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Stud_Reg_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    EditText sId, sName, sEmail, sPhone;
    Button registerBtn, chooseImage;
    ImageView studentImage;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;

    String studId, studName;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_reg);

        sId = findViewById(R.id.studID);
        sName = findViewById(R.id.studName);
        sEmail = findViewById(R.id.studEmail);
        sPhone = findViewById(R.id.studPhone);
        studentImage = findViewById(R.id.studImage);

        registerBtn = findViewById(R.id.studReg);
        chooseImage = findViewById(R.id.chooseBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Student");
        storageReference = FirebaseStorage.getInstance().getReference("Student");

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        student = new Student();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id = sId.getText().toString().trim();
                final String name = sName.getText().toString().trim();
                final String email = sEmail.getText().toString().trim();
                final String phone = sPhone.getText().toString().trim();

                if (TextUtils.isEmpty(id)) {
                    sId.setError("Please Enter Student ID.");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    sName.setError("Please Enter Full Name.");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    sEmail.setError("Please Enter Valid Email Address.");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    sPhone.setError("Please Enter Phone Number.");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, id)
                        .addOnCompleteListener(Stud_Reg_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (uploadTask != null && uploadTask.isInProgress()) {
                                    Toast.makeText(Stud_Reg_Activity.this,
                                            "Upload in progress",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    uploadImage();
                                }

                                if (task.isSuccessful()) {
                                    Toast.makeText(Stud_Reg_Activity.this,
                                            "You are Successfully Registered.",
                                            Toast.LENGTH_LONG).show();

                                    studId = firebaseAuth.getCurrentUser().getUid();

                                    DocumentReference reference = fireStore.collection("Student").document(studId);

                                    Map<String, Object> student = new HashMap<>();

                                    student.put("Student ID", id);
                                    student.put("Student Name", name);
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
                                            Log.d("TAG", "onFailure: Error Occurred !" + e.toString());
                                        }
                                    });

                                    Intent i = new Intent(Stud_Reg_Activity.this, Thank_You_Activity.class);

                                    studName = sName.getText().toString().trim();
                                    i.putExtra("name", studName);

                                    startActivity(i);
                                } else {
                                    Toast.makeText(Stud_Reg_Activity.this,
                                            "Error Occurred !" + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void uploadImage() {
        if (imageUri != null) {
            final StorageReference imageReference;
            imageReference = storageReference.child(sId.getText().toString().trim() + "." + getFileExtension(imageUri));

            uploadTask = imageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
                            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    Student student = new Student(
                                            sId.getText().toString(),
                                            sName.getText().toString(),
                                            sEmail.getText().toString(),
                                            sPhone.getText().toString(),
                                            imageUrl
                                    );
                                    String imageId = databaseReference.push().getKey();
                                    databaseReference.child(imageId).setValue(student);
                                }
                            });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            studentImage.setImageURI(imageUri);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(resolver.getType(uri));
    }

}