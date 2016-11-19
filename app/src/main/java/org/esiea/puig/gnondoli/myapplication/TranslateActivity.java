package org.esiea.puig.gnondoli.myapplication;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.GoogleAPI;
import com.google.api.GoogleAPIException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.google.api.translate.TranslateV2;

public class TranslateActivity extends AppCompatActivity {
    EditText texte;
    TextView result;
    Button translate_button;
    Translate texte_translate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_activity);

        initialise();
    }




    public void initialise(){


        texte = (EditText)findViewById(R.id.texte);
        result = (TextView)findViewById(R.id.result);
        translate_button = (Button)findViewById(R.id.translate);


        texte_translate = new Translate() {
            @Override
            public String execute(String s, Language language, Language language1) throws GoogleAPIException {
                return null;
            }

            @Override
            public String[] execute(String[] strings, Language language, Language language1) throws GoogleAPIException {
                return new String[0];
            }

            @Override
            public String[] execute(String s, Language language, Language[] languages) throws GoogleAPIException {
                return new String[0];
            }

            @Override
            public String[] execute(String[] strings, Language[] languages, Language[] languages1) throws GoogleAPIException {
                return new String[0];
            }
        };



        translate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleAPI.setHttpReferrer("http://code.google.com/p/google-api-translate-java/");
                GoogleAPI.setKey("AIzaSyA8URzp5CanNk1nqe82lCiv0PIQKvdrejA");

                try {
                    result.setText(Translate.DEFAULT.execute(texte.getText().toString(),
                            Language.AUTO_DETECT,
                            Language.ENGLISH));
                } catch (GoogleAPIException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
