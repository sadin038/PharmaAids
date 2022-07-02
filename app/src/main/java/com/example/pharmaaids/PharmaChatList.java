package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PharmaChatList extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterForPharmaChatList adapter;
    ArrayList<String> chatlist;
    ArrayList<User> users;

    Set<String> list = new HashSet<>();

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,reference,data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma_chat_list);
        recyclerView = (RecyclerView)findViewById(R.id.PharmaChatListRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatlist = new ArrayList<>();
        users = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        reference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                chatlist.clear();
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSender().equals(firebaseUser.getUid()) || chat.getReceiver().equals(firebaseUser.getUid())) {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    User user = dataSnapshot1.getValue(User.class);
                                    if (user.getId().equals(chat.getSender()) && chat.getReceiver().equals(firebaseUser.getUid())) {

                                        list.add(user.getId());
                                    } else if (user.getId().equals(chat.getReceiver()) && chat.getSender().equals(firebaseUser.getUid())) {
                                        list.add(user.getId());
                                        Log.d("List", user.getId());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

//                ///////////////////////////////////////////////////////////////////////////////////////////////////
//                for(String id : list)
//                {
//                    Log.d("Outer List Lenght: ", id);
//                }
//
//                //
                data = FirebaseDatabase.getInstance().getReference("Users");
                data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot2 : dataSnapshot.getChildren())
                        {
                            User user = snapshot2.getValue(User.class);

                            for(String id : list)
                            {
                                if(id == (user.getId()))
                                {
                                    users.add(user);
                                }
                            }
                        }
                        adapter = new AdapterForPharmaChatList(PharmaChatList.this, users);
                        recyclerView.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PharmaChatList.this, "Sorry,there is some problem...please,solve them first...", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
