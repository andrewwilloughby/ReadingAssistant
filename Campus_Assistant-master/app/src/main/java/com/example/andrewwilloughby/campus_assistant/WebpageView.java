package com.example.andrewwilloughby.campus_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebpageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String pageURL = intent.getExtras().getString("webpageURL");
        String pageName = intent.getExtras().getString("webpageName");

        setTitle(pageName);

        if (pageURL != null){
            if (pageURL.contains(".pdf")) {
                pageURL = "https://docs.google.com/gview?embedded=true&url=" + pageURL;
            }
        }

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl(pageURL);
        setContentView(webView);
    }
}
