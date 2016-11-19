package org.esiea.puig.gnondoli.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


public class bddsqlite extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Download.db";
    public static final String NOTES_TABLE_NAME = "download_item";
    public static final String NOTES_COLUMN_ID = "id";
    public static final String NOTES_COLUMN_TITLE = "title";
    public static final String NOTES_COLUMN_DESCRIPTION = "description";
    private HashMap hp;


    public bddsqlite(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table download_item " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,description TEXT)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS download_item");
        onCreate(db);
    }


    public Long insert_item  (String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        Long DB_ID = db.insert("download_item", "null", contentValues);
        db.close();
        return DB_ID;
    }


    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from download_item where id="+id+"", null );
        return res;
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTES_TABLE_NAME);
        return numRows;
    }


    public void update_item (Integer id, String title, String description, String color, String date,String size) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        db.update("download_item", contentValues, "id = ? ", new String[] {Integer.toString(id)} );
        db.close();
    }


    public void delete_item (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete("download_item",
                "id = ? ",
                new String[] {Integer.toString(id)});
        db.close();
    }




    public List<Download_item> getAllNotes() {
        List<Download_item> array_list = new ArrayList<>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from download_item", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){


            array_list.add(new Download_item(R.mipmap.ic_launcher,
                    res.getString(res.getColumnIndex(NOTES_COLUMN_TITLE)),
                    res.getString(res.getColumnIndex(NOTES_COLUMN_DESCRIPTION))));
            res.moveToNext();
        }
        return array_list;
    }
}




