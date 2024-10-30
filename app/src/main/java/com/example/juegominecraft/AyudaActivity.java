package com.example.juegominecraft;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class AyudaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        WebView navegador = findViewById(R.id.webview);
        navegador.getSettings().setJavaScriptEnabled(true);
        navegador.setWebViewClient(new WebViewClient());

        navegador.addJavascriptInterface(new WebAppInterface(), "Android");

        navegador.loadUrl("file:///android_asset/index.html");
    }

    private class WebAppInterface {
        @JavascriptInterface
        public void anarMain() {
            finish();
        }
    }
}

