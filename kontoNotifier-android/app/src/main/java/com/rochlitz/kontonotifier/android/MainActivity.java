package com.rochlitz.kontonotifier.android;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends Activity {

    /** CONSTANTS **/

    private static final String TAG = MainActivity.class.getCanonicalName();

    /** MEMBERS **/

    private WebView mWebView;

    private ProgressBar mLoading;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(net.ddns.kontoagent.notifierkonto_android.R.layout.activity_main);


        CookieManager.setAcceptFileSchemeCookies(true); // available in android
        // level 12
        CookieManager.getInstance().setAcceptCookie(true); // available in
        // android level 12

        mWebView = (WebView) findViewById(net.ddns.kontoagent.notifierkonto_android.R.id.kontoNotifierView);
        mWebView.setWebViewClient(new SSLTolerentWebViewClient());//TODO remove this for public version

        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDomStorageEnabled(true);


        mLoading = (ProgressBar) findViewById(net.ddns.kontoagent.notifierkonto_android.R.id.pbLoading);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();

        StrictMode.setThreadPolicy(policy);

/*
        try {

            KeyStore selfsignedKeys = KeyStore.getInstance("BKS");
            Context context = getApplicationContext();
            selfsignedKeys.load(
                    context.getResources().openRawResource(
                            net.ddns.kontoagent.notifierkonto_android.R.raw.selfsignedcertsbks),
                    "A7uj89oil".toCharArray());

            TrustManagerFactory trustMgr = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustMgr.init(selfsignedKeys);

            SSLContext selfsignedSSLcontext = SSLContext.getInstance("TLS");
            selfsignedSSLcontext.init(null, trustMgr.getTrustManagers(),
                    new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(selfsignedSSLcontext
                    .getSocketFactory());

        } catch (NoSuchAlgorithmException | CertificateException
                | NotFoundException | IOException | KeyStoreException
                | KeyManagementException e) {
            // TODO split exhaption handdling ?
            e.printStackTrace();
        }
*/
        // Test
//		testGetHtml();

        mWebView.loadUrl("https://kontoagent.ddns.net/notifier/index.html");
        mWebView.loadUrl("javascript:someVar=true");
        mWebView.getSettings().setDomStorageEnabled(true);

    }

    private void testGetHtml() {
        // test
        URL serverURL;
        try {
            serverURL = new URL(
                    "https://kontoagent.ddns.net/notifier/index.html");
            HttpsURLConnection serverConn = (HttpsURLConnection) serverURL
                    .openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    serverConn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(net.ddns.kontoagent.notifierkonto_android.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == net.ddns.kontoagent.notifierkonto_android.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // SSL Error Tolerant Web View Client
    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

    }



}