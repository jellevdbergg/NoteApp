package com.notes.jelle.notesapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jelle_000 on 21-3-2018.
 */

public class Utilities {
    public static final String FILE_EXTENSION = ".bin";
    SQLiteDatabase db;

    public static boolean saveNote(Context context, Note note, SQLiteDatabase db) {
        if(note.getTitle() != "") {
            String title = note.getTitle();
            long dateTme = System.currentTimeMillis();
            String content = "NULL";

            db.execSQL("Insert into Notes values (" + title + ", "+ content + ", "+ dateTme+");");
            return true;
        }

        return false;
    }

    public static ArrayList<Note> loadNotes(Context context, SQLiteDatabase db) {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor c;

        c=db.rawQuery("Select * from Notes", null);
        c.moveToFirst();

        for (int i=0; c.moveToPosition(i); i++) {
            String title = c.getString(0);
            String content = c.getString(1);
            long datetime = c.getLong(2);
            Note dbNote = new Note(datetime, title, content);

            notes.add(dbNote);
        }

        return notes;
    }
}
