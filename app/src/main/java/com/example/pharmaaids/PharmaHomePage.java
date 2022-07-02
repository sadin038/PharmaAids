package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PharmaHomePage extends AppCompatActivity {

    private TextView name_textView,phn_textView,email_textView,location_textView;

    private CircleImageView circleImageView;

    private String name,phn,mail,location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma_home_page);


        name_textView = (TextView)findViewById(R.id.PharmaHomePageNameTextViewId);
        email_textView = (TextView)findViewById(R.id.PharmaHomePageEmailId);
        phn_textView = (TextView)findViewById(R.id.PharmaHomePagePhoneId);
        location_textView = (TextView)findViewById(R.id.PharmaHomePageLocationId);

        circleImageView = (CircleImageView)findViewById(R.id.PharmaHomePageCircleImageId);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pharmacy");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Pharma pharma = snapshot.getValue(Pharma.class);
                    if(pharma.getId().equals(firebaseUser.getUid()))
                    {

//                        Toast.makeText(PharmaHomePage.this,"Inside database....",Toast.LENGTH_SHORT).show();
                        name = pharma.getName();

                        Log.d("Name",name);

                        mail = pharma.getEmail();
                        Log.d("Mail",mail);
                        phn = pharma.getPhone();
                        location = pharma.getLocation();

                        name_textView.setText(name);
                        email_textView.setText(mail);
                        phn_textView.setText(phn);
                        location_textView.setText(location);
                        Picasso.with(PharmaHomePage.this).load(pharma.getImgUrl()).
                                placeholder(R.mipmap.ic_launcher).into(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutId)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(PharmaHomePage.this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.chatId)
        {
            Intent intent = new Intent(PharmaHomePage.this,PharmaChatList.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.medicineId)
        {
            Intent intent = new Intent(PharmaHomePage.this,PharmaNewMedicineAdd.class);
            startActivity(intent);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }

    }
}
