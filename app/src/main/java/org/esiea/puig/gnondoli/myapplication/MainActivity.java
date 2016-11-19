package org.esiea.puig.gnondoli.myapplication;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton button;
    public List<Download_item> download_items;
    public DownloadAdapter downloadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }
    public void initialize(){
        listView = (ListView)findViewById(R.id.listview);
        button = (FloatingActionButton)findViewById(R.id.fab);
        download_items = new ArrayList<>();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_items.add(new Download_item(R.mipmap.ic_launcher,"titre","description"));
                download_items.add(new Download_item(R.mipmap.ic_launcher,"titre","description"));
                downloadAdapter = new DownloadAdapter(MainActivity.this, download_items);
                listView.setAdapter(downloadAdapter);


                Toast.makeText(getApplicationContext(), "Téléchargement en cours...", Toast.LENGTH_LONG).show();
            }
        });
    }
}
