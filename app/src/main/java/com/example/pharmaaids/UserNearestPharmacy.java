package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserNearestPharmacy extends AppCompatActivity {

        ArrayList<Pharma> pharmas;
        private AdapterForNearestPharmacy adapter;
        private RecyclerView recyclerView;
        DatabaseReference databaseReference;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_nearest_pharmacy);
            recyclerView = (RecyclerView)findViewById(R.id.NearestPharmacyRecyclerViewId);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            pharmas = new ArrayList<>();

            databaseReference = FirebaseDatabase.getInstance().getReference("Pharmacy");
    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pharmas.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Pharma pharma =snapshot.getValue(Pharma.class);

                    pharmas.add(pharma);
                }
                adapter = new AdapterForNearestPharmacy(UserNearestPharmacy.this,pharmas);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(UserNearestPharmacy.this,"Sorry,there is some problem...please,solve them first...",Toast.LENGTH_SHORT).show();
            }
        });

            super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_logout_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutId)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserNearestPharmacy.this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}
