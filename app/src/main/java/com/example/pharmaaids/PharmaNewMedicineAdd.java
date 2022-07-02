package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PharmaNewMedicineAdd extends AppCompatActivity {

    EditText nameText,availabilityText,priceText;
    Button button;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma_new_medicine_add);

        nameText = (EditText)findViewById(R.id.PharmaMedicineNameId);
        availabilityText = (EditText)findViewById(R.id.PharmaMedicineAvailabilityId);
        priceText = (EditText)findViewById(R.id.PharmaMedicinePriceId);
        button = (Button)findViewById(R.id.PharmaMedicineButtonId);
        progressDialog = new ProgressDialog(PharmaNewMedicineAdd.this);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please Wait....");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Medicine");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                addToDatabase();
            }
        });
    }

    private void addToDatabase() {
        String name = nameText.getText().toString();
        String avail = availabilityText.getText().toString();
        String price = priceText.getText().toString();
        String id = firebaseUser.getUid();

        Medicine medicine = new Medicine(name,id,avail,price);
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    nameText.setText("");
                    availabilityText.setText("");
                    priceText.setText("");
                    progressDialog.dismiss();
                    Toast.makeText(PharmaNewMedicineAdd.this,"Data Stored successfully..",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(PharmaNewMedicineAdd.this,"Sry there is problem..",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
