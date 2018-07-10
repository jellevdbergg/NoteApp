package com.notes.jelle.notesapp;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jelle_000 on 21-3-2018.
 */

public class Note implements Serializable {
    private long mDateTime;
    private String mTitle;
    private String mContent;
    private int noteId;
    private int backGroundColor;

    public Note() {
    }

    public Note(long mDateTime, String mTitle, String mContent) {
        this.mDateTime = mDateTime;
        this.mTitle = mTitle;
        this.mContent = mContent;
    }

    public long getDateTime() {
        return mDateTime;
    }


    public int getBackGroundColor() { return backGroundColor; }

    public int getId() {
        return noteId;
    }

    public void setId(int id) {
        this.noteId = id;
    }

    public void setBackGroundColor (int color) { this.backGroundColor = color;}

    public void setDateTime(long mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getDateTimeFormatted(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", context.getResources().getConfiguration().locale);

        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(new Date(mDateTime));
    }
}
