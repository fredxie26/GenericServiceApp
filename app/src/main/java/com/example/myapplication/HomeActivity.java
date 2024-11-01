package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import com.example.myapplication.helper.Person;
import com.example.myapplication.net.WebAccess;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private List<Person> originalPersonList;
    private List<Person> filteredPersonList;
    private PersonAdapter personAdapter;
    private EditText searchEditText;
    private Button searchButton;
    private ListView personListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        personListView = findViewById(R.id.personListView);

        originalPersonList = new ArrayList<>();
        filteredPersonList = new ArrayList<>();

        // Instantiate WebAccess and fetch JSON data
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebAccess webAccess = new WebAccess("http://10.0.2.2:8081/MyWebApp/data.json");
                originalPersonList = webAccess.fetchAndParseJson();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI with the result
                        personAdapter = new PersonAdapter(HomeActivity.this, originalPersonList);
                        personListView.setAdapter(personAdapter);
                    }
                });
            }
        }).start();

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
    }

    private void filterList() {
        String query = searchEditText.getText().toString().toLowerCase();
        filteredPersonList.clear(); // Clear the filtered list

        // Check if the query is null or empty
        if (query != null && !query.isEmpty()) {
            for (Person person : originalPersonList) {
                if (person.getFullInfo().toLowerCase().contains(query)) {
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
