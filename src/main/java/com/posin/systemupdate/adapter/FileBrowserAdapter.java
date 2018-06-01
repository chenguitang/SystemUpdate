package com.posin.systemupdate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.posin.systemupdate.R;
import com.posin.systemupdate.bean.FilePathItem;
import com.posin.systemupdate.view.FileBrowserDialog;

import java.util.ArrayList;

/**
 * FileName: FileBrowserAdapter
 * Author: Greetty
 * Time: 2018/5/29 20:26
 * Desc: TODO
 */
public class FileBrowserAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<FilePathItem> mListPathItems;

    public FileBrowserAdapter(Context context, ArrayList<FilePathItem> listPathItems) {
        this.mContext = context;
        this.mListPathItems = listPathItems;
    }

    public void setListItems(ArrayList<FilePathItem> listPathItems) {
        this.mListPathItems = listPathItems;
    }

    @Override
    public int getCount() {
        return mListPathItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mListPathItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilePathItem item = mListPathItems.get(position);
        convertView = item.getView();

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_row, null);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

            text.setText(item.getDisplay());
            icon.setImageBitmap(item.getIcon());

            item.setView(convertView);
            convertView.setTag(item);
        }
        return convertView;
    }
}
