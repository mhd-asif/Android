package com.asif.linkkin.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.linkkin.helper.ConnectionHelper;
import com.asif.linkkin.PostDetailsActivity;
import com.asif.linkkin.R;
import com.asif.linkkin.utils.CheckConnectivity;
import com.asif.linkkin.utils.SharedDataSaveLoad;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS on 10/18/2015.
 */
public class InteractAdapter extends RecyclerView.Adapter<InteractAdapter.InteractViewHolder>{

  String commentDate;
  String commentDesc;
  String commentPostId;
  Activity context;
  ArrayList<String> titles = new ArrayList();
  ArrayList<String> dates = new ArrayList();
  ArrayList<String> shortdDescs = new ArrayList();
  ArrayList<String> descriptions = new ArrayList();
  ArrayList<String> ids = new ArrayList();
  ArrayList<String> noComments = new ArrayList();
  ArrayList<String> categories = new ArrayList();
  ArrayList<String> locations = new ArrayList();
  String employeeId;
  String employeeName;
  String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
  LayoutInflater inflater;
  LinearLayout linearCommentsTemp,linearCommentsPostTemp;
  TextView txtCommentHeaderTemp;

  ArrayList<String>commentors=new ArrayList<>();
  ArrayList<String>commentDates=new ArrayList<>();
  ArrayList<String>commentDescs=new ArrayList<>();

  HashMap<Integer,ArrayList<String>>allImages=new HashMap<>();


  public InteractAdapter(Activity paramActivity, String employeeId, String employeeName, ArrayList<String> ids, ArrayList<String> titles, ArrayList<String> dates,ArrayList<String> shortdDescs, ArrayList<String> descriptions, ArrayList<String> noComments, ArrayList<String> categories, ArrayList<String> locations, HashMap<Integer,ArrayList<String>>allImages)
  {
    this.context = paramActivity;
    this.inflater = paramActivity.getLayoutInflater();
    this.employeeId = employeeId;
    this.employeeName = employeeName;
    this.ids = ids;
    this.titles = titles;
    this.dates = dates;
    this.shortdDescs=shortdDescs;
    this.descriptions = descriptions;
    this.noComments=noComments;
    this.categories = categories;
    this.locations = locations;
    this.allImages=allImages;
  }



  @Override
  public InteractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater mInflater=LayoutInflater.from(context);
    return new InteractViewHolder(mInflater.inflate(R.layout.row_interact, parent, false));
  }

  @Override
  public void onBindViewHolder(InteractViewHolder holder, int position) {
    holder.txtTitle.setText(titles.get(position));
    holder.txtLocation.setText(locations.get(position));
    holder.txtDescription.setText(shortdDescs.get(position));
    holder.txtDate.setText(dates.get(position));
    holder.txtCategory.setText(categories.get(position));

    int i=Integer.valueOf(noComments.get(position));

  // final String  localObject1= ids.get(paramInt);
  //  int i = ((ArrayList)this.allCommentors.get(localObject1)).size();
    if (i == 0) {
      holder.txtCommentHeader.setText("Comment");
    }
    else if (i == 1) {
      holder.txtCommentHeader.setText("1 Comment");
    } else {
      holder.txtCommentHeader.setText(i + " Comments");
    }


    holder.imgCommentHeader.setImageResource(R.drawable.comment_black);
    holder.linearCommentPost.setVisibility(View.GONE);
    holder.linearComments.setVisibility(View.GONE);

    ArrayList<String>tempImages=allImages.get(position);
    if(tempImages.size()<=0)
    {
      holder.imgScroll.setVisibility(View.GONE);
    }
    else
    {
      holder.linearScroll.removeAllViewsInLayout();
      LayoutInflater mInflater=LayoutInflater.from(context);
      View[] mViews=new View[tempImages.size()];
      ImageView[] mImages=new ImageView[tempImages.size()];
      TextView[] mSeparators=new TextView[tempImages.size()];
      ProgressBar[] mProgresses=new ProgressBar[tempImages.size()];
      for(int k=0;k<tempImages.size();k++)
      {
        mViews[k]=mInflater.inflate(R.layout.kinteract_image_layout,null);
        mImages[k]=(ImageView)mViews[k].findViewById(R.id.imgPost);
        mSeparators[k]=(TextView)mViews[k].findViewById(R.id.txtSeparator);
        mProgresses[k]=(ProgressBar)mViews[k].findViewById(R.id.progressMain);
        mProgresses[k].setIndeterminate(true);
        mProgresses[k].getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.primary_color_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
        if(k==0) mSeparators[k].setVisibility(View.GONE);
        Picasso.with(this.context).load(tempImages.get(k)).fit().into(mImages[k]);
        holder.linearScroll.addView(mViews[k]);
      }
      holder.imgScroll.setVisibility(View.VISIBLE);
    }


  }

  @Override
  public int getItemCount() {
    return this.ids.size();
  }

  public class InteractViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
  {
    public ImageView imgCommentHeader,imgComment;
    public TextView txtDate,txtTitle,txtLocation,txtDescription,txtCategory,txtCommentHeader;
    public EditText edtComment;
    public RelativeLayout linearCommentHeader;
    public LinearLayout linearComments,linearCommentPost,linearScroll;
    HorizontalScrollView imgScroll;

    public InteractViewHolder(View localView)
    {
       super(localView);
      txtDate = (TextView)localView.findViewById(R.id.txtDate);
      txtTitle = (TextView)localView.findViewById(R.id.txtTitle);
      txtLocation = (TextView)localView.findViewById(R.id.txtLocation);
      txtDescription = (TextView)localView.findViewById(R.id.txtDescription);
      txtCategory = (TextView)localView.findViewById(R.id.txtCategory);
      txtCommentHeader = (TextView)localView.findViewById(R.id.txtCommentHeader);
       edtComment = (EditText)localView.findViewById(R.id.edtComment);
      imgComment = (ImageView)localView.findViewById(R.id.imgComment);
      imgCommentHeader = (ImageView)localView.findViewById(R.id.imgCommentHeader);
      linearCommentHeader = (RelativeLayout)localView.findViewById(R.id.linearCommentHeader);
      linearComments = (LinearLayout)localView.findViewById(R.id.linearComments);
      linearCommentPost = (LinearLayout)localView.findViewById(R.id.linearCommentPost);
       imgScroll=(HorizontalScrollView)localView.findViewById(R.id.imgScroll);
       linearScroll=(LinearLayout)localView.findViewById(R.id.linearScroll);
      imgComment.setOnClickListener(this);
      linearCommentHeader.setOnClickListener(this);
       linearScroll.setOnClickListener(this);
       localView.setOnClickListener(this);
    }

    public void onClick(View view)
    {
      int pos=getAdapterPosition();

      if(view.getId()==R.id.imgComment)
      {
        commentDesc = "";
        commentDesc = edtComment.getText().toString();
        CheckConnectivity connectivity=new CheckConnectivity(context);
        if(connectivity.isConnected())
        {
          if (commentDesc.equals("")) {
            Toast.makeText(context, "Please type a comment", Toast.LENGTH_SHORT).show();
          }
          else
          {
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            int i = cal.get(Calendar.DAY_OF_MONTH);
            int j = cal.get(Calendar.MONTH);
            int k = cal.get(Calendar.YEAR);
            int curHour=cal.get(Calendar.HOUR_OF_DAY);
            int curMinute=cal.get(Calendar.MINUTE);
            int curSecond=cal.get(Calendar.SECOND);
            // commentDate = (months[j] + " " + i + ", " + k);
            commentDate=formatedText(k)+"-"+formatedText(j+1)+"-"+formatedText(i)+" "+formatedText(curHour)+":"+formatedText(curMinute)+":"+formatedText(curSecond);
            commentPostId = ((String) ids.get(pos));
            edtComment.setText("");
            txtCommentHeaderTemp = txtCommentHeader;
            linearCommentsTemp = linearComments;
            linearCommentsPostTemp=linearCommentPost;
            new SubmitComment().execute();
          }
        }
        else Toast.makeText(context,"Internet connection required",Toast.LENGTH_SHORT).show();
      }


      else if(view.getId()==R.id.linearCommentHeader)
      {

        if (!linearCommentPost.isShown())
        {
          imgCommentHeader.setImageResource(R.drawable.comment_color);
          int count=Integer.valueOf(noComments.get(pos));
          if(count<=0)
          {
            linearCommentPost.setVisibility(View.VISIBLE);
          }
          else
          {
            txtCommentHeaderTemp = txtCommentHeader;
            linearCommentsTemp = linearComments;
            linearCommentsPostTemp=linearCommentPost;
            commentPostId=ids.get(pos);

            CheckConnectivity connectivity=new CheckConnectivity(context);
            if(connectivity.isConnected()) new getAllComments().execute();
            else
            {
              String path_kinteract=context.getCacheDir().getAbsolutePath()+"/"+context.getResources().getString(R.string.file_kinteract)+"_"+commentPostId;
              File file = new File(path_kinteract);
              if(file.exists()) getOfflineRes();
              else Toast.makeText(context,"Internet connection required",Toast.LENGTH_SHORT).show();
            }

          }
        }
        else
        {
          imgCommentHeader.setImageResource(R.drawable.comment_black);
          linearComments.setVisibility(View.GONE);
          linearCommentPost.setVisibility(View.GONE);
        }

      }

      else
      {
        Intent in=new Intent(context, PostDetailsActivity.class);
        in.putExtra("employee_id",employeeId);
        in.putExtra("employee_name",employeeName);
        in.putExtra("post_id",ids.get(pos));
        in.putExtra("title",titles.get(pos));
        in.putExtra("category",categories.get(pos));
        in.putExtra("date",dates.get(pos));
        in.putExtra("location",locations.get(pos));
        in.putExtra("comment_count",noComments.get(pos));
        in.putExtra("desc",descriptions.get(pos));
        in.putExtra("images",allImages.get(pos));
        context.startActivity(in);
      }


    }

  }






  private String formatedText(int s)
  {
    if(s<10) return "0"+s;
    else return ""+s;
  }



  private void makeVisible()
  {
    int j = commentors.size();
    if (j == 0) {
      this.txtCommentHeaderTemp.setText("Comment");
    }
    else if (j == 1) {
      this.txtCommentHeaderTemp.setText("1 Comment");
    } else {
      this.txtCommentHeaderTemp.setText(j + " Comments");
    }

    this.linearCommentsTemp.removeAllViewsInLayout();
    View[] commViews = new View[j];
    TextView[] txtCommTitles = new TextView[j];
    TextView[] txtCommDates = new TextView[j];
    TextView[] txtCommDescs = new TextView[j];
    CircleImageView[] imgComms = new CircleImageView[j];
    int i = 0;
    while (i < j)
    {
      commViews[i] = this.inflater.inflate(R.layout.comment_layout, null);
      txtCommDescs[i] = ((TextView)commViews[i].findViewById(R.id.txtCommentDesc));
      txtCommTitles[i] = ((TextView)commViews[i].findViewById(R.id.txtCommentTitle));
      txtCommDates[i] = ((TextView)commViews[i].findViewById(R.id.txtCommentDate));
      imgComms[i] = ((CircleImageView)commViews[i].findViewById(R.id.imgComment));
      txtCommDescs[i].setText((CharSequence)commentDescs.get(i));
      txtCommTitles[i].setText((CharSequence)commentors.get(i));
     // arrayOfTextView2[i].setText((CharSequence) localArrayList2.get(i));
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
        txtCommDates[i].setText(comSDate);
      }
      catch (Exception e) {}

      if(commentors.get(i).toLowerCase().equals("admin")) imgComms[i].setImageResource(R.drawable.ic_linkin_launcher);
      else imgComms[i].setImageResource(R.drawable.ic_action_bar_linkin);
      this.linearCommentsTemp.addView(commViews[i]);
      i=i+1;
    }
    this.linearCommentsTemp.setVisibility(View.VISIBLE);
    linearCommentsPostTemp.setVisibility(View.VISIBLE);
  }




  private void getOfflineRes()
  {
    String path_kinteract_comm=context.getCacheDir().getAbsolutePath()+"/"+context.getResources().getString(R.string.file_kinteract)+"_"+commentPostId;
    File file = new File(path_kinteract_comm);
    if(file.exists())
    {
      int length = (int) file.length();
      byte[] bytes = new byte[length];
      FileInputStream in = null;
      try {
        in = new FileInputStream(file);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      try {
        in.read(bytes);
        String contents = new String(bytes);
        showAllData(contents);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }



  private void showAllData(String response)
  {
    commentors=new ArrayList<>();
    commentDates=new ArrayList<>();
    commentDescs=new ArrayList<>();
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
    catch (Exception e) {}
  }




  private class SubmitComment extends AsyncTask<String, Void, String>
  {
    SweetAlertDialog mDialog;

    private SubmitComment() {}

    protected String doInBackground(String... paramVarArgs)
    {
      ConnectionHelper helper = new ConnectionHelper();
      helper.createConnection(SharedDataSaveLoad.load(context, context.getResources().getString(R.string.shared_pref_api_url)), "POST");
      helper.addData(new Uri.Builder()
              .appendQueryParameter("action", "kinteract_comment_submit")
              .appendQueryParameter("user_id", InteractAdapter.this.employeeId)
              .appendQueryParameter("post_id", InteractAdapter.this.commentPostId)
                      // .appendQueryParameter("date", InteractAdapter.this.commentDate)
                      //   .appendQueryParameter("commentor", InteractAdapter.this.employeeName)
              .appendQueryParameter("description", InteractAdapter.this.commentDesc));
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
        else Toast.makeText(InteractAdapter.this.context, "Comment Submission failed", Toast.LENGTH_SHORT).show();
        makeVisible();
      }
      catch (Exception e)
      {
        Toast.makeText(InteractAdapter.this.context, "Comment Submission failed", Toast.LENGTH_SHORT).show();
        makeVisible();
      }

    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      this.mDialog = new SweetAlertDialog(InteractAdapter.this.context, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      this.mDialog.getProgressHelper().setBarColor(InteractAdapter.this.context.getResources().getColor(R.color.primary_color_dark));
      this.mDialog.setTitleText("");
      this.mDialog.setCancelable(false);
      this.mDialog.show();
    }
  }





  private class getAllComments extends AsyncTask<String, Void, String>
  {
    SweetAlertDialog mDialog;

    protected String doInBackground(String... paramVarArgs)
    {
      ConnectionHelper helper = new ConnectionHelper();
      helper.createConnection(SharedDataSaveLoad.load(context, context.getResources().getString(R.string.shared_pref_api_url)), "POST");
      helper.addData(new Uri.Builder()
              .appendQueryParameter("action", "kinteract_comment_list")
              .appendQueryParameter("post_id", commentPostId));
      return helper.getResponse();
    }

    protected void onPostExecute(String response)
    {
      super.onPostExecute(response);
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

        String path_kinteract_comm=context.getCacheDir().getAbsolutePath()+"/"+context.getResources().getString(R.string.file_kinteract)+"_"+commentPostId;
        File file = new File(path_kinteract_comm);
        if(!file.exists())
        {
          file.createNewFile();
          FileOutputStream stream = null;
          try {
            stream = new FileOutputStream(file);
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
          try {
            stream.write(response.getBytes());
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            try {
              stream.close();
              if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
              makeVisible();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        else
        {
          if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
          makeVisible();
        }

      }
      catch (Exception e)
      {
        makeVisible();
        Toast.makeText(context,""+e,Toast.LENGTH_SHORT).show();
      }
    }

    protected void onPreExecute()
    {
      super.onPreExecute();

      commentors=new ArrayList<>();
      commentDates=new ArrayList<>();
      commentDescs=new ArrayList<>();

      this.mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      this.mDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.primary_color_dark));
      this.mDialog.setTitleText("");
      this.mDialog.setCancelable(false);
      this.mDialog.show();
    }
  }






}
