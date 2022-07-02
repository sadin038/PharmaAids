package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Dialog;

public class UserNotes extends AppCompatActivity {

    EditText editText;
    Spinner spinner1, spinner2;
    Button button1,button2,save;
    //////////////////////////////////////

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private AdapterForNotes adapter;
    ArrayList<Notes> notes ;
    SqliteForNotes sqliteForNotes;
    private Dialog dialog;
    private String Task,Hour,Minute,Day,Month,Year,Spin1,Spin2;
    FirebaseUser firebaseUser;

       int name = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingButtonId);
        sqliteForNotes = new SqliteForNotes(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = (RecyclerView)findViewById(R.id.NotesRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showNotes();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotes();
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
            Intent intent = new Intent(UserNotes.this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }



    private void addNotes() {

        dialog = new Dialog(UserNotes.this);
        dialog.setContentView(R.layout.activity_user_notes_add);
        editText = dialog.findViewById(R.id.TaskId);
        spinner1 = dialog.findViewById(R.id.spinner1);
        spinner2 = dialog.findViewById(R.id.spinner2);
        button1 = dialog.findViewById(R.id.DateId);
        button2 = dialog.findViewById(R.id.TimeId);
        save = dialog.findViewById(R.id.SaveButtonId);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

               DatePickerDialog datePickerDialog = new DatePickerDialog(UserNotes.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                Year = String.valueOf(year);
                                Month = String.valueOf(month);
                                Day = String.valueOf(dayOfMonth);
                                button1.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                                Toast.makeText (UserNotes.this,"Successfully Date",Toast.LENGTH_SHORT).show ();
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(UserNotes.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String AM_PM ;
                                if(hourOfDay<12)
                                {
                                    AM_PM = "AM";
                                }
                                else
                                {
                                    AM_PM = "PM";
                                }
                                Hour = String.valueOf(hourOfDay);
                                Minute = String.valueOf(minute);

                                button2.setText(hourOfDay+" : "+minute+" "+AM_PM);
                                Toast.makeText (UserNotes.this,"Successfully Time Picked",Toast.LENGTH_SHORT).show ();
                            }
                        },hour,minute,false);
                timePickerDialog.show();

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Task = editText.getText().toString();
                Spin1 = spinner1.getSelectedItem().toString();
                Spin2 = spinner2.getSelectedItem().toString();



                String Id = firebaseUser.getUid();

                boolean checker =  sqliteForNotes.addtotable (Id,Task,Spin1,Spin2,Hour,Minute,Year,Month,Day);
                if(checker == true)
                {
                    Toast.makeText (UserNotes.this,"Successfully Data Stored",Toast.LENGTH_SHORT).show ();
                }
                else
                {
                    Toast.makeText (UserNotes.this,"Data storage is not Completed",Toast.LENGTH_SHORT).show ();
                }
                /////////////////////////////////////////////////////////////////
//                Intent intent = new Intent(UserNotes.this,UserAlarmManager.class);
//                intent.putExtra("message",Task);
//                intent.putExtra("Id",Id);
//
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(UserNotes.this, Integer.parseInt(Id),intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);


               /////////////////////////////////////////////////////////////////////


                showNotes();
                dialog.dismiss();
            }
        });


    }

    private void showNotes() {
        Cursor result = sqliteForNotes.display ();
        if(result.getCount () == 0)
        {
            Toast.makeText (UserNotes.this,"No data is found",Toast.LENGTH_SHORT).show ();
            return;
        }

        result.moveToFirst ();
        notes = new ArrayList<>();

        do{

            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH));
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
//            String hour = String.valueOf(calendar.get(Calendar.HOUR));
//            String C_hour = "24";
            if(firebaseUser.getUid().equals(result.getString(0)))
            {
//                if(hour>Integer.parseInt(result.getString(4)) || (hour==Integer.parseInt(result.getString(4)) && minute>Integer.parseInt(result.getString(5))))
//                {
//                    int updated_day = Integer.parseInt(day);
//                    updated_day = updated_day + 1;
//                    sqliteForNotes.updateData(result.getString(0),result.getString(6),result.getString(7),String.valueOf(updated_day));
//                }
//
//                Notes note = new Notes(result.getString(0),result.getString (1),result.getString (2),result.getString (3),result.getString (4),
//                        result.getString (5),result.getString (6),result.getString (7),result.getString (8));
//
//                notes.add(note);

                if(year.equals(result.getString(6)) && month.equals(result.getString(7)) && day.equals(result.getString(8))){

                    if(hour<Integer.parseInt(result.getString(4)) || (hour==Integer.parseInt(result.getString(4)) && minute<Integer.parseInt(result.getString(5))))
                    {
                        Notes note = new Notes(result.getString(0),result.getString (1),result.getString (2),result.getString (3),result.getString (4),
                                result.getString (5),result.getString (6),result.getString (7),result.getString (8));

                        notes.add(note);
                    }

                }
            }

        }
        while(result.moveToNext ());
        adapter = new AdapterForNotes(this,notes);
        recyclerView.setAdapter(adapter);
    }


}


