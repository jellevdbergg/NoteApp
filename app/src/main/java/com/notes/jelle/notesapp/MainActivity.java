package com.notes.jelle.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    NoteAdapter na;

    private int mSelectedColor;
    private final ArrayList<Note> noteList = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.main_listView);

        inflateFabButton();
    }


    private void inflateColorDialog (ActionMode mode) {
        final ActionMode mode_final = mode;
        mSelectedColor = ContextCompat.getColor(this, R.color.white);

        int[] mColors = getResources().getIntArray(R.array.default_rainbow);

        ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors,
                0,
                5, // Number of columns
                ColorPickerDialog.SIZE_SMALL,
                true // True or False to enable or disable the serpentine effect
                //0, // stroke width
                //Color.BLACK // stroke color
        );

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                Log.i("ok", "test this"+ mSelectedColor);
                SparseBooleanArray selected = na.getSelectedIds();
                MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
                Log.i("ok", "selected"+ selected);
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        Note selectedListItem = na.getItem(selected.keyAt(i));
                        // Remove selected items using ids from the adapter
                        selectedListItem.setBackGroundColor(mSelectedColor);
                        db.changeNoteColor(selectedListItem);
                    }
                }
                na.notifyDataSetChanged();
                mode_final.finish();
            }
        });

        dialog.show(getFragmentManager(), "color_dialog_test");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem menuItemColor;
//        MenuItem menuItemDelete;
//
//        menuItemColor = menu.findItem(R.id.action_color_change);
//        menuItemDelete = menu.findItem(R.id.action_note_delete);
//
//        menuItemColor.setEnabled(false);
//        menuItemDelete.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_main_new_note:
//                // Start node activity in newNode mode
//                startActivity(new Intent(this, NoteActivity.class));
//                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyDatabaseHelper db = new MyDatabaseHelper(this);

        this.noteList.clear();
        ArrayList<Note> list = db.getAllNotes();
        this.noteList.addAll(list);

        if (this.noteList == null || this.noteList.size() == 0) {
            Toast.makeText(this, "We have no notes saved", Toast.LENGTH_SHORT).show();
            return;
        } else {
            na = new NoteAdapter(this, R.layout.item_note, this.noteList);
            mListView.setAdapter(na);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    final Note selectedNote = (Note) mListView.getItemAtPosition(position);

                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("note", selectedNote);
                    startActivity(viewNoteIntent);
                }
            });

            createMultiSelectListeners();
        }
    }

    private void inflateFabButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NoteActivity.class));
            }
        });
    }

    private void createMultiSelectListeners () {
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Adds the menu on the top and assignes the nr of selected items
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = mListView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                na.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // call getSelectedIds method from customListViewAdapter
                SparseBooleanArray selected = na.getSelectedIds();
                switch (item.getItemId()) {
                    case R.id.action_note_delete:
//                        showAlertDialog();
                        // Captures all selected ids with a loop
                        MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Note selectedListItem = na.getItem(selected.keyAt(i));
                                // Remove selected items using ids from the adapter
                                na.remove(selectedListItem);
                                //And remove the notes from the database
                                db.deleteNote(selectedListItem);
                            }
                        }

                        Toast.makeText(getApplicationContext(),"Notes succesfully deleted ", Toast.LENGTH_LONG).show();
                        mode.finish();
                        return true;

                    case R.id.action_color_change:
                        // call getSelectedIds method from customListViewAdapter
                        Toast.makeText(getApplicationContext(),"Pick a color ", Toast.LENGTH_LONG).show();
                        inflateColorDialog(mode);
//                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                na.removeSelection();
            }
        });
    }
}
