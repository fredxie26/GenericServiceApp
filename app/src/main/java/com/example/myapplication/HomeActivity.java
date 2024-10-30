package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.Button;

import com.example.myapplication.helper.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

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

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, clients);
//        simpleList.setAdapter(arrayAdapter);
    }

    private void parseJson(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Person person = new Person(
                        jsonObject.getString("first_name"),
                        jsonObject.getString("last_name"),
                        jsonObject.getString("photo_path"),
                        jsonObject.getString("address"),
                        jsonObject.getString("status")
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


    /*
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    String mCurrentPhotoPath;
    private ArrayList<String> photos = null;
    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "");
        if (photos.isEmpty()) {
            displayPhoto(null);
        } else {
            displayPhoto(photos.get(index));
        }
    }
    public void takePhoto(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            //}
        }
    }
    public void scrollPhotos(View v) {
        updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());

        if (v.getId() == R.id.btnPrev) {
            if (index > 0) {
                index--;
            }
        } else if (v.getId() == R.id.btnNext) {
            if (index < (photos.size() - 1)) {
                index++;
            }
        }

        displayPhoto(photos.get(index));
    }


    private void displayPhoto(String path) {
        ImageView iv = (ImageView) findViewById(R.id.ivGallery);
        TextView tv = (TextView) findViewById(R.id.tvTimestamp);
        EditText et = (EditText) findViewById(R.id.etCaption);
        if (path == null || path.isEmpty()) {
            iv.setImageResource(R.mipmap.ic_launcher);
            et.setText("");
            tv.setText("");
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
            String[] attr = path.split("_");
            et.setText(attr[1]);
            tv.setText(attr[2]);
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date());
        String imageFileName = "_caption_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startTimestamp , endTimestamp;
                try {
                    String from = (String) data.getStringExtra("STARTTIMESTAMP");
                    String to = (String) data.getStringExtra("ENDTIMESTAMP");
                    startTimestamp = format.parse(from);
                    endTimestamp = format.parse(to);

                } catch (Exception ex) {
                    startTimestamp = null;
                    endTimestamp = null;
                }
                String keywords = (String) data.getStringExtra("KEYWORDS");
                index = 0;
                photos = findPhotos(startTimestamp, endTimestamp, keywords);

                // Display the first photo if available
                if (photos.isEmpty()) {
                    index = 0;  // Reset to the first photo
                    displayPhoto(null);
                } else {
                    displayPhoto(photos.get(index));
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "");

            // Display the first photo if available
            if (!photos.isEmpty()) {
                index = 0;  // Reset to the first photo
                displayPhoto(photos.get(index));
            } else {
                displayPhoto(null);
            }
        }
    }

    private void updatePhoto(String path, String caption) {
        String[] attr = path.split("_");
        if (attr.length >= 3) {
            File to = new File(attr[0] + "_" + caption + "_" + attr[2] + "_" + attr[3]);
            File from = new File(path);
            from.renameTo(to);
        }
    }
    private ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.myapplication/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime()
                        && f.lastModified() <= endTimestamp.getTime())
                ) && (keywords == "" || f.getPath().contains(keywords)))
                    photos.add(f.getPath());
            }
        }
        return photos;
    }
    public void filter(View v) {
        Intent i = new Intent(HomeActivity.this, SearchActivity.class);
        startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
    };

     */
}