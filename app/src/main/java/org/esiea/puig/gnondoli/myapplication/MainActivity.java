package org.esiea.puig.gnondoli.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
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
    public void checkAndRequestPermissions() {


        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                List<String> grantedPermissions = new ArrayList<String>();
                for(PermissionGrantedResponse response: report.getGrantedPermissionResponses()){
                    if(!grantedPermissions.contains(response.getPermissionName())){
                        grantedPermissions.add(response.getPermissionName());
                    }
                }
                Toast.makeText(getApplicationContext(), "Granted permissions:"+grantedPermissions.toString(), Toast.LENGTH_LONG).show();
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





}
