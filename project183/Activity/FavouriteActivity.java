package com.example.project183.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project183.Adapter.FavouriteAdapter;
import com.example.project183.Domain.Foods;
import com.example.project183.Helper.TinyDB;
import com.example.project183.R;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    private TinyDB tinyDB;
    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_activity);

        tinyDB = new TinyDB(this);
        recyclerView = findViewById(R.id.favouriteRecyclerView);

        ArrayList<Foods> favourites = tinyDB.getListObject("favouriteList", Foods.class);
        if (favourites != null && !favourites.isEmpty()) {
            adapter = new FavouriteAdapter(favourites);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No favourites yet!", Toast.LENGTH_SHORT).show();
        }
    }
}