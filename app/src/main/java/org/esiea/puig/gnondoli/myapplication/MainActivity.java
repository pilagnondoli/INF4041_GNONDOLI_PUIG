package org.esiea.puig.gnondoli.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;



public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton button;
    public List<Download_item> download_items;
    public DownloadAdapter downloadAdapter;
    Dialog download_dialog;


    private long enqueue;
    private DownloadManager dm;
    File Directory;


    private String d_titre,d_lien;
    public bddsqlite base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_content);
        coordinatorLayout.setBackgroundColor(Color.GRAY);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                String action = intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){


                    Toast.makeText(MainActivity.this,"Téléchargement terminé",Toast.LENGTH_LONG).show();


                    download_items.add(new Download_item(R.mipmap.ic_launcher,d_titre,d_lien));


                    downloadAdapter = new DownloadAdapter(MainActivity.this,download_items);
                    listView.setAdapter(downloadAdapter);
                    Long id = base.insert_item(d_titre, d_lien);
                    Toast.makeText(MainActivity.this,"Bien inséré dans la base de données" + String.valueOf(id),Toast.LENGTH_LONG).show();
                }
            }
        };


        registerReceiver(broadcastReceiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    public void initialize(){

        base = new bddsqlite(this);
        listView = (ListView)findViewById(R.id.listview);
        button = (FloatingActionButton)findViewById(R.id.fab);
        download_items = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Directory = new File(Environment.getExternalStorageDirectory(), "MobileDev/download");
                if (!Directory.exists()) {
                    Directory.mkdirs();
                }


                show_download_dialog();

            }
        });

        download_items = base.getAllNotes();
        downloadAdapter = new DownloadAdapter(MainActivity.this,download_items);
        listView.setAdapter(downloadAdapter);

    }

    public void show_download_dialog(){
        download_dialog = new Dialog(this);
        download_dialog.requestWindowFeature(1);
        download_dialog.setContentView(R.layout.download_dialog);


        final EditText lien = (EditText)download_dialog.findViewById(R.id.lien);
        final EditText titre = (EditText)download_dialog.findViewById(R.id.titre);
        final Button download = (Button)download_dialog.findViewById(R.id.telecharger);
        final Spinner format = (Spinner)download_dialog.findViewById(R.id.spinner_format);
        final Button open_in_browser = (Button)download_dialog.findViewById(R.id.open_in_browser);
        final Button open_app = (Button)download_dialog.findViewById(R.id.open_app);

        format.setPrompt("Choose format");

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.download,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        format.setAdapter(staticAdapter);




        format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(format.getSelectedItem().toString() == "selectionner un lien ou le saisir"){
                    lien.setText("http://api.androidhive.info/feed/img/nat.jpg");
                }
                else {
                    lien.setText(format.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                d_lien = lien.getText().toString();
                d_titre = titre.getText().toString();


                //String extension = lien.getText().toString().substring(lien.getText().toString().lastIndexOf("."));

                if(isOnline()){
                    if(isValidUrl(lien.getText().toString())){
                        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(lien.getText().toString()));


                        request.allowScanningByMediaScanner();
                        request.setTitle("MOBILE DEV : "+ titre);
                        request.setNotificationVisibility(1);
                        enqueue = dm.enqueue(request);


                        Toast.makeText(MainActivity.this,"Téléchargement en cours ...",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"Pas de connexion internet ...",Toast.LENGTH_LONG).show();
                }

                download_dialog.cancel();
            }
        });


        open_in_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://inf4042.fabrigli.fr/"));
                startActivity(browserIntent);

                download_dialog.cancel();
            }
        });
        open_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.android.vending"));
                startActivity(appIntent);

                download_dialog.cancel();
            }
        });

        download_dialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.rotation:
                Intent translateintent = new Intent(getApplicationContext(),JsonActivity.class);
                startActivity(translateintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}


