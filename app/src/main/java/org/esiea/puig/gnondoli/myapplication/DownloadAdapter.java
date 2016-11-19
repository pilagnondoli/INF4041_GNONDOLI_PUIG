package org.esiea.puig.gnondoli.myapplication;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class DownloadAdapter extends ArrayAdapter<Download_item> {


    ArrayList<Download_item> arraylist;
    public List<Download_item> downloadItems;



    private static final Random RANDOM = new Random();


    private class DownloadViewHolder {
        public TextView Title;
        public TextView Description;
        public ImageView image_dwn;



        private DownloadViewHolder() {
        }
    }


    public DownloadAdapter(Context context, List<Download_item> items) {
        super(context, 0, items);
        this.downloadItems = items;
        this.arraylist = new ArrayList();
        this.arraylist.addAll(this.downloadItems);
    }


    public int getCount() {
        return this.downloadItems.size();
    }


    public long getItemId(int position) {
        return (long) position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.download_item, parent, false);
        }
        DownloadViewHolder viewHolder = (DownloadViewHolder) convertView.getTag();


        if (viewHolder == null) {
            viewHolder = new DownloadViewHolder();
            viewHolder.Title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.Description = (TextView)convertView.findViewById(R.id.description);
            viewHolder.image_dwn = (ImageView)convertView.findViewById(R.id.image_dwn);


            convertView.setTag(viewHolder);
        }


        Download_item downloadItem = getItem(position);
        viewHolder.Title.setText(downloadItem.getTitle());
        viewHolder.Description.setText(downloadItem.getDescription());
        viewHolder.image_dwn.setImageResource(downloadItem.getImage_dwn());



        return convertView;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        downloadItems.clear();
        if (charText.length() == 0) {
            downloadItems.addAll(arraylist);
        } else {
            for (Download_item wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    downloadItems.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public void remove(Download_item object) {
        downloadItems.remove(object);
        notifyDataSetChanged();
    }


    public List<Download_item> getDownloadItems() {
        return downloadItems;
    }

}

