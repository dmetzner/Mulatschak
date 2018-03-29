package heroiceraser.mulatschak.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import at.heroiceraser.mulatschak.R;

public class WebViewActivityRules extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadUrl("https://sites.google.com/view/mulatschak-regeln");
    }

}