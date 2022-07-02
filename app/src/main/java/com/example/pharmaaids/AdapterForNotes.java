package com.example.pharmaaids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterForNotes extends RecyclerView.Adapter<AdapterForNotes.MyViewHolder> {

    private Context context;
    private List<Notes> notes;

    public AdapterForNotes(Context context, List<Notes> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_user_notes,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Notes note = notes.get(position);

        holder.textView1.setText(note.getTask());
        holder.textView2.setText(note.getHow_much());
        holder.textView3.setText(note.getTimes());
        holder.textView4.setText(note.getDay()+"-"+note.getMonth()+"-"+note.getYear());
        holder.textView5.setText(note.getHour()+":"+note.getMin());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1,textView2,textView3,textView4,textView5;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.notes_items_taskId);
            textView2 = itemView.findViewById(R.id.notes_items_how_muchId);
            textView3 = itemView.findViewById(R.id.notes_items_per_day_timesId);
            textView4 = itemView.findViewById(R.id.notes_items_dateId);
            textView5 = itemView.findViewById(R.id.notes_items_timeId);
        }
    }
}
