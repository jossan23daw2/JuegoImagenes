package com.example.juegominecraft;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class AjustosActivity extends AppCompatActivity {

    private WebView navegador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustos);

        navegador = findViewById(R.id.webkit);
        navegador.getSettings().setJavaScriptEnabled(true);
        navegador.getSettings().setDatabaseEnabled(true);
        navegador.getSettings().setDomStorageEnabled(true);
        navegador.setWebViewClient(new WebViewClient());
        navegador.loadUrl("file:///android_asset/index.html");
    }
}
