package com.mitchwongho.d3;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.mitchwongho.d3.domain.DataPoint;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView( R.id.webview)
    WebView webview;

    public class WebAppInterface {
        private Context context;

        public WebAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public String loadData() {
//            return "[{\"letter\": \"A\", \"frequency\": \".09\" },{\"letter\": \"B\", \"frequency\": \".05\" }]";
            return createDataSet();
        }

        private final String createDataSet() {
            final Random rand = new Random(System.currentTimeMillis());
            final String[] x = new String[] {
                    "A", "B", "C", "D", "E", "F",
                    "G", "H", "I", "J", "K", "L",
                    "M", "N", "O", "P", "Q", "R",
                    "S", "T", "U", "V", "W", "X",
                    "Y", "Z"};
            final List<DataPoint> set = new ArrayList<DataPoint>();
            for (int i = 0; i < x.length ; i++) {
                set.add( new DataPoint(x[i], rand.nextFloat()));
            }
            final DataPoint[] pts = set.toArray( new DataPoint[]{} );
            return new Gson().toJson(pts, DataPoint[].class );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject( this );
    }

    @Override
    protected void onResume() {
        super.onResume();

        final WebSettings ws = webview.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setAllowFileAccess(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowContentAccess(true);
        ws.setAllowFileAccessFromFileURLs(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        webview.addJavascriptInterface( new WebAppInterface( this ), "Android");
        webview.loadUrl("file:///android_asset/main.html");
    }
}
