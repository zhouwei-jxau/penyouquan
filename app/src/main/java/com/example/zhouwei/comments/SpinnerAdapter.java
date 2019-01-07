package com.example.zhouwei.comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhouwei on 2018/12/18.
 */

public class SpinnerAdapter extends ArrayAdapter {
    private int resourceId=-1;

    public SpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        resourceId=resource;
    }

    public SpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        resourceId=resource;
    }

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    public SpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Object[] objects) {
        super(context, resource, textViewResourceId, objects);
        resourceId=resource;
    }

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        super.getView(position, convertView, parent);
        View view=(View) LayoutInflater.from(getContext()).inflate(resourceId,null);
        String value=(String) getItem(position);
        TextView textView=(TextView)view.findViewById(R.id.spinner_value);
        textView.setText(value);
        return view;
    }
}
