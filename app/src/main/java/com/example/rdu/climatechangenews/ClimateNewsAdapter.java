package com.example.rdu.climatechangenews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rdu on 13.10.2017.
 */

public class ClimateNewsAdapter extends ArrayAdapter<ClimateNews> {

    public ClimateNewsAdapter(Context context, ArrayList<ClimateNews> climateNews) {
        super(context, 0, climateNews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.climate_list_item, parent, false);
        }

        //find the climate news at the current position
        ClimateNews currentClimateNews = getItem(position);

        // find the textview for the author and assign it the value at that position in the list.
        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(currentClimateNews.getAuthor());

        //find the textview for the title and assign it the value at that position in the list.
        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentClimateNews.getTitle());

        //find the textview for the section and assign it the value at that position in the list.
        TextView sectionView = listItemView.findViewById(R.id.section);
        sectionView.setText(currentClimateNews.getSection());

        //find the textview for the date and assign it the value at that position in the list.
        TextView dateTextView = listItemView.findViewById(R.id.date_published);
        dateTextView.setText(currentClimateNews.getDate());

        //find the textview for the time and assign it the value at that position in the list.
        TextView timeTextView = listItemView.findViewById(R.id.time_published);
        timeTextView.setText(currentClimateNews.getTime());


        return listItemView;
    }
}
