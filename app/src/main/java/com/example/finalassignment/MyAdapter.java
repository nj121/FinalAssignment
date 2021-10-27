package com.example.finalassignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {
    private static final String TAG = "rrr";

    public MyAdapter(@NonNull Context context, int resource, ArrayList<HashMap<String,String>> list) {
        super(context, resource,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Map<String,String> map = (Map<String,String>)getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.item_title);
        TextView content = (TextView) itemView.findViewById(R.id.item_content);
        title.setText(map.get("title"));
        content.setText(map.get("content"));
        return itemView;
    }
}
