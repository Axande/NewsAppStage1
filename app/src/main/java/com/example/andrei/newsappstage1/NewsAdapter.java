package com.example.andrei.newsappstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Andrei on 13.04.2018.
 * <p>
 * An adapter for the list of news on the main screen.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, ArrayList<News> news) {
        //2nd field is used when populating a single TextView
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            //inflate the list item layout and save the layout details
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_layout, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.individual_news_title);
            holder.dateTextView = convertView.findViewById(R.id.individual_news_date);
            holder.timeTextView = convertView.findViewById(R.id.individual_news_time);
            holder.categoryTextView = convertView.findViewById(R.id.individual_category);
            holder.authorTextView = convertView.findViewById(R.id.individual_author);
            convertView.setTag(holder);
        } else { //reuse the layout
            holder = (ViewHolder) convertView.getTag();
        }
        News currentNews = getItem(position);

        //update teh fields
        holder.titleTextView.setText(currentNews.getTitle());
        holder.dateTextView.setText(currentNews.getDate());
        holder.timeTextView.setText(currentNews.getTime());
        holder.categoryTextView.setText(currentNews.getCategory());
        if (currentNews.getAuthor().equals("")) {
            holder.authorTextView.setVisibility(View.GONE);
        } else {
            holder.authorTextView.setText(currentNews.getAuthor());
            holder.authorTextView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView categoryTextView;
        private TextView authorTextView;
    }
}
