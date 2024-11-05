package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.helper.Person;
import com.example.myapplication.helper.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends Activity {

    Intent intent;
    ArrayList<Person> data;
    int position;
    private ImageView picture;
    private TextView name;
    private TextView address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        picture = findViewById(R.id.detailImage);
        name = findViewById(R.id.detailName);
        address = findViewById(R.id.detailAddress);

        intent = getIntent();
        position = getIntent().getExtras().getInt("KEY_POSITION");
        data = Objects.requireNonNull(getIntent().getExtras()).getParcelableArrayList("KEY_PERSON_LIST");

        changePerson(position);

        picture.setOnTouchListener(new OnSwipeTouchListener(DetailActivity.this) {
            public void onSwipeRight() {
                if (position < data.size())
                    position++;
                else
                    position = 0;
                changePerson(position);
            }
            public void onSwipeLeft() {
                if (position > 0)
                    position--;
                else
                    position = data.size();
                changePerson(position);
            }
        });

        name.setOnTouchListener(new OnSwipeTouchListener(DetailActivity.this) {
            public void onSwipeRight() {
                if (position < data.size())
                    position++;
                else
                    position = 0;
                changePerson(position);
            }
            public void onSwipeLeft() {
                if (position > 0)
                    position--;
                else
                    position = data.size();
                changePerson(position);
            }
        });

        address.setOnTouchListener(new OnSwipeTouchListener(DetailActivity.this) {
            public void onSwipeRight() {
                if (position < data.size())
                    position++;
                else
                    position = 0;
                changePerson(position);
            }
            public void onSwipeLeft() {
                if (position > 0)
                    position--;
                else
                    position = data.size();
                changePerson(position);
            }
        });

    }

    private void changePerson (int pos)
    {
        Person person = data.get(pos);

        // Load the image from the local path
        String photoPath = person.getPhotoPath(); // Assuming this method returns the local file path
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath); // Load the image from the file

        if (bitmap != null) {
            picture.setImageBitmap(bitmap); // Set the bitmap to the ImageView
        } else {
            // Optionally set a placeholder image if the bitmap is null
            picture.setImageResource(R.drawable.cat); // Replace with your placeholder image
        }
        name = findViewById(R.id.detailName);
        address = findViewById(R.id.detailAddress);
        name.setText(person.getName());
        address.setText(person.getAddress());
    }

}
