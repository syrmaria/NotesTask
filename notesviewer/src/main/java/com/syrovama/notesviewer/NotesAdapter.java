package com.syrovama.notesviewer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Holder> {
    private static final String TAG = "MyAdapter";
    private ArrayList<Note> mNotes;

    NotesAdapter(ArrayList<Note> dataset) {
        mNotes = dataset;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup recycler, int i) {
        LayoutInflater inflater = LayoutInflater.from(recycler.getContext());
        return new Holder(inflater.inflate(R.layout.recycler_item, recycler, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.bind(mNotes.get(i));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        private final TextView mTitleTextView;
        private final TextView mContentTextView;


        Holder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mContentTextView = itemView.findViewById(R.id.content_text_view);
        }
        void bind(Note note) {
            mTitleTextView.setText(note.getTitle());
            mContentTextView.setText(note.getContent());
        }
    }
}


