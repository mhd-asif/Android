package com.bscheme.linkkin;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bscheme.linkkin.adapter.SymbolAdapter;
import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.helper.DisconnectClass;
import com.bscheme.linkkin.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by kanchan on 11/11/2015.
 */
public class SymbolActivity extends Activity {

    RecyclerView recycler;
    ImageView imgBack;

    SymbolAdapter adapter;

    ArrayList<Integer>iconResources=new ArrayList<>();

    ArrayList<String> details=new ArrayList<>();
    ArrayList<String>links=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symbol_layout);
        DisconnectClass disconnectClass=new DisconnectClass(this);
        imgBack=(ImageView)findViewById(R.id.imgBack);


      //  prepareListData();
        new GetSymbolTask().execute();


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }




    @Override
    protected void onResume() {
        super.onResume();
        DisconnectClass.resetDisconnectTimer();
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




    private void setAdapter()
    {
        recycler=(RecyclerView)findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        // DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST);
        //  recycler.addItemDecoration(divider);
        adapter=new SymbolAdapter(this,details,links);
        recycler.setAdapter(adapter);
    }


/*
    private void prepareListData()
    {
        for(int i=0;i<7;i++)
        {
            details.add("This is the text description of symbol "+(i+1)+". It will not be very large. It will be helpful to introduce the user with the symbol.");
            if(i==0) iconResources.add(R.drawable.university_48);
            else if(i==1) iconResources.add(R.drawable.school_48);
            else if(i==2) iconResources.add(R.drawable.barber_shop_48);
            else if(i==3) iconResources.add(R.drawable.laundry_48);
            else if(i==4) iconResources.add(R.drawable.restaurant_48);
            else if(i==5) iconResources.add(R.drawable.hospital_48);
            else if(i==6) iconResources.add(R.drawable.grocery_48);
        }
    }   */






    private class GetSymbolTask extends AsyncTask<String,String,String>
    {
        SweetAlertDialog mDialog;

        @Override
        protected String doInBackground(String... params) {
            return new ConnectionHelper().getDataFromUrlByGET(Urls.GET_SYMBOL);
        }

        @Override
        protected void onPreExecute() {
            details = new ArrayList();
            links = new ArrayList();
            mDialog = new SweetAlertDialog(SymbolActivity.this, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
            mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.primary_color_dark));
            mDialog.setTitleText("");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject json=new JSONObject(s);
                JSONArray jSymbols=json.getJSONArray("symbols");
                for(int i=0;i<jSymbols.length();i++)
                {
                    JSONObject jSymbol=jSymbols.getJSONObject(i);
                    details.add(jSymbol.getString("detail"));
                    links.add(jSymbol.getString("link"));
                }
                mDialog.dismiss();
                setAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(),"A problem occured",Toast.LENGTH_SHORT).show();
            }

        }
    }





}
