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
        holder.mTitle.setText(n.getTitle());
        if (n.getTitle().isEmpty()) {
            holder.mTitle.setVisibility(View.GONE);
        }
        holder.mText.setText(n.getText());
        if (n.getText().isEmpty()) {
            holder.mText.setVisibility(View.GONE);
        }
        if(n.getYear() == 0) {
            holder.mDate.setText("");
            holder.mDate.setVisibility(View.GONE);
            holder.mStatus.setText(mCtx.getString(R.string.status_no));
            holder.mStatus.setBackgroundColor(mCtx.getColor(R.color.colorGray));
        } else {
            String dateToDisplay = n.getYear() + "/" + (n.getMonth() + 1) + "/" + n.getDay();
            holder.mDate.setText(dateToDisplay);
            switch (n.getState()) {
                case 1:
                    holder.mStatus.setText(mCtx.getString(R.string.status_planned));
                    holder.mStatus.setBackgroundColor(mCtx.getColor(R.color.colorGreen));
                    break;
                case 0:
                    holder.mStatus.setText(mCtx.getString(R.string.status_today));
                    holder.mStatus.setBackgroundColor(mCtx.getColor(R.color.colorYellow));
                    break;
                case -1:
                    holder.mStatus.setText(mCtx.getString(R.string.status_overdued));
                    holder.mStatus.setBackgroundColor(mCtx.getColor(R.color.colorRed));
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mStatus, mTitle, mText, mDate;

        public TasksViewHolder(View itemView) {
            super(itemView);

            mStatus = itemView.findViewById(R.id.status);
            mTitle = itemView.findViewById(R.id.title);
            mText = itemView.findViewById(R.id.text);
            mDate = itemView.findViewById(R.id.date);


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