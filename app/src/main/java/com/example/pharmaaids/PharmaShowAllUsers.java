package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PharmaShowAllUsers extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<User> users;
    AdapterForShowAllUsers adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma_show_all_users);
        recyclerView = (RecyclerView)findViewById(R.id.ShowAllUserRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        users = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
                adapter = new AdapterForShowAllUsers(PharmaShowAllUsers.this,users);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PharmaShowAllUsers.this,"Sorry,there is some problem...please,solve them first...",Toast.LENGTH_SHORT).show();
            }
        });

        super.onStart();
    }
}
