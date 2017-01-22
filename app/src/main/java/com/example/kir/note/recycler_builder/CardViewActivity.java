package com.example.kir.note.recycler_builder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kir.note.R;

public class CardViewActivity extends Activity {

    TextView number;
    TextView header;
    TextView text;
    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cardview_activity);
        number = (TextView) findViewById(R.id.note_number);
        header = (TextView) findViewById(R.id.note_header);
        text = (TextView )findViewById(R.id.note_text);
        data = (TextView) findViewById(R.id.note_data);

    }
}
