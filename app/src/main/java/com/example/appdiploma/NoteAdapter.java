package com.example.appdiploma;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiploma.activities.UpdateNoteActivity;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<Note> noteList;

    public NoteAdapter(Context mCtx, List<Note> noteList) {
        this.mCtx = mCtx;
        this.noteList = noteList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.note_card, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Note n = noteList.get(position);
        holder.textViewTitle.setText(n.getTitle());
        holder.textViewDesc.setText(n.getText());
        if(n.getYear() == 0) {
            holder.textViewFinishBy.setText("");
            holder.textViewFinishBy.setVisibility(View.GONE);
            holder.textViewStatus.setText(mCtx.getString(R.string.status_no));
            holder.textViewStatus.setBackgroundColor(mCtx.getColor(R.color.colorGray));
        } else {
            String dateToDisplay = n.getYear() + "/" + (n.getMonth() + 1) + "/" + n.getDay();
            holder.textViewFinishBy.setText(dateToDisplay);
            switch (n.getState()) {
                case 1:
                    holder.textViewStatus.setText(mCtx.getString(R.string.status_planned));
                    holder.textViewStatus.setBackgroundColor(mCtx.getColor(R.color.colorGreen));
                    break;
                case 0:
                    holder.textViewStatus.setText(mCtx.getString(R.string.status_today));
                    holder.textViewStatus.setBackgroundColor(mCtx.getColor(R.color.colorYellow));
                    break;
                case -1:
                    holder.textViewStatus.setText(mCtx.getString(R.string.status_overdued));
                    holder.textViewStatus.setBackgroundColor(mCtx.getColor(R.color.colorRed));
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStatus, textViewTitle, textViewDesc, textViewFinishBy;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewStatus = itemView.findViewById(R.id.status);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewDesc = itemView.findViewById(R.id.text);
            textViewFinishBy = itemView.findViewById(R.id.date);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Note note = noteList.get(getAdapterPosition());

            Intent intent = new Intent(mCtx, UpdateNoteActivity.class);
            intent.putExtra("task", note);

            mCtx.startActivity(intent);
        }
    }
}