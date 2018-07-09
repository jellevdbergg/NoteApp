package com.notes.jelle.notesapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jelle_000 on 21-3-2018.
 */

public class NoteAdapter extends ArrayAdapter<Note> {

    private SparseBooleanArray selectedListItemsIds;
    List multipleSelectionList;

    public NoteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
        selectedListItemsIds = new SparseBooleanArray();
        this.multipleSelectionList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, null);
        }

        Note note = getItem(position);

        if (note != null) {
            TextView title = (TextView) convertView.findViewById(R.id.list_note_title);
            TextView content = (TextView) convertView.findViewById(R.id.list_note_content);
            TextView date = (TextView) convertView.findViewById(R.id.list_note_date);

            title.setText(note.getTitle());
            date.setText(note.getDateTimeFormatted(getContext()));

            if(note.getContent().length() > 50) {
                content.setText(note.getContent().substring(0,49));
            }
            else {
                content.setText(note.getContent());
            }
        }

        return convertView;
    }

    @Override
    public void remove(Note object) {
        multipleSelectionList.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedListItemsIds.get(position));
    }

    public void removeSelection() {
        selectedListItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedListItemsIds.put(position, value);
        else
            selectedListItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedListItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedListItemsIds;
    }
}
