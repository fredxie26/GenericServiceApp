package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.helper.Person;

import java.util.List;

public class PersonAdapter extends BaseAdapter {
    private List<Person> personList;
    private Context context;

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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Person person = personList.get(position);

        // Load the drawable resource as a Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat); // Assuming cat.png is your image
        holder.personImageView.setImageBitmap(bitmap);

        holder.personTextView.setText(person.getFullInfo());

        return convertView;
    }

    static class ViewHolder {
        ImageView personImageView;
        TextView personTextView;
    }
}
