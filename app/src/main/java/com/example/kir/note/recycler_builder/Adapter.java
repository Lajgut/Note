package com.example.kir.note.recycler_builder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kir.note.NoteDB;
import com.example.kir.note.R;
import com.example.kir.note.TextEditor;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.PersonViewHolder> {

    public static final String EXTRA_TEXT = "text_key";
    public static final String EXTRA_HEADER = "header_key";
    public static final String EXTRA_NUMBER = "number_key";


    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView header;
        TextView text;
        TextView data;
        TextView number;


        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            number = (TextView) itemView.findViewById(R.id.note_number);
            header = (TextView) itemView.findViewById(R.id.note_header);
            text = (TextView) itemView.findViewById(R.id.note_text);
            data = (TextView) itemView.findViewById(R.id.note_data);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TextEditor.class);
                    intent.putExtra(EXTRA_NUMBER, number.getText());
                    intent.putExtra(EXTRA_TEXT, text.getText());
                    intent.putExtra(EXTRA_HEADER, header.getText());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    List<NotePreview> notes;

    public Adapter(List<NotePreview> notes){
        this.notes = notes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        personViewHolder.number.setText(notes.get(i).number);
        personViewHolder.header.setText(notes.get(i).header);
        personViewHolder.text.setText(notes.get(i).text);
        personViewHolder.data.setText(notes.get(i).data);


        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v)
            { Intent intent = new Intent(v.getContext(), TextEditor.class);
                intent.putExtra(EXTRA_NUMBER, personViewHolder.number.getText());
                intent.putExtra(EXTRA_TEXT, personViewHolder.text.getText());
                intent.putExtra(EXTRA_HEADER, personViewHolder.header.getText());
                v.getContext().startActivity(intent);
            }
        });
        personViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(final View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(personViewHolder.itemView.getContext());
                builder.setTitle(R.string.alert1)
                        .setMessage(R.string.alert2)
                        .setCancelable(false)
                        .setPositiveButton(R.string.alert3, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String mId = String.valueOf(personViewHolder.number.getText());
                                SQLiteDatabase database;
                                NoteDB savedNotes = new NoteDB(v.getContext());
                                database = savedNotes.getWritableDatabase(); database.delete(NoteDB.TABLE_NOTES, NoteDB.KEY_ID + "=" + mId, null);
                                savedNotes.close();
                                remove(i);

                            }
                        })
                        .setNegativeButton(R.string.alert4, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create(); alert.show(); return true; } });
    }
    public void remove(int i) {
        notes.remove(notes.get(i));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
