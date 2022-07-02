package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PharmaLogin extends AppCompatActivity {

    private EditText emailEditText,passwordEditText;
    private Button button;
    private TextView textView;
    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma_login);

        emailEditText = (EditText)findViewById(R.id.PharmaLoginEmailId);
        passwordEditText = (EditText)findViewById(R.id.PharmaLoginPasswordId);
        button = (Button)findViewById(R.id.PharmaLoginButtonId);
        textView = (TextView)findViewById(R.id.selectPId);
        progressDialog = new ProgressDialog(PharmaLogin.this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmaLogin.this,PharmaRegistration.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String mail = emailEditText.getText().toString();
                String pwd = passwordEditText.getText().toString();
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
                    return;
                }

                if(pwd.length()<6)
                {
                    passwordEditText.setError("Minimum length of password should be 6....");
                    passwordEditText.requestFocus();
                    progressDialog.dismiss();
                    return;
                }

                login(mail,pwd);

            }
        });
    }

    private void login(String mail, String pwd) {

        firebaseAuth.signInWithEmailAndPassword(mail,pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Intent intent = new Intent(PharmaLogin.this,PharmaHomePage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(PharmaLogin.this,"Authentication Failed....",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}

