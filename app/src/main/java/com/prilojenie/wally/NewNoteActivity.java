package com.prilojenie.wally;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class NewNoteActivity extends AppCompatActivity {

    public EditText titleEt;
    public EditText noteEt;

    ImageView saveNote;
    int element;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        titleEt = findViewById(R.id.titleEt);
        noteEt = findViewById(R.id.noteEt);

        titleEt.setText(getIntent().getExtras().getString("Title"));
        noteEt.setText(getIntent().getExtras().getString("Note"));
        element = getIntent().getExtras().getInt("Element");
        saveNote = findViewById(R.id.saveNote);

        sp = getSharedPreferences("MyNotes", MODE_PRIVATE);

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });



    }


    private void saveNote()
    {
        SharedPreferences.Editor editor = sp.edit();

        if(titleEt.getText().toString().isEmpty())
        {
            titleEt.setError("Input a title!");
            return;
        }

        if(noteEt.getText().toString().isEmpty())
        {
            noteEt.setError("Input a note!");
            return;
        }

        if(element == -1)
        {
            editor.putString("title" + Notepad.noteCount, titleEt.getText().toString());
            editor.putString("note" + Notepad.noteCount, noteEt.getText().toString());
            Notepad.noteCount++;
            editor.putInt("NoteCount", Notepad.noteCount);
        }
        else
        {
            editor.putString("title" + element, titleEt.getText().toString());
            editor.putString("note" + element, noteEt.getText().toString());
        }

        editor.apply();
        Notepad fragment = new Notepad();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.menuNotepad, fragment);
    }
}