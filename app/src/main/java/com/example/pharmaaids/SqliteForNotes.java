package com.example.pharmaaids;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class SqliteForNotes extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = " note.db ";
    private static final String TABLE_NAME = " mytable ";
    private static  final String COL1 = "ID";
    private static  final String COL2 = "TASK";
    private static final String COL3 = " HOW_MUCH ";
    private static final String COL4 = "TIMES ";
    private static final String COL5 = "TIMEPICKER_HOUR";
    private static final String COL6 = "TIMEPICKER_MINUTE";
    private static final String COL7 = "DATEPICKER_YEAR";
    private static final String COL8 = "DATEPICKER_MONTH";
    private static final String COL9 = "DATEPICKER_DAY";




    public SqliteForNotes(Context context) {
        super (context, DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String quary;
        quary = " CREATE TABLE " + TABLE_NAME + " ( " + COL1 + " TEXT, " + COL2 + " TEXT, "
                + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 + " TEXT, " + COL7 + " TEXT, " +COL8 + " TEXT, "+ COL9 + " TEXT " + " ) ";
        db.execSQL (quary);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL (" DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate (db);

    }

    public boolean  addtotable(String id,String task,String how_much,String times,String hour,String minute,String year,String month,String day)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put (COL1,id);
        contentValues.put (COL2,task);
        contentValues.put (COL3,how_much);
        contentValues.put (COL4,times);
        contentValues.put (COL5,hour);
        contentValues.put (COL6,minute);
        contentValues.put (COL7,year);
        contentValues.put (COL8,month);
        contentValues.put (COL9,day);
        long checker = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        if(checker==-1)
        {
            return false;
        }
        else
        {
            return  true;
        }
    }

    public boolean updateData(String id,String year,String month,String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL7,year);
        contentValues.put(COL8,month);
        contentValues.put(COL9,day);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

//    public boolean  updateDate(String year,String month,String day)
//    {
//        ArrayList<Notes> notes;
//        SqliteForNotes sqliteForNotes = null;
//        Cursor result = sqliteForNotes.display ();
//
//        result.moveToFirst ();
//        notes = new ArrayList<>();
//
//        do{
//
//            Notes note = new Notes(result.getString(0),result.getString (1),result.getString (2),result.getString (3),result.getString (4),
//                    result.getString (5),result.getString (6),result.getString (7),result.getString (8));
//           notes.add(note);
//           int x = Integer.parseInt(note.getHour());
//           int y = Integer.parseInt(note.getMin());
//           Calendar calendar = Calendar.getInstance();
//           int hour = calendar.get(Calendar.HOUR);
//           int min = calendar.get(Calendar.MINUTE);
//
//           if(hour>x || (hour==x && min>y))
//           {
//               SQLiteDatabase sqLiteDatabase = getWritableDatabase ();
//               ContentValues contentValues = new ContentValues ();
//               contentValues.put (COL7,year);
//               contentValues.put (COL8,month);
//               contentValues.put (COL9,day);
//               long checker = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
//               if(checker==-1)
//               {
//                   return false;
//               }
//               else
//               {
//                   return  true;
//               }
//           }
//
//        }
//        while(result.moveToNext ());
//
//
//
//    }


    public Cursor display()
    {
//        Calendar calendar = Calendar.getInstance();
//        String year = String.valueOf(calendar.get(Calendar.YEAR));
//        String month = String.valueOf(calendar.get(Calendar.MONTH));
//        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        SQLiteDatabase sqLiteDatabase = getReadableDatabase ();
        Cursor res ;
        //String sql = "SELECT * FROM mytable WHERE DATEPICKER_YEAR == year AND DATEPICKER_MONTH == month AND DATEPICKER_DAY == day";
        res = sqLiteDatabase.rawQuery ("SELECT * FROM mytable",null);
        return res;

    }

    public int  delete(String id)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase ();
        int res = sqLiteDatabase.delete (TABLE_NAME," ID = ?",new String[]{ id });
        return res;

    }

}


