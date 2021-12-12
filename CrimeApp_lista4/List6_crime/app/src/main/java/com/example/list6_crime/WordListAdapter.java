package com.example.list6_crime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LinkedList<Crime> wordList;
    private Context mContext;
    private LayoutInflater inflater;

    public WordListAdapter(Context context, LinkedList<Crime> wordList){
        inflater = LayoutInflater.from(context);
        this.wordList = wordList;
        this.mContext = context;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        public final TextView wordText;
        final WordListAdapter adapter;
        public final TextView setCrime;
        public final ImageView setImage;

        public WordViewHolder(@NonNull View itemView, WordListAdapter adapter) {
            super(itemView);
            wordText = itemView.findViewById(R.id.word);
            setCrime = itemView.findViewById(R.id.Date);
            setImage = itemView.findViewById(R.id.Image);
            this.adapter = adapter;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Crime currentCrime = wordList.get(getAbsoluteAdapterPosition());
            Intent intent = new Intent( mContext, DetailActivity.class);
            intent.putExtra("title", currentCrime.getmId().toString());
//            intent.putExtra("title2", currentCrime.getmId2());
            intent.putExtra("title3", getAbsoluteAdapterPosition());

            mContext.startActivity(intent);
            adapter.notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public WordListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.word_list_item, parent, false);
        return new WordViewHolder(itemView, this);

    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.WordViewHolder holder, int position) {

        Crime current = wordList.get(position);
        holder.wordText.setText(current.getmTitle());
        if (current.getmDate() != null)
            holder.setCrime.setText(current.getmDate().toString());
        if (current.ismSolved())
            holder.setImage.setVisibility(View.VISIBLE);
        else
            holder.setImage.setVisibility(View.INVISIBLE);
    }


    @Override
    public int getItemCount() {
        return wordList.size();
    }

}
