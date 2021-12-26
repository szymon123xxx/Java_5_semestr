package com.example.list6_crime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Date currentTime = Calendar.getInstance().getTime();

    private RecyclerView recyclerView;
    private WordListAdapter wordListAdapter;
    private DBHandler dbHandler;

    private LinkedList<Crime> crimes = new LinkedList<>();



    Button createCrime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        createCrime = findViewById(R.id.createCrime);

        dbHandler = new DBHandler(this);
        getCrimes();

        wordListAdapter = new WordListAdapter();
        recyclerView.setAdapter(wordListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createCrime.setOnClickListener(v -> {
            addCrime();
            getCrimes();
            recyclerView.getAdapter().notifyDataSetChanged();
        });


    }


    @Override
    protected void onDestroy() {
        dbHandler.close();
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCrimes();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void getCrimes(){
        crimes.clear();
        Cursor cursor = dbHandler.getCrime();

        if (cursor.getCount() == 0)
            Toast.makeText(this,"EMPTY", Toast.LENGTH_SHORT).show();
        else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                Date date = new Date(cursor.getLong(2));
                boolean solve = (cursor.getInt(3) > 0);
                String picture = cursor.getString(4);
                crimes.add(new Crime(id, name, date, solve, picture));
            }
        }
    }


    private void addCrime(){
        String name = "New crime " + crimes.size();
        Date date = new Date();
        dbHandler.addCrime(new Crime(name, date, false, "null"));
        Log.d("elo", String.valueOf(currentTime));

    }

    public class WordListAdapter extends RecyclerView.Adapter<MainActivity.WordListAdapter.ViewHolder> implements Filterable {
        LinkedList<Crime> crimesAll;
        public WordListAdapter(){
            this.crimesAll = new LinkedList<>(crimes);
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<Crime> filteredList = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(crimesAll);
                }
                else {
                    for (Crime crime: crimesAll){
                        if (crime.getmTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(crime);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                crimes.clear();
                crimes.addAll((Collection<? extends Crime>) results.values);
                notifyDataSetChanged();
            }
        };


        class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

            public final TextView wordText;
            public final TextView setCrime;
            public final ImageView setImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                wordText = itemView.findViewById(R.id.word);
                setCrime = itemView.findViewById(R.id.Date);
                setImage = itemView.findViewById(R.id.Image);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                Crime current = crimes.get(getAbsoluteAdapterPosition());
                Intent intent = new Intent( MainActivity.this, DetailActivity.class);
                intent.putExtra("id", current.getmId2());
                intent.putExtra("title", current.getmTitle());
                intent.putExtra("solved", current.ismSolved());
                intent.putExtra("date", current.getmDate().toString());
                intent.putExtra("picture", current.getmPicture());

                startActivity(intent);
                notifyDataSetChanged();
                getCrimes();



            }
        }

        @NonNull
        @Override
        public WordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WordListAdapter.ViewHolder(LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.word_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Crime current = crimes.get(position);
            holder.wordText.setText(current.getmTitle());
            if (current.getmDate() != null)
                holder.setCrime.setText(current.getmDate().toString());
            if (current.ismSolved())
                holder.setImage.setVisibility(View.VISIBLE);
            else
                holder.setImage.setVisibility(View.INVISIBLE);
            getCrimes();


        }

        @Override
        public int getItemCount() {
            return crimes.size();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        searchView.setQueryHint("Type here to search");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wordListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}