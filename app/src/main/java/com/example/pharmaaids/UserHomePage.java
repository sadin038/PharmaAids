package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class UserHomePage extends AppCompatActivity implements View.OnClickListener {

    private CardView search_pharmacy,save_medicine,alarm_reminder,bmi_calculation;
    private TextView textView;
   // private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        search_pharmacy = (CardView)findViewById(R.id.search_pharmacyId);
        save_medicine = (CardView)findViewById(R.id.save_medicineId);
        alarm_reminder = (CardView)findViewById(R.id.medicine_alarmId);
        bmi_calculation = (CardView)findViewById(R.id.bmi_calculationId);
        //imageView = (ImageView)findViewById(R.id.UserHomePageImageView);



        search_pharmacy.setOnClickListener(this);
        save_medicine.setOnClickListener(this);
        alarm_reminder.setOnClickListener(this);
        bmi_calculation.setOnClickListener(this);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    if(user.getId().equals(firebaseUser.getUid()))
                    {

                      Toast.makeText(UserHomePage.this,"Image ar uploaded",Toast.LENGTH_SHORT).show();

//                        Picasso.with(UserHomePage.this).load(user.getImgUrl()).
//                                placeholder(R.mipmap.ic_launcher).into(imageView);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.search_pharmacyId)
        {
            Intent intent = new Intent(UserHomePage.this,UserNearestPharmacy.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.medicine_alarmId)
        {
            Intent intent = new Intent(UserHomePage.this,UserChatList.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.save_medicineId)
        {
            Intent intent = new Intent(UserHomePage.this,UserNotes.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.bmi_calculationId)
        {
            Intent intent = new Intent(UserHomePage.this,UserProfile.class);
            startActivity(intent);
        }
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
            Intent intent = new Intent(UserHomePage.this,MainActivity.class);
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
