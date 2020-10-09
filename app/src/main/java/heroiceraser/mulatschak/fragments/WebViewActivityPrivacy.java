package heroiceraser.mulatschak.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import at.heroiceraser.mulatschak.R;

public class WebViewActivityPrivacy extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadUrl("https://sites.google.com/view/privacypolicymulatschak");
    }
}