package com.adelphes.capella;

import android.app.*;
import android.content.Context;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.webkit.*;
import java.io.*;

public class MainActivity extends Activity {

    WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);

        // @JavascriptInterface only appears to support proxying of:
        // - integers (floats/doubles get passed as NaN)
        // - strings
        // - booleans
        // - arrays
        // - null (object instances are not proxied)
        mWebView.addJavascriptInterface(new Object() {
            /** Show a toast from the web page */
            @JavascriptInterface
            public String showToast(String toast) {
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
                return "done";
            }
            @JavascriptInterface
            public Object proxyGet(Object target, String prop, Object receive ) {
                return null;
            }
        }, "Android");

        String data = "";
        
        try {
            InputStream stream = getAssets().open("main.js");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            data = new String(buffer);
        } catch (IOException e) {
            Toast.makeText(this, "main.js load failed: " + e.getMessage(), Toast.LENGTH_SHORT);
        }

        String unencodedHtml =
             "<html style=\"background:#333;color:#fff;\"><body><button id=\"btn\" onClick=\"showAndroidToast('Hello Android 2!')\">Hello</button><script type=\"text/javascript\">"
             + data
             + "</script></body></html>";
        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
        mWebView.loadData(encodedHtml, "text/html", "base64");

        setContentView(mWebView);
    }
}
