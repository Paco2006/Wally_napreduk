package com.prilojenie.wally;

import static android.content.Context.MODE_PRIVATE;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;

public class Notepad extends Fragment {

    TextView toolbarTitle;
    ListView notesView;

    public static int noteCount;
    public static ArrayList<String> noteTitles = new ArrayList<>();
    ArrayAdapter<String> notesAdapter;
    private ArrayList<Note> notes = new ArrayList<>();

    SharedPreferences sharedPreferences;

    ImageView addNote;

    Activity activity;

    View parentHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        activity = getActivity();
        parentHolder = inflater.inflate(R.layout.notepad, container, false);
        super.onCreate(savedInstanceState);
//        activity.setContentView(R.layout.notepad);

        sharedPreferences = activity.getSharedPreferences("MODE", MODE_PRIVATE);
        addNote = parentHolder.findViewById(R.id.addNote);

        notesView = parentHolder.findViewById(R.id.notesList);

        notesAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, noteTitles);
        loadNotes();
        notesView.setAdapter(notesAdapter);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNote("", "", -1);
            }
        });
        notesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addNewNote(notes.get(i).getTitle(), notes.get(i).getNote(), i);
            }
        });
        return parentHolder;
    }

    private void loadNotes()
    {
        SharedPreferences sharedPref = activity.getSharedPreferences("MyNotes", MODE_PRIVATE);
        noteCount = sharedPref.getInt("NoteCount", 0);
        noteTitles.clear();
        notes.clear();
        // Load the notes
        String tempTitle = "";
        String tempNote = "";
        for(int i = 0; i < noteCount; i++)
        {
            tempTitle = sharedPref.getString("title" + i, "");
            tempNote = sharedPref.getString("note" + i, "");
            Note n = new Note(tempTitle, tempNote);
            noteTitles.add(tempTitle);
            notes.add(n);
        }
        notesAdapter.notifyDataSetChanged();
    }

    private void addNewNote(String title, String note, int element)
    {
        Intent newNote = new Intent(activity, NewNoteActivity.class);
        newNote.putExtra("Title", title);
        newNote.putExtra("Note", note);
        newNote.putExtra("Element", element);
        startActivity(newNote);
    }
}
