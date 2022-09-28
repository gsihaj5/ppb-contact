package com.example.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database {

    private final SQLiteDatabase db;

    public Database(Context context) {
        SQLiteOpenHelper open_db = new DbOpener(context, "db.sql", null, 1);
        db = open_db.getWritableDatabase();
        db.execSQL("CREATE table IF NOT EXISTS contact(number TEXT, name TEXT)");
    }

    public ArrayList<Contact> getAllContact() {
        Cursor cursor = db.rawQuery("select * from contact", null);
        ArrayList<Contact> contacts = new ArrayList<>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(1);
                String number = cursor.getString(0);

                contacts.add(new Contact(name, number));
            } while (cursor.moveToNext());
        }
        return contacts;
    }

    public void addContact(Contact contact) {
        ContentValues data = new ContentValues();

        data.put("name", contact.getName());
        data.put("number", contact.getNumber());

        db.insert("contact", null, data);
    }

    public void updateContact(String oldName, Contact newContact) {
        ContentValues data = new ContentValues();

        data.put("name", newContact.getName());
        data.put("number", newContact.getNumber());
        db.update("contact", data, "name='" + oldName + "'", null);

    }

    public Contact getContact(String name) throws Exception {
        Cursor cursor = db.rawQuery("select * from contact where name like '%" + name + "%'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String cursorName = cursor.getString(1);
            String number = cursor.getString(0);

            return new Contact(cursorName, number);
        }

        throw new Exception("User Not Found");
    }

    public void deleteContact(String name) {
        db.delete("contact", "name='" + name + "'", null);
    }

}
