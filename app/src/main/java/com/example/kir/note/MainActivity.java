package com.example.kir.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kir.note.recycler_builder.Adapter;
import com.example.kir.note.recycler_builder.NotePreview;

import java.util.ArrayList;
import java.util.List;

//экшн моде, для удаления
//шрифты
//добавить поиск, менюшку, кароче красоту навести
//посмотреть разный варианты клавиатура(напр. голосовой ввод туда отправить)

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NUMBER = "number_key";

    private List<NotePreview> notes;
    private RecyclerView rv;

    private void init(){
        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TextEditor.class);
                intent.putExtra(EXTRA_NUMBER, "-1");
                startActivity(intent);
            }
        });

        init();

    }

    private void refreshNotes(){
        notes = new ArrayList<>();
        NoteDB.readFromDB(new View(this), notes);

        Adapter adapter = new Adapter(notes);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshNotes();

    }
}
