package adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nandanarafiardika.notes.NotesListActivity;
import com.nandanarafiardika.notes.NotesViewActivity;
import com.nandanarafiardika.notes.R;
import java.io.Serializable;
import java.util.ArrayList;

import model.DbHelper;
import model.Notes;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private ArrayList<Notes> listNotes = new ArrayList<>();
    private Activity activity;
    private DbHelper dbHelper;

    public NotesAdapter(Activity activity){
        this.activity = activity;
        dbHelper = new DbHelper(activity);
    }

    public ArrayList<Notes> getListNotes(){
        return listNotes;
    }

    public void setListNotes(ArrayList<Notes> listNotes){
        if(listNotes.size() > 0){
            this.listNotes.clear();
        }
        this.listNotes.addAll(listNotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_list_card, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.card_title.setText(listNotes.get(position).getTitle());
        holder.card_body.setText(listNotes.get(position).getContent());
        holder.card_body.setOnClickListener(view -> {
           Intent edit = new Intent(activity, NotesViewActivity.class);
           edit.putExtra("notes_data", (Serializable) listNotes.get(position));
           activity.startActivity(edit);
        });

        holder.notes_delete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Do you want to delete this note?");
            builder.setMessage("This action can't be undone");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dbHelper.deleteNotes(listNotes.get(position).getId());
                Toast.makeText(activity, "Note Deleted", Toast.LENGTH_SHORT).show();
                Intent delete = new Intent(activity, NotesListActivity.class);
                delete.putExtra("currentUser", listNotes.get(position).getOwner());
                delete.putExtra("password", listNotes.get(position).getPass());
                activity.startActivity(delete);
                activity.finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView card_title, card_body;
        ImageView notes_delete;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            card_title = itemView.findViewById(R.id.card_title);
            card_body = itemView.findViewById(R.id.notes_edit);
            notes_delete = itemView.findViewById(R.id.notes_delete);
        }
    }
}
