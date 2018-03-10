package com.asif.linkkin;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asif.linkkin.models.KincomingDetailsInfo;
import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.utils.Font;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kanchan on 11/29/2015.
 */
public class KincomingDetailsActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView txtTitle,txtTrainingNo,txtDate,txtTime,txtDuration,txtGroup,txtResource,txtTarget,txtVenue,txtParticipant,txtStatus;

    String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kincoming_details);
        DisconnectClass disconnectClass=new DisconnectClass(this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        txtTitle=(TextView)findViewById(R.id.tool_bar_title);
        txtTrainingNo=(TextView)findViewById(R.id.txtTrainingNo);
        txtDate=(TextView)findViewById(R.id.txtDate);
        txtTime=(TextView)findViewById(R.id.txtTime);
        txtDuration=(TextView)findViewById(R.id.txtDuration);
        txtGroup=(TextView)findViewById(R.id.txtGroup);
        txtResource=(TextView)findViewById(R.id.txtResource);
        txtTarget=(TextView)findViewById(R.id.txtTarget);
        txtVenue=(TextView)findViewById(R.id.txtVenue);
        txtParticipant=(TextView)findViewById(R.id.txtParticipant);
        txtStatus=(TextView)findViewById(R.id.txtStatus);

        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtTitle);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtTrainingNo);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtDate);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtTime);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtDuration);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtGroup);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtResource);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtTarget);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtVenue);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtParticipant);
        Font.LATO_Regular.apply(KincomingDetailsActivity.this, txtStatus);

        Font.LATO_Regular.apply(this,(TextView)findViewById(R.id.txtDate1));
        Font.LATO_Regular.apply(this,(TextView)findViewById(R.id.txtTime1));
        Font.LATO_Regular.apply(this,(TextView)findViewById(R.id.txtDuration1));
        Font.LATO_Regular.apply(this,(TextView)findViewById(R.id.txtGroup1));
        Font.LATO_Regular.apply(this,(TextView)findViewById(R.id.txtResource1));
        Font.LATO_Regular.apply(this,(TextView)findViewById(R.id.txtTarget1));
        Font.LATO_Regular.apply(this,(TextView)findViewById(R.id.txtVenue1));
        Font.LATO_Regular.apply(this,(TextView)findViewById(R.id.txtParticipant1));
        Font.LATO_Regular.apply(this, (TextView) findViewById(R.id.txtStatus1));

        KincomingDetailsInfo info = getIntent().getParcelableExtra("info");

        txtTitle.setText(info.title);
        txtTrainingNo.setText("Event Training No: "+info.trainingNo);

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yy");
        try {
            Date comDate1=format1.parse(info.startDate);
            Calendar calCom1 = Calendar.getInstance();
            calCom1.setTime(comDate1);
            int comYear1=calCom1.get(Calendar.YEAR);
            int comMonth1=calCom1.get(Calendar.MONTH);
            int comDay1=calCom1.get(Calendar.DAY_OF_MONTH);
            String comSDate1=months[comMonth1]+" "+comDay1+", "+comYear1;

            Date comDate2=format1.parse(info.endDate);
            Calendar calCom2 = Calendar.getInstance();
            calCom2.setTime(comDate2);
            int comYear2=calCom2.get(Calendar.YEAR);
            int comMonth2=calCom2.get(Calendar.MONTH);
            int comDay2=calCom2.get(Calendar.DAY_OF_MONTH);
            String comSDate2=months[comMonth2]+" "+comDay2+", "+comYear2;

            txtDate.setText(comSDate1+" TO "+comSDate2);
        }
        catch (Exception e) {}

        txtTime.setText(info.startTime+" TO "+info.endTime);


        if(Integer.valueOf(info.duration)>=2) txtDuration.setText(info.duration+" days");
        else txtDuration.setText(info.totalTime);
        txtGroup.setText(info.group);
        txtResource.setText(info.resource);
        txtTarget.setText(info.target);
        txtVenue.setText(info.venue);
        txtParticipant.setText(info.participant);
        txtStatus.setText(info.status);


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
    }



    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        DisconnectClass.resetDisconnectTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisconnectClass.removeActivity(this);
    }
}
