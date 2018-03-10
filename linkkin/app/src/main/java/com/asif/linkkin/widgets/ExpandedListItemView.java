package com.asif.linkkin.widgets;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.linkkin.helper.ConnectionHelper;
import com.asif.linkkin.R;
import com.asif.linkkin.models.KindomInfo;
import com.asif.linkkin.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpandedListItemView extends RelativeLayout {

    private TextView mText;
    private WebView wb_about_us;
    SweetAlertDialog mDialog;
    Context mContext;

    public ExpandedListItemView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ExpandedListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandedListItemView(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ExpandedListItemView(Context activity, KindomInfo info) {
        super(activity);
        init();
        setText(info.description);
    }

    private void init() {
        inflate(getContext(), R.layout.expandable_list_item_view, this);

        mText = (TextView) findViewById(R.id.expandable_list_item_view_text);
//        wb_about_us = (WebView) findViewById(R.id.webview_about_us);

//        new getRes().execute();
    }

    public void setWbeview(String text) {
        this.mText.setText(text);

    }
    public void setText(String text) {
        this.mText.setText(text);

    }

    private class getRes extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectionHelper ch = new ConnectionHelper();
//            ch.createConnection(Urls.KINDIVIDUAL_LIST, "GET");

            return ch.getDataFromUrlByGET(Urls.KINDOM_LIST);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)) // check response is null or not

                try {
                JSONObject jObj = new JSONObject(s);
                if (jObj.getString("status").contains("success")) {

                    final List<KindomInfo> infoList = new ArrayList<KindomInfo>();


                    KindomInfo info = new KindomInfo();

                    info.id = jObj.getJSONObject("list").getString("id");
                    info.name = jObj.getJSONObject("list").getString("title");
                    info.description = jObj.getJSONObject("list").getString("short_desc");
                    info.business = jObj.getJSONObject("list").getString("business");
                    info.about_us = jObj.getJSONObject("list").getString("about_us");
                    info.industries = jObj.getJSONObject("list").getString("industries");
                    info.statistics = jObj.getJSONObject("list").getString("statistics");
                    info.subsidiaries = jObj.getJSONObject("list").getString("subsidiaries");

//                    infoList.add(info);

                    setText(info.about_us);

                }else {
                    Toast.makeText(mContext, jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
