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
import java.util.HashSet;
import java.util.Set;

public class UserChatList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> chatlist;
    ArrayList<Pharma> pharmas;
    ArrayList<Pharma> pharma_chat;

    Set<String> list = new HashSet<>();

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,reference,data;
    AdapterForChatList adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat_list);

        recyclerView = (RecyclerView) findViewById(R.id.UserChatListRecyclerViewId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pharmas = new ArrayList<>();

        pharma_chat = new ArrayList<>();
        chatlist = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        reference = FirebaseDatabase.getInstance().getReference("Pharmacy");




          databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pharmas.clear();
                chatlist.clear();
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSender().equals(firebaseUser.getUid()) || chat.getReceiver().equals(firebaseUser.getUid())) {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Pharma pharma = dataSnapshot1.getValue(Pharma.class);
                                    if (pharma.getId().equals(chat.getSender()) && chat.getReceiver().equals(firebaseUser.getUid())) {

                                        list.add(pharma.getId());
                                       // pharmas.add(pharma);
                                       // chatlist.add(pharma.getId());
                                        Log.d("List Id", pharma.getId());
                                    } else if (pharma.getId().equals(chat.getReceiver()) && chat.getSender().equals(firebaseUser.getUid())) {
                                        list.add(pharma.getId());
                                       // pharmas.add(pharma);
                                        //chatlist.add(pharma.getId());
                                        Log.d("List", pharma.getId());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                ///////////////////////////////////////////////////////////////////////////////////////////////////
                for(String id : list)
                {
                    Log.d("Outer List Lenght: ", id);
                }

               //
                data = FirebaseDatabase.getInstance().getReference("Pharmacy");
                data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot2 : dataSnapshot.getChildren())
                        {
                            Pharma pharma = snapshot2.getValue(Pharma.class);

                            for(String id : list)
                            {
                                if(id == (pharma.getId()))
                                {
                                    pharmas.add(pharma);
                                }
                            }
                        }
                        adapter = new AdapterForChatList(UserChatList.this, pharmas);
                        recyclerView.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                /////////////////////////////////////////////////////////////////////////////////////////////////

//                for(Pharma pharma : list)
//                {
//                    pharmas.add(pharma);
//                }


//                adapter = new AdapterForChatList(UserChatList.this, pharmas);
//                recyclerView.setAdapter(adapter);

////
////
////
//                for(Pharma pharm : pharmas)
//                {
//                    if(!pharma_chat.contains(pharm))
//                    {
//                        pharma_chat.add(pharm);
//                    }
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserChatList.this, "Sorry,there is some problem...please,solve them first...", Toast.LENGTH_SHORT).show();
            }
        });
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
            Intent intent = new Intent(UserChatList.this,MainActivity.class);
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
