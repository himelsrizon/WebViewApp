package com.himel.webview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


    WebView webView;
    ProgressBar loadingbar;
    String weburl="https://youtube.com/";
    LinearLayout nointernet;
    Button tryagain;
    SwipeRefreshLayout swipeRefreshLayout;
    ScrollView mscrollview;
    AlertDialog.Builder ExitDialog;




   // private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






       // FirebaseMessaging.getInstance().subscribeToTopic("notification");
       // auth=FirebaseAuth.getInstance();

        //++++++++++++++++++++ on back pressed++++++++++++++++++++++++++++++++++++++++++++++++++++++++++






        webView=findViewById(R.id.webView);
        loadingbar=findViewById(R.id.loadingbar);
        nointernet=findViewById(R.id.nointernet);
        tryagain=findViewById(R.id.tryagain);
        swipeRefreshLayout=findViewById(R.id.swiferlayout);
        mscrollview=findViewById(R.id.mscrollview);

        loadingbar.setMax(100);

/*****= Reload code**/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        webView.loadUrl(weburl);
                    }
                },  3000);
            }
        });


        webView.setWebViewClient(new WebViewClient(){


            @Override
            //Keep webview in app when clicking links
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            /**all page no internet image**/
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                checkConnection();
                super.onReceivedError(view, request, error);
            }
        });



        // Enabling some setting so that browser can work properly
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        checkConnection();





        /**** Loading bar code**/

        loadingbar.setProgress(0);
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                loadingbar.setProgress(newProgress);
                if (newProgress==100)
                    loadingbar.setVisibility(View.INVISIBLE);
                else
                    loadingbar.setVisibility(View.VISIBLE);
                super.onProgressChanged(view, newProgress);
            }
        });



        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
            }
        });






    }

    @Override
    public void onBackPressed() {
        exit();
    }




    /**==== No internet Control****/

    public void checkConnection(){

        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if(wifi.isConnected()){
            webView.loadUrl(weburl);
            webView.setVisibility(View.VISIBLE);
            nointernet.setVisibility(View.GONE);


        }
        else if (mobileNetwork.isConnected()){
            webView.loadUrl(weburl);
            webView.setVisibility(View.VISIBLE);
            nointernet.setVisibility(View.GONE);
        }
        else{

            webView.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);

        }

    }








    public  void  exit()
    {


        ExitDialog =new AlertDialog.Builder(MainActivity.this);
        ExitDialog.setMessage("Do you Want to Exit");
        ExitDialog.setTitle("Youtube");
        ExitDialog.setCancelable(false);

        ExitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });


        ExitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog =ExitDialog.create();
        alertDialog.show();


    }

}