package com.asif.linkkin;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.linkkin.helper.ConnectionHelper;
import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.utils.Font;
import com.asif.linkkin.utils.SharedDataSaveLoad;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import us.feras.mdv.MarkdownView;

/**
 * Created by kanchan on 12/2/2015.
 */
public class PostDetailsActivity extends AppCompatActivity{

    ImageView imgBack,imgCommentHeader,imgComment;
    EditText edtComment;
    TextView txtTitle,txtCategory,txtDate,txtLocation,txtCommentHeader;
    LinearLayout linearScroll,linearComments,linearCommentPost;
    RelativeLayout relativeCommentHeader;
    HorizontalScrollView imgScroll;
    MarkdownView webView;

    String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    int count,width;

    String title,category,date,location,comment_count,desc,employeeId,employeename,postId,commentDesc;
    ArrayList<String> images=new ArrayList<>();

    ArrayList<String>commentors=new ArrayList<>();
    ArrayList<String>commentDates=new ArrayList<>();
    ArrayList<String>commentDescs=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        DisconnectClass disconnectClass=new DisconnectClass(this);
        webView=(MarkdownView)findViewById(R.id.webView);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgCommentHeader=(ImageView)findViewById(R.id.imgCommentHeader);
        imgComment=(ImageView)findViewById(R.id.imgComment);
        edtComment=(EditText)findViewById(R.id.edtComment);
        txtTitle=(TextView)findViewById(R.id.tool_bar_title);
        txtCategory=(TextView)findViewById(R.id.txtCategory);
        txtDate=(TextView)findViewById(R.id.txtDate);
        txtLocation=(TextView)findViewById(R.id.txtLocation);
        txtCommentHeader=(TextView)findViewById(R.id.txtCommentHeader);
        relativeCommentHeader=(RelativeLayout)findViewById(R.id.relativeCommentHeader);
        linearComments=(LinearLayout)findViewById(R.id.linearComments);
        linearCommentPost=(LinearLayout)findViewById(R.id.linearCommentPost);
        linearScroll=(LinearLayout)findViewById(R.id.linearScroll);
        imgScroll=(HorizontalScrollView)findViewById(R.id.imgScroll);

        webView.setBackgroundColor(Color.parseColor("#00000000"));

        Font.LATO_Regular.apply(PostDetailsActivity.this, txtTitle);
        Font.LATO_Regular.apply(PostDetailsActivity.this, txtCategory);
        Font.LATO_Regular.apply(PostDetailsActivity.this, txtDate);
        Font.LATO_Regular.apply(PostDetailsActivity.this, txtLocation);
        Font.LATO_Regular.apply(PostDetailsActivity.this, txtCommentHeader);

        employeeId=getIntent().getExtras().getString("employee_id");
        employeename=getIntent().getExtras().getString("employee_name");
        postId=getIntent().getExtras().getString("post_id");
        title=getIntent().getExtras().getString("title");
        category=getIntent().getExtras().getString("category");
        date=getIntent().getExtras().getString("date");
        location=getIntent().getExtras().getString("location");
        comment_count=getIntent().getExtras().getString("comment_count");
        desc=getIntent().getExtras().getString("desc");
        images=(ArrayList<String>)getIntent().getSerializableExtra("images");

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width= (int)((150 * displayMetrics.density) + 0.5);

        txtTitle.setText(title);
        txtCategory.setText(category);
        txtDate.setText(date);
        txtLocation.setText(location);
        count=Integer.valueOf(comment_count);
        if(count==0) txtCommentHeader.setText("Comment");
        else if(count==1) txtCommentHeader.setText("1 Comment");
        else txtCommentHeader.setText(count+" Comments");

        imgCommentHeader.setImageResource(R.drawable.comment_black);

        linearComments.setVisibility(View.GONE);
        linearCommentPost.setVisibility(View.GONE);

        if (images.size()<=0)  imgScroll.setVisibility(View.GONE);
        else showAllImages();

        webView.getSettings().setJavaScriptEnabled(true);
       // webView.loadDataWithBaseURL("", desc, "text/html", "UTF-8", "");
        webView.loadMarkdown(desc, "file:///android_asset/css/new.css");

        webView.setBackgroundColor(Color.parseColor("#00000000"));


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler hh = new Handler();
                hh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 100);
            }
        });



        relativeCommentHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearCommentPost.isShown())
                {
                    linearComments.setVisibility(View.GONE);
                    linearCommentPost.setVisibility(View.GONE);
                    imgCommentHeader.setImageResource(R.drawable.comment_black);
                }
                else
                {
                    if(count<=0)
                    {
                        linearCommentPost.setVisibility(View.VISIBLE);
                    }
                    else new getAllComments().execute();
                }
            }
        });


        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDesc="";
                commentDesc=edtComment.getText().toString();
                if(commentDesc.equals("")) Toast.makeText(getApplicationContext(),"Please type a comment",Toast.LENGTH_SHORT).show();
                else new SubmitComment().execute();
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



    private void showAllImages()
    {
        linearScroll.removeAllViewsInLayout();
        LayoutInflater mInflater=LayoutInflater.from(this);
        View[] mViews=new View[images.size()];
        ImageView[] mImages=new ImageView[images.size()];
        TextView[] mSeparators=new TextView[images.size()];
        ProgressBar[] mProgresses=new ProgressBar[images.size()];
        FrameLayout[]  mFrames=new FrameLayout[images.size()];
        for(int k=0;k<images.size();k++)
        {
            mViews[k]=mInflater.inflate(R.layout.kinteract_image_layout,null);
            mImages[k]=(ImageView)mViews[k].findViewById(R.id.imgPost);
            mSeparators[k]=(TextView)mViews[k].findViewById(R.id.txtSeparator);
            mFrames[k]=(FrameLayout)mViews[k].findViewById(R.id.framePost);
            mProgresses[k]=(ProgressBar)mViews[k].findViewById(R.id.progressMain);
            mFrames[k].getLayoutParams().height=width;
            mFrames[k].getLayoutParams().width=width;
            if(k==0) mSeparators[k].setVisibility(View.GONE);
            mProgresses[k].setIndeterminate(true);
            mProgresses[k].getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary_color_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            Picasso.with(this).load(images.get(k)).fit().into(mImages[k]);
            linearScroll.addView(mViews[k]);
        }
        imgScroll.setVisibility(View.VISIBLE);
    }



    private void makeVisible()
    {
        linearComments.removeAllViewsInLayout();
        int j=commentors.size();
        View[] views = new View[j];
        TextView[] txtCommentor = new TextView[j];
        TextView[] txtCommentDate = new TextView[j];
        TextView[] txtCommentDesc = new TextView[j];
        CircleImageView[] imgCommentor = new CircleImageView[j];
        LayoutInflater inflater=LayoutInflater.from(PostDetailsActivity.this);
        int i = 0;
        while (i < j)
        {
            views[i] = inflater.inflate(R.layout.comment_layout, null);
            txtCommentDesc[i] = ((TextView)views[i].findViewById(R.id.txtCommentDesc));
            txtCommentor[i] = ((TextView)views[i].findViewById(R.id.txtCommentTitle));
            txtCommentDate[i] = ((TextView)views[i].findViewById(R.id.txtCommentDate));
            imgCommentor[i] = ((CircleImageView)views[i].findViewById(R.id.imgComment));
            txtCommentDesc[i].setText((CharSequence)commentDescs.get(i));
            txtCommentor[i].setText((CharSequence)commentors.get(i));
            txtCommentDate[i].setText(commentDates.get(i));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try
            {
                Date comDate=format.parse(commentDates.get(i));
                Calendar calCom = Calendar.getInstance();
                calCom.setTime(comDate);
                int comYear=calCom.get(Calendar.YEAR);
                int comMonth=calCom.get(Calendar.MONTH);
                int comDay=calCom.get(Calendar.DAY_OF_MONTH);
                String comSDate=months[comMonth]+" "+comDay+", "+comYear;
                txtCommentDate[i].setText(comSDate);
            }
            catch (Exception e) {}

            if(commentors.get(i).toLowerCase().equals("admin")) imgCommentor[i].setImageResource(R.drawable.ic_linkin_launcher);
            else imgCommentor[i].setImageResource(R.drawable.ic_action_bar_linkin);
            linearComments.addView(views[i]);
            i=i+1;
        }
        linearComments.setVisibility(View.VISIBLE);
        linearCommentPost.setVisibility(View.VISIBLE);
        imgCommentHeader.setImageResource(R.drawable.comment_color);
    }




    private class getAllComments extends AsyncTask<String, Void, String>
    {
        SweetAlertDialog mDialog;

        protected String doInBackground(String... paramVarArgs)
        {
            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(SharedDataSaveLoad.load(PostDetailsActivity.this,getResources().getString(R.string.shared_pref_api_url)), "POST");
            helper.addData(new Uri.Builder()
                    .appendQueryParameter("action", "kinteract_comment_list")
                    .appendQueryParameter("post_id", postId));
            return helper.getResponse();
        }

        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            this.mDialog.dismissWithTime(500);
            Log.e("comment_get_response: ", response);
            try
            {
                JSONObject json=new JSONObject(response);
                int success=json.getInt("success");
                if(success==1)
                {
                    JSONArray jComments=json.getJSONArray("comments");
                    for(int i=0;i<jComments.length();i++)
                    {
                        JSONObject jObj=jComments.getJSONObject(i);
                        commentors.add(jObj.getString("comment_author"));
                        commentDates.add(jObj.getString("comment_date"));
                        commentDescs.add(jObj.getString("comment_content"));
                    }
                }
                makeVisible();
            }
            catch (Exception e)
            {
                makeVisible();
                Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
            }
        }

        protected void onPreExecute()
        {
            super.onPreExecute();

            commentors=new ArrayList<>();
            commentDates=new ArrayList<>();
            commentDescs=new ArrayList<>();

            this.mDialog = new SweetAlertDialog(PostDetailsActivity.this, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
            this.mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.primary_color_dark));
            this.mDialog.setTitleText("");
            this.mDialog.setCancelable(false);
            this.mDialog.show();
        }
    }




    private class SubmitComment extends AsyncTask<String, Void, String>
    {
        SweetAlertDialog mDialog;

        private SubmitComment() {}

        protected String doInBackground(String... paramVarArgs)
        {
            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(SharedDataSaveLoad.load(PostDetailsActivity.this, getResources().getString(R.string.shared_pref_api_url)), "POST");
            helper.addData(new Uri.Builder()
                    .appendQueryParameter("action", "kinteract_comment_submit")
                    .appendQueryParameter("user_id", employeeId)
                    .appendQueryParameter("post_id", postId)
                            // .appendQueryParameter("date", InteractAdapter.this.commentDate)
                            //   .appendQueryParameter("commentor", InteractAdapter.this.employeeName)
                    .appendQueryParameter("description", commentDesc));
            return helper.getResponse();
        }

        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            this.mDialog.dismissWithTime(500);
            Log.e("comment_sbm_response: ", response);

            try
            {
                JSONObject json=new JSONObject(response);
                int success=json.getInt("success");
                if(success==1)
                {
                    JSONArray jComments=json.getJSONArray("all_comments");
                    for(int i=0;i<jComments.length();i++)
                    {
                        JSONObject jObj=jComments.getJSONObject(i);
                        commentors.add(jObj.getString("comment_author"));
                        commentDates.add(jObj.getString("comment_date"));
                        commentDescs.add(jObj.getString("comment_content"));
                    }
                }
                else Toast.makeText(getApplicationContext(), "Comment Submission failed", Toast.LENGTH_SHORT).show();

                count=commentors.size();
                if(count==0) txtCommentHeader.setText("Comment");
                else if(count==1) txtCommentHeader.setText("1 Comment");
                else txtCommentHeader.setText(count+" Comments");
                edtComment.setText("");
                makeVisible();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Comment Submission failed", Toast.LENGTH_SHORT).show();
                count=commentors.size();
                if(count==0) txtCommentHeader.setText("Comment");
                else if(count==1) txtCommentHeader.setText("1 Comment");
                else txtCommentHeader.setText(count+" Comments");
                edtComment.setText("");
                makeVisible();
            }

        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            this.mDialog = new SweetAlertDialog(PostDetailsActivity.this, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
            this.mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.primary_color_dark));
            this.mDialog.setTitleText("");
            this.mDialog.setCancelable(false);
            this.mDialog.show();
        }
    }




}
