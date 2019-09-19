package com.example.zooapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.NonNull;

public class AlternatingColorArrayAdapter<T> extends ArrayAdapter<T> {
    public AlternatingColorArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public AlternatingColorArrayAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public AlternatingColorArrayAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
        super(context, resource, objects);
    }

    public AlternatingColorArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public AlternatingColorArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public AlternatingColorArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if (position % 2 == 1) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.colorListViewOddItemBackground));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.colorListViewEvenItemBackground));
        }

        return view;
    }

}
