package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UserRegistration extends AppCompatActivity {

    private EditText emailEditText,passwordEditText,unameEditText,confirmPasswordEditText;
    private Button button;
    private ImageView imageView;

    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private static final int REQUEST_CODE = 1;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


        emailEditText = (EditText)findViewById(R.id.UserRegistrationEmailEditTextId);
        passwordEditText = (EditText)findViewById(R.id.UserRegistrationPasswordEditTextId);
        confirmPasswordEditText = (EditText)findViewById(R.id.UserRegistrationConfirmPasswordEditTextId);
        unameEditText = (EditText)findViewById(R.id.UserRegistrationNameEditTextId);
        button = (Button)findViewById(R.id.UserRegistrationButtonId);
        imageView = (ImageView)findViewById(R.id.UserRegistrationImageViewId);
        progressDialog =new ProgressDialog(UserRegistration.this);
        storageReference = FirebaseStorage.getInstance().getReference("Users");



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                progressDialog.show();
                String name = unameEditText.getText().toString();
                String mail = emailEditText.getText().toString();
                String pwd = passwordEditText.getText().toString();
                String repwd = confirmPasswordEditText.getText().toString();
                if(mail.isEmpty()){
                    emailEditText.setError("Enter an email address....");
                    emailEditText.requestFocus();
                    progressDialog.dismiss();
                    return;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                {
                    emailEditText.setError("Enter a valid email address....");
                    emailEditText.requestFocus();
                    progressDialog.dismiss();
                    return;
                }

                //checking the validity of the password
                if(pwd.isEmpty())
                {
                    passwordEditText.setError("Enter a password.....");
                    passwordEditText.requestFocus();
                    progressDialog.dismiss();
                    return;
                }

                if(pwd.length()<6)
                {
                    passwordEditText.setError("Minimum length of password should be 6....");
                    passwordEditText.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(!pwd.equals(repwd))
                {
                    Toast.makeText(UserRegistration.this,"Check your Password please...",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }


                register(name,mail,pwd);


            }
        });

    }


    private void register(final String name, final String email, final String password)
    {

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            final String userId = firebaseUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            StorageReference reference = storageReference.child(userId+"."+getExtension(imageUri));
                            reference.putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Get a URL to the uploaded content


                                            Toast.makeText(getApplicationContext(),"" +
                                                    "Image is stored successfully",Toast.LENGTH_SHORT).show();

                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                                             while (!uriTask.isSuccessful());
                                            Uri display = uriTask.getResult();


                                            User user = new User(userId,name,email,display.toString());

                                            databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful())
                                                    {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(),"Registration is successful,Confirm your email from your gmail",Toast.LENGTH_SHORT).show();
                                                        emailEditText.setText("");
                                                        passwordEditText.setText("");
                                                        confirmPasswordEditText.setText("");
                                                        unameEditText.setText("");
                                                        imageView.setVisibility(View.GONE);
                                                        button.setVisibility(View.VISIBLE);


                                                    }
                                                    else
                                                    {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(),"Registration is unsuccessful",Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            // ...
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"" +
                                                    "Image is not stored",Toast.LENGTH_SHORT).show();
                                        }
                                    });




                        }
                        else
                        {
                            progressDialog.dismiss();
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {

                                Toast.makeText(getApplicationContext(), "Already registered with this email", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Problem in registration...", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

    public String getExtension(Uri imageUri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver
                .getType(imageUri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }
}

