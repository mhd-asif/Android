package com.asif.linkkin;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asif.linkkin.models.KinformationDetailsInfo;
import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.utils.Font;

import us.feras.mdv.MarkdownView;

/**
 * Created by kanchan on 11/29/2015.
 */
public class PolicyDetailsActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView txtTitle;
   // RelativeLayout relativeDesc;
    MarkdownView webView;
//    WebView web;
    ProgressBar progress;

    String desc,title;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_details);
        DisconnectClass disconnectClass=new DisconnectClass(this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        txtTitle=(TextView)findViewById(R.id.tool_bar_title);
        webView=(MarkdownView)findViewById(R.id.webView);
        progress=(ProgressBar)findViewById(R.id.progressBar);
        progress.setIndeterminate(true);
        progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary_color_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
     //   relativeDesc=(RelativeLayout)findViewById(R.id.relativeDesc);
//        web=(WebView)findViewById(R.id.web);

        Font.LATO_Regular.apply(PolicyDetailsActivity.this, txtTitle);
        webView.setBackgroundColor(Color.parseColor("#00000000"));


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler hh=new Handler();
                hh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisconnectClass.resetDisconnectTimer();
        webView.setVisibility(View.INVISIBLE);
        //progress.setVisibility(View.GONE);

    /*    mDialog = new SweetAlertDialog(PolicyDetailsActivity.this, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
        mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.primary_color_dark));
        mDialog.setTitleText("");
        mDialog.setCancelable(false);
        mDialog.show();  */


        Handler hh=new Handler();
        hh.postDelayed(new Runnable() {
            @Override
            public void run() {

                //title=getIntent().getExtras().getString("title");
                // desc=getIntent().getExtras().getString("desc");
                KinformationDetailsInfo info = getIntent().getParcelableExtra("info");
                title = info.title;
                desc = info.desc;

                txtTitle.setText(title);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setAppCacheEnabled(true);
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                //webView.loadDataWithBaseURL("", desc, "text/html", "UTF-8", "");
                webView.loadMarkdown(desc, "file:///android_asset/css/policy.css");

                webView.setBackgroundColor(Color.parseColor("#00000000"));

        /*        StringBuilder sb = new StringBuilder();
                sb.append("<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
                sb.append(desc.toString());
                sb.append("</body></HTML>");
                webView.loadDataWithBaseURL("file:///android_asset/css/", sb.toString(), "text/html","utf-8", null);   */

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        //mDialog.dismiss();
                        webView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    }

                });


            }
        }, 1000);

   /*     MarkdownView markdownView = new MarkdownView(PolicyDetailsActivity.this);
        markdownView.loadMarkdown(desc, "file:///android_asset/css/new.css");
        relativeDesc.addView(markdownView);   */

        /*web.getSettings().setJavaScriptEnabled(true);
        web.setBackgroundColor(Color.parseColor("#FAFAFA"));
        web.loadDataWithBaseURL("", desc, "text/html", "UTF-8", "");*/

    }



    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        DisconnectClass.resetDisconnectTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //   DisconnectClass.stopDisconnectTimer();
        DisconnectClass.removeActivity(this);
    }
}
