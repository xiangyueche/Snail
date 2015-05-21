package com.example.westsnow.myapplication;


import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by wumengting on 5/18/15.
 */
public class TimelineAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public TimelineAdapter(Context context, List<Map<String, Object>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.show_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String titleStr = list.get(position).get("title").toString();
        String timeStr = list.get(position).get("time").toString();
        System.out.println(timeStr);


        viewHolder.title.setText(titleStr);
        viewHolder.time.setText(timeStr);

        return convertView;
    }

    static class ViewHolder {
        public TextView title;
        public TextView time;
    }

}
