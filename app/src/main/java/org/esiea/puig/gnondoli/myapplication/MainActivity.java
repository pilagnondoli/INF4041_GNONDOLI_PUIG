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
        checkAndRequestPermissions();
        initialize();


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_content);

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
    public void checkAndRequestPermissions() {


        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                List<String> grantedPermissions = new ArrayList<String>();
                for(PermissionGrantedResponse response: report.getGrantedPermissionResponses()){
                    if(!grantedPermissions.contains(response.getPermissionName())){
                        grantedPermissions.add(response.getPermissionName());
                    }
                }
                //Toast.makeText(getApplicationContext(), "Granted permissions:"+grantedPermissions.toString(), Toast.LENGTH_LONG).show();
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET);


    }




    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) { //The request code you passed along with the request.
            //grantResults holds a list of all the results for the permissions requested.
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Log.d("PermissionResult=>", "Denied");
                    return;
                }
            }
            Log.d("PermissionResult=>", "All Permissions Granted");
        }
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
                lien.setText(format.getSelectedItem().toString());
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {




            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (lien == null){
                    lien.setText("http://api.androidhive.info/feed/img/nat.jpg");
                }


                d_lien = lien.getText().toString();
                d_titre = titre.getText().toString();


                String extension = lien.getText().toString().substring(lien.getText().toString().lastIndexOf("."));


                String path = Directory.getAbsolutePath();


                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(lien.getText().toString()));


                request.allowScanningByMediaScanner();
                request.setTitle("MOBILE DEV : "+ titre);
                request.setNotificationVisibility(1);
                request.setDestinationInExternalPublicDir(path, titre + "."+extension);
                enqueue = dm.enqueue(request);


                Toast.makeText(MainActivity.this,"Téléchargement en cours ...",Toast.LENGTH_LONG).show();


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


        download_dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.rotation:
                Intent translateintent = new Intent(getApplicationContext(),TranslateActivity.class);
                startActivity(translateintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}



