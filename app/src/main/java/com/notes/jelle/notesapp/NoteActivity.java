package com.notes.jelle.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import static android.os.ParcelFileDescriptor.MODE_CREATE;

public class NoteActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mContent;

    private int mNoteId;

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private int mode;

    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mTitle = findViewById(R.id.note_title);
        mContent = findViewById(R.id.note_content);

        MyDatabaseHelper db = new MyDatabaseHelper(this);

        Intent intent = this.getIntent();
        this.note = (Note) intent.getSerializableExtra("note");
        if(note== null)  {
            this.mode = MODE_CREATE;
        } else  {
            this.mode = MODE_EDIT;
            this.mTitle.setText(this.note.getTitle());
            this.mContent.setText(this.note.getContent());
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        inflateFabButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.mode == MODE_EDIT) {
            getMenuInflater().inflate(R.menu.menu_note_new, menu);
        }

        return true;
    }

    private void inflateFabButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_note_delete:
                deleteNote();
                break;
            case android.R.id.home:
                saveNote();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        saveNote();
    }

    private void saveNote () {
        MyDatabaseHelper db = new MyDatabaseHelper(this);

        String title = mTitle.getText().toString();
        String content = mContent.getText().toString();
        long dateTime = System.currentTimeMillis();

        if(title.equals("")) {
            finish();
            return;
        }

        if(mode==MODE_CREATE ) {
            this.note= new Note(dateTime, title, content);
            db.addNote(note);
        } else  {
            this.note.setTitle(title);
            this.note.setContent(content);
            this.note.setDateTime(dateTime);
            db.updateNote(note);
        }

        // Back to MainActivity.
        finish();
    }

    private void deleteNote () {
        if(mode==MODE_CREATE) {
            Toast.makeText(this,"You can't delete a node that is not yet created ", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Delete note")
                    .setMessage("Are you sure you want to delete this?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
                            db.deleteNote(note);

                            Toast.makeText(getApplicationContext(),"Note deleted ", Toast.LENGTH_LONG).show();

                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .setCancelable(false);

            dialog.show();
        }
    }
}
