package com.example.contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database {

    private final SQLiteOpenHelper open_db;
    private final SQLiteDatabase db;

    public Database(Context context) {
        open_db = new DbOpener(context, "db.sql", null, 1);
        db = open_db.getWritableDatabase();
        db.execSQL("CREATE table IF NOT EXISTS contact(number TEXT, name TEXT)");
    }

    public ArrayList<Contact> getAllContact() {
        Cursor cursor = db.rawQuery("select * from contact", null);
        ArrayList<Contact> contacts = new ArrayList<>();

        if (cursor.getCount() > 0) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String number = cursor.getString(cursor.getColumnIndex("number"));

            contacts.add(new Contact(name, number));
        }
        return contacts;
    }
}
