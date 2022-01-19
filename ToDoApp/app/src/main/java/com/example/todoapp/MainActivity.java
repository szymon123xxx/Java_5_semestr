package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;


import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private LinkedList<Task> tasks = new LinkedList<>();
    private DBHandler dbHandler;
    TextView NewToDo;
    ImageView ImageAdd;

    Date Time = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM");
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat2 = new SimpleDateFormat("MMM d, yyyy ");

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_view);
        TextView currentDay = findViewById(R.id.todayDate);
        NewToDo = findViewById(R.id.addMeTextMain);
        ImageAdd = findViewById(R.id.addMeMain);


        dbHandler = new DBHandler(this);


        recycleViewAdapter = new RecycleViewAdapter();
        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentDay.setText(dateFormat.format(Time));



        NewToDo.setOnClickListener(v -> {
            addTask();
            getTasks();
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        });
        ImageAdd.setOnClickListener(v -> {
            addTask();
            getTasks();
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        });


    }

    @Override
    protected void onDestroy() {
        dbHandler.close();
        super.onDestroy();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        getTasks();
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }

    private void getTasks(){
        tasks.clear();
        Cursor cursor = dbHandler.getTask();

        if (cursor.getCount() == 0)
            Toast.makeText(this,"EMPTY", Toast.LENGTH_SHORT).show();
        else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                Date date = new Date(cursor.getLong(2));
                boolean solve = (cursor.getInt(3) > 0);
                String picture = cursor.getString(4);
                tasks.add(new Task(id, name, date, solve, picture));
            }
        }
    }

    private void addTask(){
        String name = "New Task";
        Date date = new Date();
        dbHandler.addTask(new Task(name, date, false, "null"));

    }

    public class RecycleViewAdapter extends RecyclerView.Adapter<MainActivity.RecycleViewAdapter.ViewHolder> implements Filterable {
        LinkedList<Task> searchTasks ;


        @Override
        public Filter getFilter() {

            return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                LinkedList<Task> filteredList = new LinkedList<>();
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(searchTasks);

                }
                else {
                    for (Task task: searchTasks){
                        if (task.getmTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(task);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (constraint.equals("")){
                    getTasks();
                }
                else {
                    tasks.clear();
                    tasks.addAll((Collection<? extends Task>) results.values);

                }
                notifyDataSetChanged();

            }
        };

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final TextView wordText;
            public final TextView setDate;
            public final ImageView setBox;
            public final ImageView add;
            public final TextView add2;
            private final ImageView imageViewDelete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                System.out.println("searchtask " + searchTasks);

                wordText = itemView.findViewById(R.id.word);
                setDate = itemView.findViewById(R.id.Date);
                setBox = itemView.findViewById(R.id.ImageCheckBox);
                add = itemView.findViewById(R.id.addMe);
                searchTasks = new LinkedList<>(tasks);
                add2 = itemView.findViewById(R.id.addMeText);
                imageViewDelete = itemView.findViewById(R.id.delete);

                itemView.setOnClickListener(this);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                Task current = tasks.get(getAdapterPosition());
                Intent intent = new Intent( MainActivity.this, DetailActivity.class);
                intent.putExtra("id", current.getId());
                intent.putExtra("title", current.getmTitle());
                intent.putExtra("solved", current.ismSolved());
                intent.putExtra("date", current.getmDate().toString());
                intent.putExtra("picture", current.getmPicture());

                startActivity(intent);
                notifyDataSetChanged();
                getTasks();
            }
        }


        @NonNull
        @Override
        public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecycleViewAdapter.ViewHolder(LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.recycle_view_item, parent, false));

        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
            Task current = tasks.get(position);
            holder.wordText.setText(current.getmTitle());
            if (current.getmDate() != null)
                holder.setDate.setText(dateFormat2.format(current.getmDate()));
            holder.add.setVisibility(View.GONE);
            holder.add2.setVisibility(View.GONE);


            if (current.ismSolved()) {
                holder.setBox.setVisibility(View.VISIBLE);
                holder.wordText.setPaintFlags(holder.wordText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }else {
                holder.setBox.setVisibility(View.INVISIBLE);
            }


            if(position == tasks.size()-1){
                holder.add.setVisibility(View.VISIBLE);
                holder.add2.setVisibility(View.VISIBLE);
            }

            if (tasks.size() > 0){
                NewToDo.setVisibility(View.GONE);
                ImageAdd.setVisibility(View.GONE);
            }

            if (tasks.isEmpty()){
                NewToDo.setVisibility(View.VISIBLE);
                ImageAdd.setVisibility(View.VISIBLE);
            }

            holder.add.setOnClickListener(v -> {
                addTask();
                getTasks();
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            });

            holder.add2.setOnClickListener(v -> {
                addTask();
                getTasks();
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            });

            holder.imageViewDelete.setOnClickListener(v -> {
                if  (holder.wordText.getPaint().isStrikeThruText())
                    holder.wordText.setPaintFlags(holder.wordText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                dbHandler.deleteTask(current.getId());
                getTasks();

                if (tasks.isEmpty()){
                    NewToDo.setVisibility(View.VISIBLE);
                    ImageAdd.setVisibility(View.VISIBLE);
                }

                notifyDataSetChanged();

            });


        }

        @Override
        public int getItemCount() {
            return tasks.size();
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
                recycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}






