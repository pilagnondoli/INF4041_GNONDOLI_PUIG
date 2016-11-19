package org.esiea.puig.gnondoli.myapplication;

    import android.app.Application;


    import com.karumi.dexter.Dexter;




    public class PermissionActivity extends Application {


        @Override
        public void onCreate() {
            super.onCreate();
            Dexter.initialize(getApplicationContext());
        }
    }


