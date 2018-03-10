package com.asif.linkkin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.linkkin.adapter.SpinAdapter;
import com.asif.linkkin.helper.ConnectionHelper;
import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.helper.JsonParser;
import com.asif.linkkin.utils.MyUtils;
import com.asif.linkkin.utils.SharedDataSaveLoad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by kanchan on 11/26/2015.
 */
public class InteractPostActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imgBack;
    //ProgressBar progress;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SpinAdapter adapterCategory;
    SpinAdapter adapterLocation;
    Button btnSubmit;
    String category;
    //String company;
    static String date = "";
    String description;
  //  EditText edtCompany;
    EditText edtDescription;
    EditText edtTitle;
    String employeeId;
    private String encodedString;
    private String fileName = null;
    private String imagePath = "";
    String location;
    ArrayList<String> locations = new ArrayList();
    ArrayList<String> locIds = new ArrayList();
    ArrayList<String> locIcons = new ArrayList();
    ArrayList<String> categories = new ArrayList();
    ArrayList<String> catIds = new ArrayList();
    ArrayList<String> catIcons = new ArrayList();
    MenuItem mItem;
    private String mParam1;
    private String mParam2;
    static String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    Spinner spinnerCategory;
    Spinner spinnerLocation;
    String title;
    static TextView txtDate;
    TextView txtPhoto;
    private static int RESULT_LOAD_IMG = 1;

    SweetAlertDialog mDialog;
    String postId="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_post);
        DisconnectClass disconnectClass=new DisconnectClass(this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
     //   progress=(ProgressBar)findViewById(R.id.progress);
     //   progress.setVisibility(View.GONE);

        spinnerCategory = ((Spinner)findViewById(R.id.spinnerCategory));
        spinnerLocation = ((Spinner)findViewById(R.id.spinnerLocation));
        btnSubmit = ((Button)findViewById(R.id.btnSubmit));
        txtDate = ((TextView)findViewById(R.id.txtDate));
        txtPhoto = ((TextView)findViewById(R.id.txtPhoto));
        edtTitle = ((EditText)findViewById(R.id.edtTitle));
       // this.edtCompany = ((EditText)findViewById(R.id.edtCompany));
        edtDescription = ((EditText)findViewById(R.id.edtDescription));
        ((LinearLayout)findViewById(R.id.linearDate)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.linearPhoto)).setOnClickListener(this);
        ((Button)findViewById(R.id.btnSubmit)).setOnClickListener(this);

/*
        this.employeeId = getIntent().getExtras().getString("employee_id");
        this.categories = ((ArrayList)getIntent().getSerializableExtra("categories"));
        this.locations = ((ArrayList)getIntent().getSerializableExtra("locations"));   */

        employeeId = SharedDataSaveLoad.load(getApplicationContext(), getResources().getString(R.string.shared_pref_employee_id));

        new getKinteractCats().execute();


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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == this.RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = this.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);
                txtPhoto.setText(imagePath);
                cursor.close();
                // Get the Image's file name
                String fileNameSegments[] = imagePath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];

            } else {
                //Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(InteractPostActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

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




    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }



    private void setAdapter()
    {
        if (!(categories.get(0)).equals("Select Category")) {
            this.categories.add(0, "Select Category");
            catIds.add(0,"-1");
            catIcons.add(0,"");
        }
        if (!(locations.get(0)).equals("Select Location")) {
            this.locations.add(0, "Select Location");
            locIds.add(0,"-1");
            locIcons.add(0,"");
        }

        this.adapterCategory = new SpinAdapter(this, this.categories);
        this.adapterLocation = new SpinAdapter(this, this.locations);
        this.spinnerCategory.setAdapter(this.adapterCategory);
        this.spinnerLocation.setAdapter(this.adapterLocation);
    }



    @Override
    public void onClick(View paramView) {

        if (paramView.getId() == R.id.linearDate)
        {
         //   Calendar cal = Calendar.getInstance();
         //   DatePickerDialog.newInstance(this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show(getSupportFragmentManager(), "Datepickerdialog");
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        else if (paramView.getId() == R.id.linearPhoto)
        {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }
        else if(paramView.getId() == R.id.btnSubmit)
        {
            if (this.spinnerCategory.getSelectedItemPosition() == 0)
            {
                Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            }
            else if (this.spinnerLocation.getSelectedItemPosition() == 0)
            {
                Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_SHORT).show();
            }
            else if (this.date.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            }
            else
            {
               // this.category = this.spinnerCategory.getSelectedItem().toString();
               // this.location = this.spinnerLocation.getSelectedItem().toString();
                int catPos=spinnerCategory.getSelectedItemPosition();
                int locPos=spinnerLocation.getSelectedItemPosition();
                category=catIds.get(catPos);
                location=locIds.get(locPos);
                this.title = "";
              //  this.company = "";
                this.description = "";
                this.title = this.edtTitle.getText().toString();
              //  this.company = this.edtCompany.getText().toString();
                this.description = this.edtDescription.getText().toString();
                if ((this.title.equals("")) || (this.description.equals("")))
                {
                    Toast.makeText(getApplicationContext(), "Please fill up all options", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new SubmitPost().execute();
                }
            }
        }

    }




    private void getLocations()
    {
        new getKinteractLocs().execute();
    }

    /*
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.date = (this.months[monthOfYear] + " " + dayOfMonth + ", " + year);
        this.txtDate.setText(this.date);
    }   */




    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            date = (months[month] + " " + day + ", " + year);
            txtDate.setText(date);
        }
    }




    private class SubmitPost extends AsyncTask<String, Void, String>
    {

        protected String doInBackground(String... paramVarArgs)
        {
            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(SharedDataSaveLoad.load(InteractPostActivity.this, getResources().getString(R.string.shared_pref_api_url)), "POST");
            helper.addData(new Uri.Builder()
                    .appendQueryParameter("action", "kinteract_submit")
                    .appendQueryParameter("user_id", employeeId)
                    .appendQueryParameter("cat_id", category)
                    .appendQueryParameter("location_id", location)
                    .appendQueryParameter("occurred_on", date)
                    .appendQueryParameter("title", title)
                    .appendQueryParameter("description", description));
            return helper.getResponse();

        }

        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            //mDialog.dismissWithTime(500);
            Log.e("post_submit_response: ", response);
           // Toast.makeText(getApplicationContext(), "Category: " + category + "\nLocation: " + location, Toast.LENGTH_SHORT).show();
            try
            {
                mDialog.dismiss();
                JSONObject paramString = new JSONObject(response);
                int success = paramString.getInt("success");
                if(success==1)
                {
                    postId=String.valueOf(paramString.getInt("post_id"));
                    MyUtils.postId=postId;
                    MyUtils.imagePath=imagePath;
                    MyUtils.fileName=fileName;
                    MyUtils.isCallable=true;
                    Toast.makeText(getApplicationContext(), "Submission successful", Toast.LENGTH_SHORT).show();
                    finish();
               /*     if(imagePath.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Submission successful", Toast.LENGTH_SHORT).show();
                        MyUtils.isCallable=true;
                        finish();
                    }
                    else
                    {
                        new UploadImageTask().execute();
                    }  */
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Post Submission failed", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            mDialog = new SweetAlertDialog(InteractPostActivity.this,SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
            mDialog.getProgressHelper().setBarColor(InteractPostActivity.this.getResources().getColor(R.color.primary_color_dark));
            mDialog.setTitleText("");
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }




    private class getKinteractCats
            extends AsyncTask<String, Void, String>
    {

        protected String doInBackground(String... paramVarArgs)
        {
           // return new ConnectionHelper().getDataFromUrlByGET(Urls.GET_INTERACT_CATEGORY_LOCATION);
            try {
                ConnectionHelper helper = new ConnectionHelper();
                helper.createConnection(SharedDataSaveLoad.load(InteractPostActivity.this, getResources().getString(R.string.shared_pref_api_url)), "POST");
                helper.addData(new Uri.Builder()
                        .appendQueryParameter("action", "get_kinteract_cat"));
                return helper.getResponse();
            }
            catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
           // this.mDialog.dismiss();
            try
            {
                if(response==null) Toast.makeText(getApplicationContext(),"Data retreiving failed",Toast.LENGTH_SHORT).show();
                else
                {
                    JSONObject paramString = new JSONObject(response);
                    int noOptions=paramString.getInt("no_of_options");
                    JSONArray localObject = paramString.getJSONArray("kinteract_options");
                    int i = 0;
                    while (i < (localObject).length())
                    {
                        JSONObject localJSONObject = (localObject).getJSONObject(i);
                        categories.add(localJSONObject.getString("title"));
                        catIds.add(localJSONObject.getString("id"));
                        catIcons.add(localJSONObject.getString("icon"));
                        i += 1;
                    }

                    getLocations();
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(getApplicationContext(), "Data retreiving failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            categories = new ArrayList();
            catIcons=new ArrayList<>();
            catIds=new ArrayList<>();
            mDialog = new SweetAlertDialog(InteractPostActivity.this, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
            mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.primary_color_dark));
            mDialog.setTitleText("");
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }




    private class getKinteractLocs extends AsyncTask<String, Void, String>
    {
        //SweetAlertDialog mDialog;


        protected String doInBackground(String... paramVarArgs)
        {
            // return new ConnectionHelper().getDataFromUrlByGET(Urls.GET_INTERACT_CATEGORY_LOCATION);
            try
            {
                ConnectionHelper helper = new ConnectionHelper();
                helper.createConnection(SharedDataSaveLoad.load(InteractPostActivity.this, getResources().getString(R.string.shared_pref_api_url)), "POST");
                helper.addData(new Uri.Builder()
                        .appendQueryParameter("action", "get_kinteract_location"));
                return helper.getResponse();
            }
            catch (Exception e)
            {
                return null;
            }
        }

        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            mDialog.dismiss();
            try
            {
                if(response==null)
                {
                    Toast.makeText(getApplicationContext(),"Data retreiving failed",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONObject paramString = new JSONObject(response);
                    int noOptions=paramString.getInt("no_of_options");
                    JSONArray localObject = paramString.getJSONArray("kinteract_locations");
                    int i = 0;
                    while (i < (localObject).length())
                    {
                        JSONObject localJSONObject = (localObject).getJSONObject(i);
                        locations.add(localJSONObject.getString("title"));
                        locIds.add(localJSONObject.getString("id"));
                        locIcons.add(localJSONObject.getString("icon"));
                        i += 1;
                    }

                    setAdapter();
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(getApplicationContext(), "Data retreiving failed", Toast.LENGTH_SHORT).show();
                //e.printStackTrace();
            }
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
          //  employeeId = SharedDataSaveLoad.load(getApplicationContext(), getResources().getString(R.string.shared_pref_employee_id));
            locations = new ArrayList<>();
            locIds=new ArrayList<>();
            locIcons=new ArrayList<>();
        }
    }





    private class UploadImageTask extends AsyncTask<Void, Integer, String>
    {


        final StringBuilder response = new StringBuilder();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progress.setVisibility(View.VISIBLE);
          //  progress.setProgress(0);
        }

        @Override
        protected String doInBackground(Void... params) {

            JsonParser jsonParser = new JsonParser();
            jsonParser.establishConnection("http://linkkin.co/sqgc/ajax-request/", "POST");
            try {
                jsonParser.writeParamData("action", "kinteract_image_upload");
                jsonParser.writeParamData("user_id", employeeId);
                jsonParser.writeParamData("post_id", postId);

            /*    jsonParser.addFilePart("1", "1", "/storage/sdcard0/Pictures/MCredit/20151129_180848.jpg", "");
                jsonParser.addFilePart("2", "2", "/storage/sdcard0/Pictures/MCredit/20151129_180638.jpg", "");
                jsonParser.addFilePart("3", "3", "/storage/sdcard0/Pictures/MCredit/20151129_180646.jpg", "");   */

                jsonParser.addFilePart("1", fileName, imagePath, "");

                response.append(jsonParser.getResponse());
                jsonParser.finishConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress();
            return response.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
          //  progress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String msg) {
            // Put converted Image string into Async Http Post param
            Log.i("response_upload", msg);
           // Toast.makeText(InteractPostActivity.this,""+msg,Toast.LENGTH_SHORT).show();
            /*try {
                JSONObject jObj = new JSONObject(msg);
                List<String> listErrorImages = null;

                if (jObj.getInt("error") == 0) {
                    listErrorImages = new ArrayList<>();


                }
            } catch (Exception e){
                e.printStackTrace();
            }*/
          //  progress.setVisibility(View.GONE);
            if(mDialog!=null&mDialog.isShowing()) mDialog.dismiss();
            try
            {
                JSONObject jObj=new JSONObject(msg);
                int success=jObj.getInt("success");
                if(success==1)
                {
                    Toast.makeText(getApplicationContext(), "Submission successful", Toast.LENGTH_SHORT).show();
                    MyUtils.isCallable=true;
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Image uploading failed", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Image uploading failed", Toast.LENGTH_SHORT).show();
            }
        }


    }







}
