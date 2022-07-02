package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    Intent intent;

    DatabaseReference databaseReference;
    ImageButton imageButton;
    EditText editText;

    AdapterForMessage adapter;

    private ArrayList<Chat> chats;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        imageButton = (ImageButton)findViewById(R.id.MsgImageButtonId);
        editText = (EditText)findViewById(R.id.MsgTextSendId);
        recyclerView = (RecyclerView)findViewById(R.id.MsgRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        readMessage(firebaseUser.getUid(),userId);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                if(!msg.equals(""))
                {
                    sendMessage(firebaseUser.getUid(),userId,msg);
                    Toast.makeText(MessageActivity.this,"Send msg successfully...",Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(MessageActivity.this,"you can't send empty message...",Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
            }
        });



    }

    private void sendMessage(String sender,String receiver,String message)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("sender",sender);
//        hashMap.put("receiver",receiver);
//        hashMap.put("message",message);

        Chat chat = new Chat(sender,receiver,message);

        databaseReference.child("Chats").push().setValue(chat);
        //
    }
    private void readMessage(final String myid, final String userid)
    {
        chats = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid))
                    {
                        chats.add(chat);
                    }
                    adapter = new AdapterForMessage(MessageActivity.this,chats);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this,"Sorry,there is some problem...please,solve them first...",Toast.LENGTH_SHORT).show();
            }
        });

    }
}