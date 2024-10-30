package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.helper.Person;

import java.util.List;
import java.util.Set;

public class PersonAdapter extends BaseAdapter {
    private List<Person> personList;
    private Context context;
    private final String[] checkboxOptions = {"active", "inactive"};

    public PersonAdapter(Context context, List<Person> personList) {
        this.context = context;
        this.personList = personList;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_listview, parent, false);

            holder = new ViewHolder();
            holder.personImageView = convertView.findViewById(R.id.personImageView);
            holder.personTextView = convertView.findViewById(R.id.personTextView);
            holder.statusButton = convertView.findViewById(R.id.listStatusBtn);
            holder.checkboxLayout = convertView.findViewById(R.id.listCheckboxLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Person person = personList.get(position);

        // Load the drawable resource as a Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat); // Assuming cat.png is your image
        holder.personImageView.setImageBitmap(bitmap);

        holder.personTextView.setText(person.getFullInfo());

        // Set the status button click listener
        holder.statusButton.setOnClickListener(view -> toggleCheckboxes(holder, position));

        return convertView;
    }

    static class ViewHolder {
        ImageView personImageView;
        TextView personTextView;
        Button statusButton;
        LinearLayout checkboxLayout;
    }

    // Method to toggle the visibility of checkboxes
    private void toggleCheckboxes(ViewHolder holder, int position) {
        if (holder.checkboxLayout.getVisibility() == View.VISIBLE) {
            holder.checkboxLayout.setVisibility(View.GONE);
        } else {
            holder.checkboxLayout.setVisibility(View.VISIBLE);
            addCheckboxes(holder.checkboxLayout, personList.get(position), holder.personTextView);
        }
    }

    // Create the checkboxes based on the status
    private void addCheckboxes(LinearLayout layout, Person person, TextView personTextView) {
        layout.removeAllViews(); // Clear previous checkboxes if any

        boolean[] checkedStates = getCheckedStates(person.getStatuses());
        for (int i = 0; i < checkboxOptions.length; i++) {
            layout.addView(createCheckbox(checkboxOptions[i], checkedStates[i], person, personTextView));
        }
    }

    // Create a checkbox and set its listener
    private CheckBox createCheckbox(String option, boolean isChecked, Person person, TextView personTextView) {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setText(option);
        checkBox.setChecked(isChecked);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            String message = isChecked1 ? option + " selected" : option + " deselected";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            if (isChecked1) {
                person.addStatus(option);
            } else {
                person.removeStatus(option);
            }
            personTextView.setText(person.getFullInfo());
        });

        return checkBox;
    }

    // Determine which checkboxes should be checked based on the status
    private boolean[] getCheckedStates(Set<String> statuses) {
        boolean[] checkedStates = new boolean[checkboxOptions.length];

        for (int i = 0; i < checkboxOptions.length; i++) {
            checkedStates[i] = statuses.contains(checkboxOptions[i]);
        }

        return checkedStates;
    }
}
