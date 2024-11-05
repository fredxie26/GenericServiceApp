package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.Button;

import com.example.myapplication.helper.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    private List<Person> originalPersonList;
    private List<Person> filteredPersonList;
    private PersonAdapter personAdapter;
    private EditText searchEditText;
    private Button searchButton;
    private ListView personListView;

    String jsonData = "[{\"first_name\": \"Alice\", \"last_name\": \"Smith\", \"photo_path\": \"/path/to/alice.jpg\", \"address\": \"123 Maple St, New York\", \"status\": \"Active\"}," +
            "{\"first_name\": \"Bob\", \"last_name\": \"Johnson\", \"photo_path\": \"/path/to/bob.jpg\", \"address\": \"456 Oak Ave, Los Angeles\", \"status\": \"Inactive\"}," +
            "{\"first_name\": \"Charlie\", \"last_name\": \"Brown\", \"photo_path\": \"/path/to/charlie.jpg\", \"address\": \"789 Pine Rd, Chicago\", \"status\": \"Active\"}]";

    @Override   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        personListView = findViewById(R.id.personListView);

        originalPersonList = new ArrayList<>();
        filteredPersonList = new ArrayList<>();
        parseJson(jsonData);

        // Set up the adapter
        personAdapter = new PersonAdapter(this, originalPersonList);
        personListView.setAdapter(personAdapter);

        // Set up search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList();
            }
        });

        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), DetailActivity.class);
                in.putExtra("KEY_POSITION", position);
                if (filteredPersonList.size() > 0)
                {
                    in.putParcelableArrayListExtra("KEY_PERSON_LIST", (ArrayList<? extends Parcelable>) filteredPersonList);
                }
                else
                {
                    in.putParcelableArrayListExtra("KEY_PERSON_LIST", (ArrayList<? extends Parcelable>) originalPersonList);
                }
                startActivity(in);
            }
        });
    }

    private void parseJson(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Parse status as a Set<String> from a comma-separated string
                Set<String> statusSet = new HashSet<>();
                String statusString = jsonObject.getString("status");
                String[] statuses = statusString.split(",");
                for (String status : statuses) {
                    statusSet.add(status.trim().toLowerCase()); // Trim spaces if any
                }

                Person person = new Person(
                        jsonObject.getString("first_name"),
                        jsonObject.getString("last_name"),
                        jsonObject.getString("photo_path"),
                        jsonObject.getString("address"),
                        statusSet
                );
                originalPersonList.add(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void filterList() {
        String query = searchEditText.getText().toString().toLowerCase();
        filteredPersonList.clear(); // Clear the filtered list

        // Check if the query is null or empty
        if (query != null && !query.isEmpty()) {
            for (Person person : originalPersonList) {
                if (person.getFullInfo().toLowerCase().contains(query.toLowerCase())) {
                    filteredPersonList.add(person);
                }
            }
            personAdapter = new PersonAdapter(this, filteredPersonList);
        } else {
            // Reset to original list if the query is empty
            filteredPersonList.addAll(originalPersonList);
            personAdapter = new PersonAdapter(this, originalPersonList);
        }
        personListView.setAdapter(personAdapter);
    }
}